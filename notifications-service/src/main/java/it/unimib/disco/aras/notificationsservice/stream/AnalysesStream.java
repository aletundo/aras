package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * The Interface AnalysesStream.
 */
public interface AnalysesStream {
    
    /** The input. */
    String INPUT = "analyses-in";

    /**
	 * Inbound analyses.
	 *
	 * @return the subscribable channel
	 */
    @Input(INPUT)
    SubscribableChannel inboundAnalyses();
}
