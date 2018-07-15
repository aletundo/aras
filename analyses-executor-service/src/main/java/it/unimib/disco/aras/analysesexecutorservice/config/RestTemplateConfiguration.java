package it.unimib.disco.aras.analysesexecutorservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * The Class RestTemplateConfiguration.
 */
@Configuration
public class RestTemplateConfiguration {
	
	/**
	 * Rest template.
	 *
	 * @param builder
	 *            the builder
	 * @return the rest template
	 */
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
