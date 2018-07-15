package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * The Interface ReportsStream.
 */
public interface ReportsStream {
    
    /** The input. */
    String INPUT = "reports-in";

    /**
	 * Inbound reports.
	 *
	 * @return the subscribable channel
	 */
    @Input(INPUT)
    SubscribableChannel inboundReports();
}
