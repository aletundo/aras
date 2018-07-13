package it.unimib.disco.aras.testsuite.web.rest.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AnalysesExecutorServiceClient {
	private final RestTemplate client;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${application.clients.analyses-executor-service}")
	private String baseUrl;

	public AnalysesExecutorServiceClient(RestTemplateBuilder restTemplateBuilder) {
	  HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	  this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
	}

	public ResponseEntity<String> createAnalysis(ObjectNode jsonObject) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(jsonObject), headers);
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.postForEntity(baseUrl + "/analyses",
					httpEntity, String.class);
	    } catch(HttpServerErrorException | HttpClientErrorException e) {
	    	response = ResponseEntity.status(e.getStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
	    }
		
		return response;
	}
	
	public ResponseEntity<String> getAnalysisResultsByAnalysisId(String analysisId)
			throws IOException {
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.getForEntity(baseUrl + "/results/search/findByAnalysisId?analysisId=" + analysisId, String.class);
	    } catch(HttpServerErrorException | HttpClientErrorException e) {
	    	response = ResponseEntity.status(e.getStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
	    }
	    
		return response;
	}
}