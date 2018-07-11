package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisMessage;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisStatus;
import it.unimib.disco.aras.testsuite.web.rest.client.AnalysesExecutorServiceClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisService {
	
	@Autowired
	private AnalysesExecutorServiceClient analysesExecutorServiceClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private Consumer<AnalysisMessage> analysisConsumer;
	
	public ResponseEntity<String> createStartNowAnalysisWithValidConfig(String projectId, String versionId, Map<String, String> arcanParameters) throws IOException, InterruptedException {
		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode()
				.put("projectId", projectId)
				.put("versionId", versionId);

		ObjectNode parametersObject = objectMapper.getNodeFactory().objectNode();
		
		for(String key : arcanParameters.keySet()) {
			parametersObject.put(key, arcanParameters.get(key));
		}
		configurationObject.set("arcanParameters", parametersObject);
		
		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();
		
		jsonObject.set("configuration", configurationObject);
		jsonObject.put("startTime", "");

		ResponseEntity<String> response =  analysesExecutorServiceClient.createAnalysis(jsonObject);
		
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();
		
		assertThat(status).isEqualTo(HttpStatus.CREATED);
		assertThat(headers.getLocation()).isNotNull();
		assertThat(body.at("/_links/self").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isNull()).isEqualTo(false);
		
		analysisConsumer.getLatch().await(1, TimeUnit.MINUTES);
		assertThat(analysisConsumer.getLatch().getCount()).isEqualTo(0);
		assertThat(analysisConsumer.getMessages().get(new Long(4)).getStatus()).isEqualTo(AnalysisStatus.CREATED);
		assertThat(analysisConsumer.getMessages().get(new Long(3)).getStatus()).isEqualTo(AnalysisStatus.SCHEDULED);
		assertThat(analysisConsumer.getMessages().get(new Long(2)).getStatus()).isEqualTo(AnalysisStatus.RUNNING);
		assertThat(analysisConsumer.getMessages().get(new Long(1)).getStatus()).isEqualTo(AnalysisStatus.COMPLETED);
				
		return response;
	}
	
	public ResponseEntity<String> createStartNowAnalysisWithInvalidConfig(String projectId, String versionId, Map<String, String> arcanParameters) {
		
		return null;
	}
	
	public ResponseEntity<String> createScheduledAnalysisWithValidConfigAndValidStartTime(String projectId, String versionId, Map<String, String> arcanParameters) {
		
		return null;
	}
	
	public ResponseEntity<String> createScheduledAnalysisWithInvalidConfigAndValidStartTime(String projectId, String versionId, Map<String, String> arcanParameters) {
		
		return null;
	}
	
	public ResponseEntity<String> createScheduledAnalysisWithValidConfigAndInvalidStartTime(String projectId, String versionId, Map<String, String> arcanParameters) {
		
		return null;
	}
}
