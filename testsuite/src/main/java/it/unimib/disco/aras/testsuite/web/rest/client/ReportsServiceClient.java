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
public class ReportsServiceClient {
	private final RestTemplate client;

	@Value("${application.clients.reports-service}")
	private String baseUrl;

	public ReportsServiceClient(RestTemplateBuilder restTemplateBuilder) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
	}

	public ResponseEntity<String> getReportsByAnalysisId(String analysisId)
			throws IOException {
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.getForEntity(baseUrl + "/reports/search/findByAnalysisId?analysisId=" + analysisId, String.class);
	    } catch(HttpServerErrorException | HttpClientErrorException e) {
	    	response = ResponseEntity.status(e.getStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
	    }
		
		return response;
	}
	
	public ResponseEntity<String> getReportsByReportId(String reportId)
			throws IOException {
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.getForEntity(baseUrl + "/reports/" + reportId, String.class);
	    } catch(HttpServerErrorException | HttpClientErrorException e) {
	    	response = ResponseEntity.status(e.getStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
	    }
		
		return response;
	}
}