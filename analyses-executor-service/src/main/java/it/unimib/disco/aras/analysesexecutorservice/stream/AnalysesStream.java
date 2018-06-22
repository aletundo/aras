package it.unimib.disco.aras.analysesexecutorservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AnalysesStream {
    final String ANALYSES_INPUT = "analyses-in";
    final String ANALYSES_OUTPUT = "analyses-out";

    @Input(ANALYSES_INPUT)
    SubscribableChannel inboundAnalyses();
    @Output(ANALYSES_OUTPUT)
    MessageChannel outboundAnalyses();
}
