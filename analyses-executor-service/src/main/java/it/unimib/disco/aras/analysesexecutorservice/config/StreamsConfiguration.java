package it.unimib.disco.aras.analysesexecutorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysesStream;
import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysisResultsStream;

/**
 * The Class StreamsConfiguration.
 */
@EnableBinding(value={AnalysesStream.class, AnalysisResultsStream.class})
public class StreamsConfiguration {

}
