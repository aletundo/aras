package it.unimib.disco.aras.analysesexecutorservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * The Interface AnalysesStream.
 */
public interface AnalysesStream {
    
    /** The analyses input. */
    final String ANALYSES_INPUT = "analyses-in";
    
    /** The analyses output. */
    final String ANALYSES_OUTPUT = "analyses-out";

    /**
	 * Inbound analyses.
	 *
	 * @return the subscribable channel
	 */
    @Input(ANALYSES_INPUT)
    SubscribableChannel inboundAnalyses();
    
    /**
	 * Outbound analyses.
	 *
	 * @return the message channel
	 */
    @Output(ANALYSES_OUTPUT)
    MessageChannel outboundAnalyses();
}
