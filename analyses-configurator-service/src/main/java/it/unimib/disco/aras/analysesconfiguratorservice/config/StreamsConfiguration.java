package it.unimib.disco.aras.analysesconfiguratorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.analysesconfiguratorservice.stream.AnalysisConfigurationsStream;

@EnableBinding(AnalysisConfigurationsStream.class)
public class StreamsConfiguration {

}
