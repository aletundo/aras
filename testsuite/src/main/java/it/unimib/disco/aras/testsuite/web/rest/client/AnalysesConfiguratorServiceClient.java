package it.unimib.disco.aras.testsuite.web.rest.client;

import java.io.IOException;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AnalysesConfiguratorServiceClient {
	private final RestTemplate client;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${application.clients.analyses-configurator-service}")
	private String baseUrl;

	public AnalysesConfiguratorServiceClient(RestTemplateBuilder restTemplateBuilder) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
	}

	public ResponseEntity<String> createConfiguration(ObjectNode jsonObject)
			throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(jsonObject), headers);
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.postForEntity(baseUrl + "/configurations",
					httpEntity, String.class);
	    } catch(HttpClientErrorException e) {
	    	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body("");
	    }
		
		return response;
	}
}