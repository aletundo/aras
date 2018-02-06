package it.unimib.disco.aras.analysesconfiguratorservice.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConfigurationProducerImpl implements Producer<AnalysisConfiguration> {
    
    @Autowired
    private Source source;

    @Override
    public void dispatch(AnalysisConfiguration analysisConfiguration) {
        source.output().send(MessageBuilder.withPayload(analysisConfiguration).build());
        log.info("Configuration with id: " + analysisConfiguration.getId() + " dispatched!");
    }

}
