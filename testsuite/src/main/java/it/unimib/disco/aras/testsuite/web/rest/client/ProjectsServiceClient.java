package it.unimib.disco.aras.testsuite.web.rest.client;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectsServiceClient {
	private final RestTemplate client;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${application.clients.projects-service}")
	private String baseUrl;

	public ProjectsServiceClient(RestTemplateBuilder restTemplateBuilder) {
	  HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	  this.client = restTemplateBuilder.requestFactory(() -> requestFactory).build();
	}
	
	public ResponseEntity<String> createProject(ObjectNode jsonObject) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(jsonObject), headers);
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.postForEntity(baseUrl + "/projects",
					httpEntity, String.class);
	    } catch(HttpClientErrorException e) {
	    	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body("");
	    }
		
		return response;
	}

	public ResponseEntity<String> createVersion(String projectId, ObjectNode jsonObject) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(jsonObject), headers);
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.exchange(baseUrl + "/projects/" + projectId, HttpMethod.PATCH,
					httpEntity, String.class);
	    } catch(HttpClientErrorException e) {
	    	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body("");
	    }
		
		return response;
	}
	
	public ResponseEntity<String> uploadVersionArtefactsZip(String projectId, String versionId, File zip, boolean skipFile) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
	    if(!skipFile) {
	    	formData.add("artefact", new FileSystemResource(zip));
	    } else {
	    	formData.add("artefact", null);
	    }
	    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(
	    formData, headers);
	    
	    ResponseEntity<String> response;
	    
	    try {
			response = this.client.postForEntity(baseUrl + "/projects/" + projectId + "/versions/" + versionId,
					httpEntity, String.class);
	    } catch(HttpClientErrorException e) {
	    	log.info(e.getMessage());
	    	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body("");
	    }
		
		return response;
	}
}