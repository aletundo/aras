package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisMessage;
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
	
	private DateFormat dateFormat;
	
	public AnalysisService() {
		this.dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	}

	public ResponseEntity<String> createStartNowAnalysisWithValidConfig(String configurationId) throws IOException, InterruptedException {
		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode().put("id", configurationId);

		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();

		jsonObject.set("configuration", configurationObject);
		jsonObject.put("startTime", "");

		ResponseEntity<String> response = analysesExecutorServiceClient.createAnalysis(jsonObject);

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

	public void createStartNowAnalysisWithInvalidConfig() throws JsonProcessingException {

		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode().put("configurationId", "");

		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();

		jsonObject.set("configuration", configurationObject);
		jsonObject.put("startTime", "");

		ResponseEntity<String> response = analysesExecutorServiceClient.createAnalysis(jsonObject);

		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> createScheduledAnalysisWithValidConfigAndValidStartTime(String configurationId) throws IOException, InterruptedException {

		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode().put("id", configurationId);

		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();

		jsonObject.set("configuration", configurationObject);
		jsonObject.put("startTime", dateFormat.format(Date.from(Instant.now().plus(30, ChronoUnit.SECONDS))));

		ResponseEntity<String> response = analysesExecutorServiceClient.createAnalysis(jsonObject);

		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		HttpHeaders headers = response.getHeaders();

		assertThat(status).isEqualTo(HttpStatus.CREATED);
		assertThat(headers.getLocation()).isNotNull();
		assertThat(body.at("/_links/self").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_links/self").isNull()).isEqualTo(false);

		return response;
	}

	public void createScheduledAnalysisWithInvalidConfigAndValidStartTime() throws JsonProcessingException {
		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode().put("configurationId", "");

		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();

		jsonObject.set("configuration", configurationObject);
		Date startTime = Date.from(Instant.now().plus(1, ChronoUnit.MINUTES));
		jsonObject.put("startTime", dateFormat.format(startTime));

		ResponseEntity<String> response = analysesExecutorServiceClient.createAnalysis(jsonObject);

		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	public void createScheduledAnalysisWithValidConfigAndInvalidStartTime(String configurationId) throws JsonProcessingException {

		ObjectNode configurationObject = objectMapper.getNodeFactory().objectNode().put("id", configurationId);

		ObjectNode jsonObject = objectMapper.getNodeFactory().objectNode();

		jsonObject.set("configuration", configurationObject);
		jsonObject.put("startTime", "Wed. 11 Jul 2018 20:30.16 UTC");

		ResponseEntity<String> response = analysesExecutorServiceClient.createAnalysis(jsonObject);

		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<String> verifyAnalysisResultsCreated(String analysisId) throws IOException {
		ResponseEntity<String> response = analysesExecutorServiceClient.getAnalysisResultsByAnalysisId(analysisId);
		
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		
		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/id").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/id").isNull()).isEqualTo(false);
		assertThat(body.at("/analysisId").textValue()).isEqualTo(analysisId);
		assertThat(body.at("/resultsPath").textValue()).isEqualTo("/data/analyses/" + analysisId + "/results/results.zip");
		
		return response;
	}
	
	public void verifyAnalysisResultsNotCreated(String analysisId) throws IOException {
		ResponseEntity<String> response = analysesExecutorServiceClient.getAnalysisResultsByAnalysisId(analysisId);
		HttpStatus status = response.getStatusCode();
		
		assertThat(status).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	public void verifyAnalysesMessages(final long timeout, final Set<String> statusesToCheck) throws InterruptedException {
		analysisConsumer.getLatch().await(timeout, TimeUnit.MINUTES);
		assertThat(analysisConsumer.getLatch().getCount()).isEqualTo(0);
		
		List<String> receivedStatuses = new LinkedList<>();
		for(AnalysisMessage m : analysisConsumer.getMessages()) {
			receivedStatuses.add(m.getStatus().name());
		}
		
		assertThat(receivedStatuses).containsExactlyInAnyOrderElementsOf(statusesToCheck);
	}
}
