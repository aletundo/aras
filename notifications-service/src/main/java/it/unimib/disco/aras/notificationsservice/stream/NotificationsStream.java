package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * The Interface NotificationsStream.
 */
public interface NotificationsStream {
    
    /** The output. */
    String OUTPUT = "notifications-out";

    /**
	 * Outbound notifications.
	 *
	 * @return the message channel
	 */
    @Output(OUTPUT)
    MessageChannel outboundNotifications();
}
