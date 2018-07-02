package it.unimib.disco.aras.analysesexecutorservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisResultsService;
import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysesStream;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConsumerImpl implements Consumer<AnalysisMessage> {
	
	@Autowired
	private AnalysisResultsService analysisResultsService;
	
	@StreamListener(AnalysesStream.ANALYSES_INPUT)
	public void consume(@Payload AnalysisMessage analysis) {
		log.debug("Analysis message about analysis " + analysis.getId() + " consumed!");
		switch(analysis.getStatus()){
		case CREATED:
			break;
		case COMPLETED:
			analysisResultsService.createResults(analysis.getId());
			break;
		case FAILED:
			break;
		case PAUSED:
			break;
		case RUNNING:
			break;
		case SCHEDULED:
			break;
		case DELETED:
			break;
		default:
			break;
		}
	}
}
