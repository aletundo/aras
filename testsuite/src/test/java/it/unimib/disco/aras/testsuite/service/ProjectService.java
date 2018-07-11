package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimib.disco.aras.testsuite.web.rest.client.ProjectsServiceClient;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectsServiceClient projectsServiceServiceClient;
	
	@Autowired
	private ObjectMapper objectMapper;

	public ResponseEntity<String> createValidProject() throws IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode().put("name", "TestProject")
				.put("description", "A beautiful description");

		ResponseEntity<String> response = projectsServiceServiceClient.createProject(jsonObject);

		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();

		assertThat(status).isEqualTo(HttpStatus.CREATED);
		assertThat(headers.getLocation()).isNotNull();
		assertThat(body.at("/id").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isNull()).isEqualTo(false);
		
		return response;
	}
	
	public void createInvalidProject() throws IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode().put("name", "TestProject");

		ResponseEntity<String> response = projectsServiceServiceClient.createProject(jsonObject);

		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(headers.getLocation()).isNull();
	}
	
	public ResponseEntity<String> createValidProjectVersion(String projectId) throws IOException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();
		
		ObjectNode versionObject = objectMapper.getNodeFactory().objectNode().put("name", "TestVersion").put("description", "A beautiful version description");
		ArrayNode versionsArray = objectMapper.getNodeFactory().arrayNode();
		versionsArray.add(versionObject);
		jsonObject.set("versions", versionsArray);

		ResponseEntity<String> response = projectsServiceServiceClient.createVersion(projectId, jsonObject);

		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/versions").isArray()).isEqualTo(true);
		assertThat(body.at("/versions").size()).isEqualTo(1);
		assertThat(body.at("/versions").get(0).at("/name").asText()).isEqualTo("TestVersion");
		
		return response;
	}
	
	public void createInvalidProjectVersion(String projectId) throws IOException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();
		
		ObjectNode versionObject = objectMapper.getNodeFactory().objectNode().put("name", "TestVersion");
		ArrayNode versionsArray = objectMapper.getNodeFactory().arrayNode();
		versionsArray.add(versionObject);
		jsonObject.set("versions", versionsArray);

		ResponseEntity<String> response = projectsServiceServiceClient.createVersion(projectId, jsonObject);

		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<String> uploadValidArtefactsZip(String projectId, String versionId) throws IOException {
		File file = new ClassPathResource("validZip.zip").getFile();
		ResponseEntity<String> response = projectsServiceServiceClient.uploadVersionArtefactsZip(projectId, versionId, file, false);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/versions").get(0).at("/artefactsPath").isMissingNode()).isEqualTo(false);
		return response;
	}
	
	public ResponseEntity<String> uploadValidEmptyArtefactsZip(String projectId, String versionId) throws IOException {
		File file = new ClassPathResource("emptyZip.zip").getFile();
		ResponseEntity<String> response = projectsServiceServiceClient.uploadVersionArtefactsZip(projectId, versionId, file, false);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/versions").get(0).at("/artefactsPath").isMissingNode()).isEqualTo(false);
		return response;
	}
	
	public void uploadInvalidEmptyArtefactsZip(String projectId, String versionId) throws IOException {
		ResponseEntity<String> response = projectsServiceServiceClient.uploadVersionArtefactsZip(projectId, versionId, File.createTempFile("tmp", "tmp"), true);
		HttpStatus status = response.getStatusCode();
		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	public void uploadInvalidArtefactsZip(String projectId, String versionId) throws IOException {
		File file = new ClassPathResource("invalidZip.tar.Z").getFile();
		ResponseEntity<String> response = projectsServiceServiceClient.uploadVersionArtefactsZip(projectId, versionId, file, false);
		HttpStatus status = response.getStatusCode();
		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
