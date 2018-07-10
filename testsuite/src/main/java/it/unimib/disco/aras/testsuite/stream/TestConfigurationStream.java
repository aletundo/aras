package it.unimib.disco.aras.testsuite.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TestConfigurationStream {
    final String ANALYSES_INPUT = "analyses-in";
    final String RESULTS_INPUT = "results-in";
    final String CONFIGURATIONS_INPUT = "configurations-in";
    final String NOTIFICATIONS_INPUT = "notifications-in";
    final String REPORTS_INPUT = "reports-in";

    @Input(ANALYSES_INPUT)
    SubscribableChannel inboundAnalyses();
    
    @Input(RESULTS_INPUT)
    SubscribableChannel inboundResults();
    
    @Input(CONFIGURATIONS_INPUT)
    SubscribableChannel inboundConfigurations();
    
    @Input(NOTIFICATIONS_INPUT)
    SubscribableChannel inboundNotifications();
    
    @Input(REPORTS_INPUT)
    SubscribableChannel inboundReports();
}
