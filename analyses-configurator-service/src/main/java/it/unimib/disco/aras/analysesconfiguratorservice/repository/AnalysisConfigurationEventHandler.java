package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;
import it.unimib.disco.aras.analysesconfiguratorservice.stream.message.AnalysisConfigurationMessage;
import it.unimib.disco.aras.analysesconfiguratorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@RepositoryEventHandler(AnalysisConfiguration.class)
@Slf4j
public class AnalysisConfigurationEventHandler {

	@Autowired
	private Producer<AnalysisConfigurationMessage> producer;

	@Autowired
	private RestTemplate restTemplate;

	private static final String projectsServiceBaseUrl = "http://projects-service";

	@HandleAfterCreate
	public void handleAnalysisConfigurationCreated(AnalysisConfiguration configuration) {
		log.info("Configuration with id: " + configuration.getId() + " saved!");
		AnalysisConfigurationMessage analysisConfigurationMessage = AnalysisConfigurationMessage.build(
				configuration.getId(), configuration.getProjectId(), configuration.getVersionId(),
				configuration.getArcanParameters());
		producer.dispatch(analysisConfigurationMessage);
	}

	@HandleBeforeCreate
	public void handleCreatingAnalysisConfiguration(AnalysisConfiguration configuration) {

		if (!isValidConfiguration(configuration)) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
	}

	private boolean isValidConfiguration(AnalysisConfiguration configuration) {
		String projectId = configuration.getProjectId();
		String versionId = configuration.getVersionId();
		// TODO: validate Arcan parameters
		// Map<String, String> arcanParameters = configuration.getArcanParameters();

		return isValidInputString(projectId) && isValidInputString(versionId)
				&& HttpStatus.OK.equals(restTemplate.getForEntity(
						projectsServiceBaseUrl + "/projects/search/findByVersionId?versionId=" + versionId,
						String.class).getStatusCode());
	}

	private boolean isValidInputString(String input) {
		return (input != null && input.trim().length() != 0);
	}

}
