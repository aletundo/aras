package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

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

	@HandleAfterCreate
	public void handleAnalysisConfigurationCreated(AnalysisConfiguration configuration) {
		log.info("Configuration with id: " + configuration.getId() + " saved!");
		AnalysisConfigurationMessage analysisConfigurationMessage = AnalysisConfigurationMessage.build(
				configuration.getId(), configuration.getProjectId(), configuration.getVersionId(),
				configuration.getArcanParameters());
		producer.dispatch(analysisConfigurationMessage);
	}

}
