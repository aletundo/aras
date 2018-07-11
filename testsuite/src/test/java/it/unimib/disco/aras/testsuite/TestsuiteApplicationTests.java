package it.unimib.disco.aras.testsuite;

import java.util.HashMap;
import java.util.Map;

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
import it.unimib.disco.aras.testsuite.service.ProjectService;

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
	private ObjectMapper objectMapper;
	
	@Test
	public void contextLoads() {}

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
		//projectService.uploadInvalidEmptyArtefactsZip(projectId, versionId);
		projectService.uploadValidArtefactsZip(projectId, versionId);
		Map<String, String> arcanParameters = new HashMap<>();
		arcanParameters.put("inputMode", "jarsFolderMode");
		analysisService.createStartNowAnalysisWithValidConfig(projectId, versionId, arcanParameters);
		//projectService.uploadValidEmptyArtefactsZip(projectId, versionId);
		//projectService.uploadInvalidArtefactsZip(projectId, versionId);
	}
}
