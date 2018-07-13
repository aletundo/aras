package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisStatus;
import it.unimib.disco.aras.testsuite.stream.message.NotificationMessage;
import it.unimib.disco.aras.testsuite.web.rest.client.NotificationsServiceClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	private Consumer<NotificationMessage> analysisConsumer;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NotificationsServiceClient notificationsServiceClient;

	final Set<String> STATUSES;

	public NotificationService() {
		this.STATUSES = newLinkedHashSet(AnalysisStatus.CREATED.name(), AnalysisStatus.SCHEDULED.name(),
				AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());
	}

	public void verifyAnalysisCreatedStatusNotification(String analysisId) throws IOException {
		Condition<String> created = new Condition<>(STATUSES::contains, AnalysisStatus.CREATED.name());
		ResponseEntity<String> response = notificationsServiceClient.getStatusNotificationsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		List<String> receivedStatuses = extractReceivedStatuses(body);

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/status-notifications").isArray()).isEqualTo(true);
		assertThat(receivedStatuses).areAtLeastOne(created);
	}
	
	public void verifyAnalysisScheduledStatusNotification(String analysisId) throws IOException {
		Condition<String> scheduled = new Condition<>(STATUSES::contains, AnalysisStatus.SCHEDULED.name());
		ResponseEntity<String> response = notificationsServiceClient.getStatusNotificationsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		List<String> receivedStatuses = extractReceivedStatuses(body);

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/status-notifications").isArray()).isEqualTo(true);
		assertThat(receivedStatuses).areAtLeastOne(scheduled);
	}
	
	public void verifyAnalysisRunningStatusNotification(String analysisId) throws IOException {
		Condition<String> running = new Condition<>(STATUSES::contains, AnalysisStatus.RUNNING.name());
		ResponseEntity<String> response = notificationsServiceClient.getStatusNotificationsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		List<String> receivedStatuses = extractReceivedStatuses(body);

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/status-notifications").isArray()).isEqualTo(true);
		assertThat(receivedStatuses).areAtLeastOne(running);
	}
	
	public void verifyAnalysisCompletedStatusNotification(String analysisId) throws IOException {
		Condition<String> complted = new Condition<>(STATUSES::contains, AnalysisStatus.COMPLETED.name());
		ResponseEntity<String> response = notificationsServiceClient.getStatusNotificationsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		List<String> receivedStatuses = extractReceivedStatuses(body);

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/status-notifications").isArray()).isEqualTo(true);
		assertThat(receivedStatuses).areAtLeastOne(complted);
	}
	
	public void verifyAnalysisFailedStatusNotification(String analysisId) throws IOException {
		Condition<String> failed = new Condition<>(STATUSES::contains, AnalysisStatus.FAILED.name());
		ResponseEntity<String> response = notificationsServiceClient.getStatusNotificationsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();
		List<String> receivedStatuses = extractReceivedStatuses(body);

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/status-notifications").isArray()).isEqualTo(true);
		assertThat(receivedStatuses).areAtLeastOne(failed);
	}
	
	private List<String> extractReceivedStatuses(final JsonNode body){
		List<String> receivedStatuses = new LinkedList<>();

		body.at("/_embedded/status-notifications").forEach(n -> {
			receivedStatuses.add(n.at("/analysisStatus").asText());
		});
		
		return receivedStatuses;
	}
}
