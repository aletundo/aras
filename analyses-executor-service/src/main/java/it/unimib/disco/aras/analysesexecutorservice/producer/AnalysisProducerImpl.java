package it.unimib.disco.aras.analysesexecutorservice.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisProducerImpl implements Producer<Analysis> {
    
    @Autowired
    private Source source;

    @Override
    public void dispatch(Analysis analysis) {
        source.output().send(MessageBuilder.withPayload(analysis).build());
        log.info("Analysis with id: " + analysis.getId() + " dispatched!");
    }

}

