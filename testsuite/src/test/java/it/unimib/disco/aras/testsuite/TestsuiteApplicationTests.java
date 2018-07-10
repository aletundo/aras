package it.unimib.disco.aras.testsuite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.unimib.disco.aras.testsuite.service.AnalysisConfigurationService;
import it.unimib.disco.aras.testsuite.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsuiteApplicationTests {
	
	@Autowired
	private AnalysisConfigurationService analysisConfigurationService;
	
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void dummy() throws Exception {
		
		analysisConfigurationService.createValidConfiguration();
		analysisConfigurationService.createInvalidConfiguration();
		projectService.createValidProject();
		projectService.createInvalidProject();
	}
}
