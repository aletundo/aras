package it.unimib.disco.aras.analysesexecutorservice.repository;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.entity.AnalysisConfiguration;
import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisJobService;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisStatus;
import it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisEventHandler.
 */
@Service
@RepositoryEventHandler(Analysis.class)

/** The Constant log. */
@Slf4j
public class AnalysisEventHandler {

	/** The producer. */
	@Autowired
	private Producer<AnalysisMessage> producer;
	
	/** The analysis job service. */
	@Autowired
	private AnalysisJobService analysisJobService;
	
	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;
	
	/** The Constant analysesConfiguratorServiceBaseUrl. */
	private static final String analysesConfiguratorServiceBaseUrl = "http://analyses-configurator-service";
	
	/**
	 * Handle creating analysis.
	 *
	 * @param analysis
	 *            the analysis
	 */
	@HandleBeforeCreate
	public void handleCreatingAnalysis(Analysis analysis) {
		
		if(null != analysis.getConfiguration() && null != analysis.getConfiguration().getId()) {
			try {
				ResponseEntity<AnalysisConfiguration> response = restTemplate.getForEntity(
						analysesConfiguratorServiceBaseUrl + "/configurations/{configurationId}",
						AnalysisConfiguration.class, analysis.getConfiguration().getId());
				analysis.setConfiguration(response.getBody());
			} catch(RestClientException e) {
				throw new HttpMessageNotReadableException("Validation failed");
			}
			
		} else {
			throw new HttpMessageNotReadableException("Validation failed");
		}
		
		if(null == analysis.getStartTime()) {
			analysis.setStartTime(Date.from(Instant.now()));
		}
	}

	/**
	 * Handle analysis created.
	 *
	 * @param analysis
	 *            the analysis
	 */
	@HandleAfterCreate
	public void handleAnalysisCreated(Analysis analysis) {
		log.debug("Analysis with id: " + analysis.getId() + " saved!");
		AnalysisMessage analysisMessage = AnalysisMessage.build(analysis.getId(),
				analysis.getConfiguration().getProjectId(), analysis.getConfiguration().getVersionId(),
				AnalysisStatus.CREATED);
		producer.dispatch(analysisMessage);
		analysisJobService.createJob(analysis.getId());
	}
	
	/**
	 * Handle analysis deleting.
	 *
	 * @param analysis
	 *            the analysis
	 */
	@HandleBeforeDelete
	public void handleAnalysisDeleting(Analysis analysis) {
		analysisJobService.deleteJob(analysis.getId());
	}
	
	/**
	 * Handle analysis saving.
	 *
	 * @param analysis
	 *            the analysis
	 */
	@HandleBeforeSave
	public void handleAnalysisSaving(Analysis analysis) {
		analysisJobService.rescheduleJob(analysis.getId(), analysis.getStartTime());
	}
	
	/**
	 * Check input string.
	 *
	 * @param input
	 *            the input
	 * @return true, if successful
	 */
	private boolean checkInputString(String input) {
		return (input == null || input.trim().length() == 0);
	}
}
