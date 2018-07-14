package it.unimib.disco.aras.reportsservice.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import it.unimib.disco.aras.reportsservice.entity.Report;
import it.unimib.disco.aras.reportsservice.repository.ReportRepository;
import it.unimib.disco.aras.reportsservice.stream.message.ReportMessage;
import it.unimib.disco.aras.reportsservice.stream.message.ReportStatus;
import it.unimib.disco.aras.reportsservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Service
@Slf4j
public class ReportService {

	private static final String REPORTS_DIR = "/data/reports/";

	@Autowired
	private Producer<ReportMessage> reportProducer;

	@Autowired
	private AnalysisResultsDownloadService analysisResultsDownloadService;

	@Autowired
	private ReportRepository reportRepository;

	public void generateReport(String analysisId, String resultsDownloadUri) {
		analysisResultsDownloadService.downloadAnalysisResults(resultsDownloadUri).subscribe(results -> {
			Date createdAt = Date.from(Instant.now());
			String reportFilenameString = String.format("report-%s-%s", analysisId, createdAt.getTime());
			File reportDir = new File(REPORTS_DIR + analysisId);
			reportDir.mkdirs();
			String reportPath = String.format("%s/%s.pdf", reportDir.getAbsolutePath(), reportFilenameString);
			try {
				unzipResults(results, reportDir);
				createPdf(analysisId, reportFilenameString, reportDir);
				Report report = reportRepository.save(
						Report.builder().analysisId(analysisId).createdAt(createdAt).reportPath(reportPath).build());
				String downloadUriString = String.format("/reports/%s/download", report.getId());

				reportProducer.dispatch(ReportMessage.builder().id(report.getId()).analysisId(analysisId)
						.downloadUriString(downloadUriString).reportStatus(ReportStatus.GENERATED).build());
			} catch (IOException | ZipException | RuntimeException | XDocReportException e) {
				reportProducer.dispatch(
						ReportMessage.builder().analysisId(analysisId).reportStatus(ReportStatus.FAILED).build());
			}
		});
	}

	private void createPdf(String analysisId, String reportFilenameString, File reportDir)
			throws IOException, XDocReportException {
		try {
			InputStream templateInputStream = new ClassPathResource("reportTemplate.odt").getInputStream();
			OutputStream outputStream = new FileOutputStream(reportDir.getPath() + "/" + reportFilenameString + ".pdf");
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateInputStream,
					TemplateEngineKind.Freemarker);
			Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);

			IContext context = report.createContext();
			context.put("analysisId", analysisId);
			context.put("reportId", reportFilenameString);

			report.convert(context, options, outputStream);
			outputStream.close();
		} catch (IOException | XDocReportException e) {
			log.error("Report generation failed due to {}", e.getMessage());
			throw e;
		}
	}

	private void unzipResults(Resource results, File reportDir) throws IOException, ZipException, RuntimeException {
		try {
			InputStream inputStream = results.getInputStream();
			File tempResultsZip = File.createTempFile("results", ".zip");
			FileUtils.copyInputStreamToFile(inputStream, tempResultsZip);

			String filepath = reportDir.getPath() + "/" + tempResultsZip.getName();
			File zipResults = Paths.get(filepath).toFile();
			Files.move(tempResultsZip, zipResults);

			ZipFile zipFile = new ZipFile(zipResults);
			if (!zipFile.isValidZipFile()) {
				throw new ZipException("Zip file is invalid");
			}
			zipFile.extractAll(reportDir.getAbsolutePath());
			zipResults.delete();
		} catch (IOException | ZipException | RuntimeException e) {
			log.error("Report generation failed due to {}", e.getMessage());
			throw e;
		}
	}

	public Resource loadReport(String reportId) throws MalformedURLException {
		Report report = reportRepository.findById(reportId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Path filepath = Paths.get(report.getReportPath()).toAbsolutePath().normalize();
		Resource resource = new UrlResource(filepath.toUri());
		
		if (resource.exists()) {
			return resource;
		} else {
			throw new ResourceNotFoundException();
		}
	}
}
