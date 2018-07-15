package it.unimib.disco.aras.analysesexecutorservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * The Interface AnalysisResultsStream.
 */
public interface AnalysisResultsStream {
    
    /** The results output. */
    final String RESULTS_OUTPUT = "results-out";

    /**
	 * Outbound results.
	 *
	 * @return the message channel
	 */
    @Output(RESULTS_OUTPUT)
    MessageChannel outboundResults();
}
