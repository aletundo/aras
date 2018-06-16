package it.unimib.disco.aras.analysesexecutorservice.job;

import java.io.File;
import java.io.IOException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
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

@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class AnalysisJob extends QuartzJobBean {
	@Autowired
	private Producer<AnalysisMessage> analysisProducer;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Analysis analysis = (Analysis) context.getMergedJobDataMap().get("analysis");
		analysisProducer.dispatch(AnalysisMessage.build(analysis.getId(), analysis.getConfiguration().getProjectId(),
				analysis.getConfiguration().getVersionId(), AnalysisStatus.RUNNING));
		log.info(
				"Analysis job for project with id: " + analysis.getConfiguration().getProjectId() + " is running now!");
		runAnalysis(analysis);
		analysisProducer.dispatch(AnalysisMessage.build(analysis.getId(), analysis.getConfiguration().getProjectId(),
				analysis.getConfiguration().getVersionId(), AnalysisStatus.COMPLETED));
		log.info("Analysis job for project with id: " + analysis.getConfiguration().getProjectId() + " completed!");
	}

	private void runAnalysis(Analysis analysis) {
		// File projectFolder = new File("/data/analyses/" + analysis.getId());
		// projectFolder.mkdirs();

		// HACK: hardcoded for debug purposes
		File projectFolder = new File("/data/analyses/junit/");
		File outputFolder = new File("/data/analyses/junit/results/");

		InterfaceModel im = new InterfaceModel();
		im.set_jarsFolderMode(true);
		im.setProjectFolder(projectFolder);
		im.setOutputFolder(outputFolder);
		im.createOutPutFolder();
		im.buildGraphTinkerpop();

		try {
			im.runCycleDetectorShapeFilter();
			im.runHubLikeDependencies();
			im.runUnstableDependencies();
			im.createCSVClassesMetrics();
			im.createCSVPackageMetrics();
		} catch (TypeVertexException | IOException e) {
			analysisProducer
					.dispatch(AnalysisMessage.build(analysis.getId(), analysis.getConfiguration().getProjectId(),
							analysis.getConfiguration().getVersionId(), AnalysisStatus.FAILED));
			log.error(e.getMessage());
			log.error("Analysis job for project with id: " + analysis.getConfiguration().getProjectId() + " failed!");
		} finally {
			im.closeGraph();
		}
	}

}
