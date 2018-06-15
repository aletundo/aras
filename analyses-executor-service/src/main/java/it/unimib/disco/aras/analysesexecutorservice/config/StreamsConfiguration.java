package it.unimib.disco.aras.analysesexecutorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.analysesexecutorservice.streams.AnalysesStreams;

@EnableBinding(AnalysesStreams.class)
public class StreamsConfiguration {

}
