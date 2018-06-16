package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotificationsStream {
    String OUTPUT = "notifications-out";

    @Output(OUTPUT)
    MessageChannel outboundNotifications();
}
