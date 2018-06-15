package it.unimib.disco.aras.analysesexecutorservice.job;

import java.io.File;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.essere.main.InterfaceModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
public class AnalysisJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		runAnalysis(context.getMergedJobDataMap());
	}
	
	private void runAnalysis(JobDataMap map) {
		Analysis analysis = (Analysis) map.get("analysis");
		log.info("Analysis job for project with id: " + analysis.getConfiguration().getProjectId() + " is running now!");
		InterfaceModel im = new InterfaceModel();
		im.set_jarsFolderMode(true);
		
		// File projectFolder = new File("/data/analyses/" + analysis.getId());
		// projectFolder.mkdirs();
		
		// Hardcoded for debug purposes
		File projectFolder = new File("/data/analyses/junit/");
		File outputFolder = new File("/data/analyses/junit/results/");
		im.setProjectFolder(projectFolder);
		im.setOutputFolder(outputFolder);
		im.buildGraphTinkerpop();
		im.closeGraph();
	}

}
