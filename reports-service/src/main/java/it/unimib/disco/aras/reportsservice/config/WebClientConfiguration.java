package it.unimib.disco.aras.reportsservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The Class WebClientConfiguration.
 */
@Configuration
public class WebClientConfiguration {
	
	/**
	 * Load balanced web client builder.
	 *
	 * @return the web client. builder
	 */
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}
}
