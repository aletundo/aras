package it.unimib.disco.aras.testsuite;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unimib.disco.aras.testsuite.service.AnalysisConfigurationService;
import it.unimib.disco.aras.testsuite.service.AnalysisService;
import it.unimib.disco.aras.testsuite.service.NotificationService;
import it.unimib.disco.aras.testsuite.service.ProjectService;
import it.unimib.disco.aras.testsuite.service.ReportService;
import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisConfigurationMessage;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisMessage;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisResultsMessage;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisStatus;
import it.unimib.disco.aras.testsuite.stream.message.NotificationMessage;
import it.unimib.disco.aras.testsuite.stream.message.ReportMessage;
import it.unimib.disco.aras.testsuite.stream.message.ReportStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsuiteApplicationTests {

	@Autowired
	private AnalysisConfigurationService analysisConfigurationService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AnalysisService analysisService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private Consumer<AnalysisConfigurationMessage> analysisConfigurationConsumer;

	@Autowired
	private Consumer<AnalysisMessage> analysisConsumer;

	@Autowired
	private Consumer<AnalysisResultsMessage> analysisResultsConsumer;

	@Autowired
	private Consumer<NotificationMessage> notificationConsumer;

	@Autowired
	private Consumer<ReportMessage> reportConsumer;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void flushConsumers() {
		analysisConfigurationConsumer.setLatch(new CountDownLatch(1));
		analysisConfigurationConsumer.getMessages().clear();
		analysisConsumer.setLatch(new CountDownLatch(4));
		analysisConsumer.getMessages().clear();
		analysisResultsConsumer.setLatch(new CountDownLatch(1));
		analysisResultsConsumer.getMessages().clear();
		notificationConsumer.setLatch(new CountDownLatch(6));
		notificationConsumer.getMessages().clear();
		reportConsumer.setLatch(new CountDownLatch(1));
		reportConsumer.getMessages().clear();
	}

	@Test
	public void path1() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		projectService.attemptToCreateProjectVersionWithInvalidVersionFields(projectId, "Test", "");
	}

	@Test
	public void path2() throws IOException, InterruptedException {
		projectService.createValidProject();
		projectService.attempToCreateProjectVersionWithInvalidProjectId("", "Test", "TestDescription");
	}

	@Test
	public void path3() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		projectService.attemptToCreateProjectVersionWithInvalidVersionFields(projectId, "", "TestDescription");
	}

	@Test
	public void path4() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		projectService.uploadValidArtefactsZipWithInvalidProjectVersion(projectId, "invalidVersionId");
	}

	@Test
	public void path5() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadInvalidArtefactsZip(projectId, versionId);
	}

	@Test
	public void path6() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZipWithInvalidProjectId("invalidProjectId", versionId);
	}

	@Test
	public void path7() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		projectService.uploadValidArtefactsZipWithInvalidProjectVersion(projectId, "invalidVersionId");
	}

	@Test
	public void path8() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createScheduledAnalysisWithValidConfigAndValidStartTime(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.FAILED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisFailedStatusNotification(analysisId);
		});
	}

	@Test
	public void path9() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createStartNowAnalysisWithValidConfig(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.FAILED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisFailedStatusNotification(analysisId);
		});
	}

	@Test
	public void path10() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		analysisConfigurationService.attemptToCreateConfigurationWithNullArcanParameters(projectId, versionId);
	}

	@Test
	public void path11() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		analysisConfigurationService.attemptToCreateConfigurationWithInvalidProjectIdOrInvalidVersionId("", versionId,
				arcanParameters);
	}

	@Test
	public void path12() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		analysisConfigurationService.attemptToCreateConfigurationWithInvalidProjectIdOrInvalidVersionId(projectId, "",
				arcanParameters);
	}

	@Test
	public void path13() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		analysisService.attempToCreateScheduledAnalysisWithValidConfigAndInvalidStartTime(configurationId);
	}

	@Test
	public void path14() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		analysisService.attempToCreateStartNowAnalysisWithInvalidConfig();
	}

	@Test
	public void path15() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createScheduledAnalysisWithValidConfigAndValidStartTime(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path16() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("cycleDependency", true);
		arcanParameters.put("hubLikeDependencies", true);
		arcanParameters.put("unstableDependencies", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createScheduledAnalysisWithValidConfigAndValidStartTime(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path17() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("classMetrics", true);
		arcanParameters.put("packageMetrics", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createScheduledAnalysisWithValidConfigAndValidStartTime(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path18() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("packageMetrics", true);
		arcanParameters.put("hubLikeDependencies", true);
		arcanParameters.put("unstableDependencies", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createScheduledAnalysisWithValidConfigAndValidStartTime(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path19() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("all", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createStartNowAnalysisWithValidConfig(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path20() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("cycleDependency", true);
		arcanParameters.put("hubLikeDependencies", true);
		arcanParameters.put("unstableDependencies", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createStartNowAnalysisWithValidConfig(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path21() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("classMetrics", true);
		arcanParameters.put("packageMetrics", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createStartNowAnalysisWithValidConfig(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}

	@Test
	public void path22() throws IOException, InterruptedException {
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId, "Test", "TestDescription");
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions/0").get("id").textValue();
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, Boolean> arcanParameters = new HashMap<>();
		arcanParameters.put("classMetrics", true);
		arcanParameters.put("hubLikeDependencies", true);
		response = analysisConfigurationService.createValidConfiguration(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String configurationId = body.get("id").textValue();
		response = analysisService.createStartNowAnalysisWithValidConfig(configurationId);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(),
				AnalysisStatus.SCHEDULED.name(), AnalysisStatus.RUNNING.name(), AnalysisStatus.COMPLETED.name());

		analysisService.verifyAnalysesMessages(1, statusesToCheck);

		await().atMost(15, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisCompletedStatusNotification(analysisId);
		});

		await().atMost(15, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsCreated(analysisId));
		await().atMost(15, SECONDS).untilAsserted(() -> reportService.verifyReportGenerated(analysisId));
		reportService.verifyReportGeneratedMessage(ReportStatus.GENERATED);
		await().atMost(15, SECONDS)
				.untilAsserted(() -> notificationService.verifyReportGeneratedNotification(analysisId));
	}
}
