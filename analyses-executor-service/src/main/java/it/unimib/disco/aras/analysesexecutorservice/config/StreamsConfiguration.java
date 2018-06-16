package it.unimib.disco.aras.analysesexecutorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysesStream;

@EnableBinding(AnalysesStream.class)
public class StreamsConfiguration {

}
