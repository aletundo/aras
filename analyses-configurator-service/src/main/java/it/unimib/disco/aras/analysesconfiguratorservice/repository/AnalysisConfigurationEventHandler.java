/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;
import it.unimib.disco.aras.analysesconfiguratorservice.stream.message.AnalysisConfigurationMessage;
import it.unimib.disco.aras.analysesconfiguratorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisConfigurationEventHandler.
 */
@Service
@RepositoryEventHandler(AnalysisConfiguration.class)

/** The Constant log. */
@Slf4j
public class AnalysisConfigurationEventHandler {

	/** The producer. */
	@Autowired
	private Producer<AnalysisConfigurationMessage> producer;

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The Constant projectsServiceBaseUrl. */
	private static final String projectsServiceBaseUrl = "http://projects-service";

	/**
	 * Handle analysis configuration created.
	 *
	 * @param configuration
	 *            the configuration
	 */
	@HandleAfterCreate
	public void handleAnalysisConfigurationCreated(AnalysisConfiguration configuration) {
		log.info("Configuration with id: " + configuration.getId() + " saved!");
		AnalysisConfigurationMessage analysisConfigurationMessage = AnalysisConfigurationMessage.build(
				configuration.getId(), configuration.getProjectId(), configuration.getVersionId(),
				configuration.getArcanParameters());
		producer.dispatch(analysisConfigurationMessage);
	}

	/**
	 * Handle creating analysis configuration.
	 *
	 * @param configuration
	 *            the configuration
	 */
	@HandleBeforeCreate
	public void handleCreatingAnalysisConfiguration(AnalysisConfiguration configuration) {

		if (!isValidConfiguration(configuration)) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
	}

	/**
	 * Checks if is valid configuration.
	 *
	 * @param configuration
	 *            the configuration
	 * @return true, if is valid configuration
	 */
	private boolean isValidConfiguration(AnalysisConfiguration configuration) {
		String projectId = configuration.getProjectId();
		String versionId = configuration.getVersionId();
		// TODO: validate Arcan parameters
		// Map<String, String> arcanParameters = configuration.getArcanParameters();

		try {
			return null != configuration.getArcanParameters() && isValidInputString(projectId)
					&& isValidInputString(versionId)
					&& HttpStatus.OK.equals(restTemplate.getForEntity(
							projectsServiceBaseUrl + "/projects/search/findByVersionId?versionId=" + versionId,
							String.class).getStatusCode());
		} catch (RestClientException e) {
			return false;
		}
	}

	/**
	 * Checks if is valid input string.
	 *
	 * @param input
	 *            the input
	 * @return true, if is valid input string
	 */
	private boolean isValidInputString(String input) {
		return (input != null && input.trim().length() != 0);
	}

}
