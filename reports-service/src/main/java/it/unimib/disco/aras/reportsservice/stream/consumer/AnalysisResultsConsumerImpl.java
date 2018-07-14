package it.unimib.disco.aras.reportsservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.reportsservice.service.ReportService;
import it.unimib.disco.aras.reportsservice.stream.AnalysisResultsStream;
import it.unimib.disco.aras.reportsservice.stream.message.AnalysisResultsMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisResultsConsumerImpl implements Consumer<AnalysisResultsMessage> {
	
	@Autowired
	private ReportService reportService;
	
	@StreamListener(AnalysisResultsStream.INPUT)
	public void consume(@Payload AnalysisResultsMessage analysisResults) {
		reportService.generateReport(analysisResults.getAnalysisId(), analysisResults.getDownloadUriString());
		log.debug("Analysis results message about analysis " + analysisResults.getId() + " consumed!");
	}
}
