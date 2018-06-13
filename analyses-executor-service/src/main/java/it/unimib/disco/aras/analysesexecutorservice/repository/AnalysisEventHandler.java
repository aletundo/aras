package it.unimib.disco.aras.analysesexecutorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@RepositoryEventHandler(Analysis.class)
@Slf4j
public class AnalysisEventHandler {
    
    @Autowired
    private Producer<Analysis> producer;
    
    @HandleAfterCreate
    public void handleAnalysisConfigurationCreated(Analysis analysis) {
        log.info("Configuration with id: " + analysis.getId() + " saved!");
        producer.dispatch(analysis);
    }

}
