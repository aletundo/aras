package it.unimib.disco.aras.analysesexecutorservice.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.job.AnalysisJob;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisJobProducerImpl implements Producer<AnalysisJob> {
    
    @Autowired
    private Source source;

    @Override
    public void dispatch(AnalysisJob analysisJob) {
        source.output().send(MessageBuilder.withPayload(analysisJob).build());
        log.info("AnalysisJob with id: " + analysisJob.getId() + " dispatched!");
    }

}

