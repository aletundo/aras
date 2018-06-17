package it.unimib.disco.aras.notificationsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AnalysesStream {
    String INPUT = "analyses-in";

    @Input(INPUT)
    SubscribableChannel inboundAnalyses();
}