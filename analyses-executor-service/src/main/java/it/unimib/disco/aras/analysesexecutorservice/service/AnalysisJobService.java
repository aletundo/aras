package it.unimib.disco.aras.analysesexecutorservice.service;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.job.AnalysisJob;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisJobService {
	
	@Autowired
	private Scheduler scheduler;
	
	public void createJob(Analysis analysis) {
		try {
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("analysis", analysis);
			JobDetail job = JobBuilder.newJob(AnalysisJob.class)
					  .withIdentity(analysis.getId(), analysis.getConfiguration().getProjectId() + "-" + analysis.getConfiguration().getVersionId())
					  .setJobData(jobDataMap)
					  .build();
			Trigger trigger = TriggerBuilder.newTrigger()
					  .withIdentity(analysis.getId(), analysis.getConfiguration().getProjectId() + "-" + analysis.getConfiguration().getVersionId())
					  .startAt(analysis.getStartTime())
					  .build();
			scheduler.scheduleJob(job, trigger);
			log.info("Scheduled analysis with id: " + analysis.getId());
		} catch (SchedulerException e) {
			log.error("Failed to schedule analysis with id: " + analysis.getId());
		}
	}
}
