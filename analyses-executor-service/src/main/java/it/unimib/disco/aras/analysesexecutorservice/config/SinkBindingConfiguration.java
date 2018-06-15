package it.unimib.disco.aras.analysesexecutorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class SinkBindingConfiguration {

}
