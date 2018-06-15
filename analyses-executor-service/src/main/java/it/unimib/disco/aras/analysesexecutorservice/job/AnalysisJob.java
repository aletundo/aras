package it.unimib.disco.aras.analysesexecutorservice.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.producer.Producer;
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
	
    @Autowired
    private Producer<AnalysisJob> producer;
    
    private String id;
    private Analysis analysis;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Analysis job triggered.");
		runAnalysis(context.getMergedJobDataMap());
	}
	
	private void runAnalysis(JobDataMap map) {
		//InterfaceModel im = new InterfaceModel();
		//modalità di input (jar, .class etc)
		//folder da dove leggere l'input (projectFolder)
		//folder dove salvare il db  (DBFolder)
		//folder dei risultati outFOlder (o outDir)
	}

}
