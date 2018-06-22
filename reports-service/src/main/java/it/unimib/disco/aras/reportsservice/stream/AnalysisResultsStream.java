package it.unimib.disco.aras.reportsservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AnalysisResultsStream {
    final String INPUT = "results-in";

    @Input(INPUT)
    SubscribableChannel inboundAnalysisResults();
}
