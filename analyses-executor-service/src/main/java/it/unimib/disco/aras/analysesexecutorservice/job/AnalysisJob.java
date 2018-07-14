package it.unimib.disco.aras.analysesexecutorservice.job;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.io.Files;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.service.ArtefactsDownloadService;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisStatus;
import it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer;
import it.unimib.disco.essere.main.InterfaceModel;
import it.unimib.disco.essere.main.graphmanager.TypeVertexException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class AnalysisJob extends QuartzJobBean {

	private static final String ANALYSES_DIR = "/data/analyses/";

	private String projectId;

	private String versionId;

	private File analysisDir;

	private String analysisId;

	@Autowired
	private Producer<AnalysisMessage> analysisProducer;

	@Autowired
	private ArtefactsDownloadService artefactsDownloadService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Analysis analysis = (Analysis) context.getMergedJobDataMap().get("analysis");
		analysisProducer.dispatch(AnalysisMessage.build(analysis.getId(), analysis.getConfiguration().getProjectId(),
				analysis.getConfiguration().getVersionId(), AnalysisStatus.RUNNING));
		log.info(
				"Analysis job for project with id: " + analysis.getConfiguration().getProjectId() + " is running now!");
		runAnalysis(analysis);
	}

	private void runAnalysis(Analysis analysis) {
		projectId = analysis.getConfiguration().getProjectId();
		versionId = analysis.getConfiguration().getVersionId();
		analysisId = analysis.getId();

		analysisDir = new File(ANALYSES_DIR + analysis.getId());
		analysisDir.mkdirs();

		artefactsDownloadService.downloadArtefacts(projectId, versionId).subscribe(artefacts -> {
			try {
				prepareArtefacts(artefacts);
				InterfaceModel interfaceModel = configureArcan();
				runArcanDetection(interfaceModel);
			} catch (IOException | ZipException | RuntimeException | TypeVertexException e) {
			}
		});
	}

	private void prepareArtefacts(Resource artefacts) throws IOException, ZipException, RuntimeException {
		InputStream inputStream = null;
		try {
			inputStream = artefacts.getInputStream();
			File tempArtefactsZip = File.createTempFile(analysisId, ".zip");
			FileUtils.copyInputStreamToFile(inputStream, tempArtefactsZip);

			String filepath = analysisDir.getPath() + "/" + tempArtefactsZip.getName();
			File zipArtefacts = Paths.get(filepath).toFile();
			Files.move(tempArtefactsZip, zipArtefacts);

			ZipFile zipFile = new ZipFile(zipArtefacts);
			if (!zipFile.isValidZipFile()) {
				throw new ZipException("Zip file is invalid");
			}
			zipFile.extractAll(analysisDir.getAbsolutePath());
			zipArtefacts.delete();
			checkValidJarFolder();
		} catch (IOException | ZipException | RuntimeException e) {
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, projectId, versionId, AnalysisStatus.FAILED));
			log.error("Analysis job for project with id: " + versionId + " failed due to {}", e.getMessage());
			throw e;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void checkValidJarFolder() throws RuntimeException {
		File[] files = analysisDir.listFiles();

		if (1 != files.length || 1 > files[0].listFiles().length) {
			throw new RuntimeException("Invalid JARs folder");
		}
	}

	private InterfaceModel configureArcan() {
		InterfaceModel interfaceModel = new InterfaceModel();
		interfaceModel.set_jarsFolderMode(true);
		interfaceModel.setProjectFolder(analysisDir);
		interfaceModel.setOutputFolder(new File(analysisDir + "/results/"));
		interfaceModel.createOutPutFolder();
		return interfaceModel;
	}

	private void runArcanDetection(InterfaceModel interfaceModel) throws TypeVertexException, IOException {
		interfaceModel.buildGraphTinkerpop();
		try {
			interfaceModel.runCycleDetectorShapeFilter();
			interfaceModel.runHubLikeDependencies();
			interfaceModel.runUnstableDependencies();
			interfaceModel.createCSVClassesMetrics();
			interfaceModel.createCSVPackageMetrics();
			analysisProducer
					.dispatch(AnalysisMessage.build(analysisId, projectId, versionId, AnalysisStatus.COMPLETED));
			log.info("Analysis job for project with id: " + projectId + " completed!");
		} catch (TypeVertexException | IOException e) {
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, projectId, versionId, AnalysisStatus.FAILED));
			log.error("Analysis job for project with id: " + versionId + " failed due to {}", e.getMessage());
			throw e;
		} finally {
			interfaceModel.closeGraph();
		}
	}
}
