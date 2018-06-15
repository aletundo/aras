package it.unimib.disco.aras.analysesexecutorservice.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AnalysesStreams {
    String INPUT = "analyses-in";
    String OUTPUT = "analyses-out";

    @Input(INPUT)
    SubscribableChannel inboundAnalyses();
    @Output(OUTPUT)
    MessageChannel outboundAnalyses();
}
