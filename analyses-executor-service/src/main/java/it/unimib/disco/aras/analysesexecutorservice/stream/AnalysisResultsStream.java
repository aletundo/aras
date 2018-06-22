package it.unimib.disco.aras.analysesexecutorservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AnalysisResultsStream {
    final String RESULTS_OUTPUT = "results-out";

    @Output(RESULTS_OUTPUT)
    MessageChannel outboundResults();
}
