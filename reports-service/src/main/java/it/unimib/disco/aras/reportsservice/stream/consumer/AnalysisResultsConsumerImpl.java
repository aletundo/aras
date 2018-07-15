package it.unimib.disco.aras.reportsservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.reportsservice.service.ReportService;
import it.unimib.disco.aras.reportsservice.stream.AnalysisResultsStream;
import it.unimib.disco.aras.reportsservice.stream.message.AnalysisResultsMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisResultsConsumerImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class AnalysisResultsConsumerImpl implements Consumer<AnalysisResultsMessage> {
	
	/** The report service. */
	@Autowired
	private ReportService reportService;
	
	/* (non-Javadoc)
	 * @see it.unimib.disco.aras.reportsservice.stream.consumer.Consumer#consume(java.lang.Object)
	 */
	@StreamListener(AnalysisResultsStream.INPUT)
	public void consume(@Payload AnalysisResultsMessage analysisResults) {
		reportService.generateReport(analysisResults.getAnalysisId(), analysisResults.getDownloadUriString());
		log.debug("Analysis results message about analysis " + analysisResults.getId() + " consumed!");
	}
}
