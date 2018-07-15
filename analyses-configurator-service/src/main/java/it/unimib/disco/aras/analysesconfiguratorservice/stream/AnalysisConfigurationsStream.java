/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * The Interface AnalysisConfigurationsStream.
 */
public interface AnalysisConfigurationsStream {
    
    /** The Constant OUTPUT. */
    public static final String OUTPUT = "analysis-configurations-out";
    
    /**
	 * Outbound analysis configurations.
	 *
	 * @return the message channel
	 */
    @Output(OUTPUT)
    MessageChannel outboundAnalysisConfigurations();
}
