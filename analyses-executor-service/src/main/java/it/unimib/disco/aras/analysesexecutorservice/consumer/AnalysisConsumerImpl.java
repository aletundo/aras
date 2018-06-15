package it.unimib.disco.aras.analysesexecutorservice.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConsumerImpl implements Consumer<Analysis> {
	
	@StreamListener(Sink.INPUT)
	public void consume(@Payload Analysis analysis) {
		log.info("Analysis with id: " + analysis.getId() + " retrieved!");
	}

}
