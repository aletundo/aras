package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimib.disco.aras.testsuite.web.rest.client.ProjectsServiceClient;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectsServiceClient projectsServiceServiceClient;
	
	@Autowired
	private ObjectMapper objectMapper;

	public void createValidProject() throws IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode().put("name", "TestProject")
				.put("description", "A beautiful description");

		ResponseEntity<String> response = projectsServiceServiceClient.createProject(jsonObject);

		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();

		assertThat(status).isEqualTo(HttpStatus.CREATED);
		assertThat(headers.getLocation()).isNotNull();
		assertThat(body.at("/_links/self").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isNull()).isEqualTo(false);
	}
	
	public void createInvalidProject() throws IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode().put("name", "TestProject");

		ResponseEntity<String> response = projectsServiceServiceClient.createProject(jsonObject);

		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(headers.getLocation()).isNull();
	}
}
