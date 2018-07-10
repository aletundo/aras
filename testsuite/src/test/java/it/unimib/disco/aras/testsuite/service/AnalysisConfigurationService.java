package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisConfigurationMessage;
import it.unimib.disco.aras.testsuite.web.rest.client.AnalysesConfiguratorServiceClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConfigurationService {
	
	@Autowired
	private AnalysesConfiguratorServiceClient analysesConfiguratorServiceClient;
	
	@Autowired
	private Consumer<AnalysisConfigurationMessage> analysisConfigurationConsumer;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public void createValidConfiguration() throws RestClientException, IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode()
				.put("projectId", "5b25722975a5270001416617")
				.put("versionId", "5b25722975a5270001416618");

		ObjectNode parametersNode = objectMapper.getNodeFactory().objectNode().put("inputMode", "jarsFolderMode");
		
		jsonObject.set("arcanParameters", parametersNode);
		
		ResponseEntity<String> response =  analysesConfiguratorServiceClient.createConfiguration(jsonObject);
		
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();
		
		assertThat(status).isEqualTo(HttpStatus.CREATED);
		assertThat(headers.getLocation()).isNotNull();
		assertThat(body.at("/_links/self").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isNull()).isEqualTo(false);
		
		analysisConfigurationConsumer.getLatch().await(1, TimeUnit.MINUTES);
		assertThat(analysisConfigurationConsumer.getLatch().getCount()).isEqualTo(0);
		assertThat(analysisConfigurationConsumer.getMessages().get(new Long(1)).getProjectId()).isEqualTo("5b25722975a5270001416617");
	}
	
	public void createInvalidConfiguration() throws RestClientException, IOException, InterruptedException {
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode()
				.put("projectId", "")
				.put("versionId", "5b25722975a5270001416618");

		ObjectNode parametersNode = objectMapper.getNodeFactory().objectNode().put("inputMode", "jarsFolderMode");
		
		jsonObject.set("arcanParameters", parametersNode);
		
		ResponseEntity<String> response =  analysesConfiguratorServiceClient.createConfiguration(jsonObject);
		
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();
		
		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(headers.getLocation()).isNull();
	}
}
