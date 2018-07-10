package it.unimib.disco.aras.testsuite.web.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
}