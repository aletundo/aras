package it.unimib.disco.aras.analysesconfiguratorservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AnalysisConfigurationsStream {
    public static final String OUTPUT = "analysis-configurations-out";
    
    @Output(OUTPUT)
    MessageChannel outboundAnalysisConfigurations();
}
