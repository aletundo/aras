package it.unimib.disco.aras.analysesconfiguratorservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
public class BindingConfiguration {

}
