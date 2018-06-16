package it.unimib.disco.aras.analysesexecutorservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisJobService;
import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysesStream;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConsumerImpl implements Consumer<Analysis> {
	
	@Autowired
	private AnalysisJobService analysisJobService;
	
	@StreamListener(AnalysesStream.INPUT)
	public void consume(@Payload Analysis analysis) {
		log.debug("Analysis with id: " + analysis.getId() + " consumed!");
		analysisJobService.createJob(analysis);
	}

}
