package it.unimib.disco.aras.analysesexecutorservice.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.AnalysisResults;
import it.unimib.disco.aras.analysesexecutorservice.repository.AnalysisResultsRepository;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

@Service
@Slf4j
public class AnalysisResultsService {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private AnalysisResultsRepository analysisResultsRepository;

	private static final String ANALYSES_DIR = "/data/analyses/";

	public void createResults(String analysisId) {
		try {
			String resultsPath = zipResultCSVFiles(analysisId);
			AnalysisResults analysisResults = AnalysisResults.builder().analysisId(analysisId).resultsPath(resultsPath)
					.build();
			analysisResultsRepository.save(analysisResults);
			publisher.publishEvent(new AfterCreateEvent(analysisResults));
		} catch (RuntimeException e) {
			log.error("Failed to create results for analysis {}", analysisId);
		}
	}

	public Resource loadResults(String analysisResultsId) throws MalformedURLException, ResourceNotFoundException {
		AnalysisResults analysisResults = analysisResultsRepository.findById(analysisResultsId)
				.orElseThrow(ResourceNotFoundException::new);

		Path filepath = Paths.get(analysisResults.getResultsPath()).toAbsolutePath().normalize();
		Resource resource = new UrlResource(filepath.toUri());

		if (resource.exists()) {
			return resource;
		} else {
			throw new ResourceNotFoundException();
		}
	}

	private String zipResultCSVFiles(String analysisId) throws RuntimeException {
		String resultsDir = ANALYSES_DIR + analysisId + "/results/";
		String zipFilepath = resultsDir + "results.zip";

		try {
			ZipFile zipFile = new ZipFile(zipFilepath);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFolder(resultsDir, parameters);
			return zipFilepath;
		} catch (ZipException e) {
			log.error("Failed to zip CSV files");
			throw new RuntimeException("Failed to zip CSV files");
		}
	}
}
