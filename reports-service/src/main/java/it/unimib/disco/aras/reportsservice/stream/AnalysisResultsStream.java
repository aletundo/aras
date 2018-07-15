package it.unimib.disco.aras.reportsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * The Interface AnalysisResultsStream.
 */
public interface AnalysisResultsStream {
    
    /** The input. */
    final String INPUT = "results-in";

    /**
	 * Inbound analysis results.
	 *
	 * @return the subscribable channel
	 */
    @Input(INPUT)
    SubscribableChannel inboundAnalysisResults();
}
