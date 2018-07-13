package it.unimib.disco.aras.testsuite.web.rest.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationsServiceClient {
	private final RestTemplate client;

	@Value("${application.clients.notifications-service}")
	private String baseUrl;

	public NotificationsServiceClient(RestTemplateBuilder restTemplateBuilder) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
	}

	public ResponseEntity<String> getStatusNotificationsByAnalysisId(String analysisId)
			throws IOException {
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.getForEntity(baseUrl + "/status-notifications/search/findByAnalysisId?analysisId=" + analysisId, String.class);
	    } catch(HttpServerErrorException | HttpClientErrorException e) {
	    	response = ResponseEntity.status(e.getStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
	    }
		
		return response;
	}
}