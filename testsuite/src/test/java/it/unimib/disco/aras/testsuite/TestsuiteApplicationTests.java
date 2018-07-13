package it.unimib.disco.aras.testsuite;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.awaitility.Awaitility.await;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import it.unimib.disco.aras.testsuite.stream.message.AnalysisStatus;

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
	private ObjectMapper objectMapper;
	

	@Test
	public void dummy() throws Exception {
		
		//analysisConfigurationService.createValidConfiguration();
		//analysisConfigurationService.createInvalidConfiguration();
		ResponseEntity<String> response = projectService.createValidProject();
		JsonNode body = objectMapper.readTree(response.getBody());
		String projectId = body.get("id").textValue();
		response = projectService.createValidProjectVersion(projectId);
		body = objectMapper.readTree(response.getBody());
		String versionId = body.at("/versions").get(0).get("id").textValue();
		projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		Map<String, String> arcanParameters = new HashMap<>();
		arcanParameters.put("inputMode", "jarsFolderMode");
		response = analysisService.createStartNowAnalysisWithValidConfig(projectId, versionId, arcanParameters);
		body = objectMapper.readTree(response.getBody());
		String analysisId = body.get("id").textValue();
		
		final Set<String> statusesToCheck = newLinkedHashSet(AnalysisStatus.CREATED.name(), AnalysisStatus.SCHEDULED.name(),
				AnalysisStatus.RUNNING.name(), AnalysisStatus.FAILED.name());
		
		analysisService.verifyAnalysesMessages(1, statusesToCheck);
		
		await().atMost(10, SECONDS).untilAsserted(() -> {
			notificationService.verifyAnalysisCreatedStatusNotification(analysisId);
			notificationService.verifyAnalysisScheduledStatusNotification(analysisId);
			notificationService.verifyAnalysisRunningStatusNotification(analysisId);
			notificationService.verifyAnalysisFailedStatusNotification(analysisId);
		});
		
		await().atMost(10, SECONDS).untilAsserted(() -> analysisService.verifyAnalysisResultsNotCreated(analysisId));
		
		//projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		//projectService.uploadInvalidArtefactsZip(projectId, versionId);
	}
}
