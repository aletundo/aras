package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ReportsStream {
    String INPUT = "reports-in";

    @Input(INPUT)
    SubscribableChannel inboundReports();
}
