package it.unimib.disco.aras.testsuite.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;

@EnableBinding(TestConfigurationStream.class)
public class StreamsConfiguration {

}
