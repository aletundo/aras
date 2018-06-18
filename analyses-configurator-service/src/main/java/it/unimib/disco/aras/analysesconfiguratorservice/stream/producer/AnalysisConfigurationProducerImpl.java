package it.unimib.disco.aras.analysesconfiguratorservice.stream.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesconfiguratorservice.stream.AnalysisConfigurationsStream;
import it.unimib.disco.aras.analysesconfiguratorservice.stream.message.AnalysisConfigurationMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConfigurationProducerImpl implements Producer<AnalysisConfigurationMessage> {

	@Autowired
	private AnalysisConfigurationsStream analysisConfigurationsStreams;

	@Override
	public void dispatch(AnalysisConfigurationMessage analysisConfiguration) {
		analysisConfigurationsStreams.outboundAnalysisConfigurations()
				.send(MessageBuilder.withPayload(analysisConfiguration).build());
		log.debug("Configuration with id: " + analysisConfiguration.getId() + " dispatched!");
	}

}
