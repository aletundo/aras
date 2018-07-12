package it.unimib.disco.aras.testsuite.web.rest.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationsServiceClient {
	private final RestTemplate client;

	@Autowired
	private ObjectMapper objectMapper;

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
	    } catch(HttpClientErrorException e) {
	    	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body("");
	    }
		
		return response;
	}
}