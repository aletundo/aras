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
import it.unimib.disco.aras.analysesexecutorservice.repository.AnalysisRepository;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisStatus;
import it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisJobService {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private AnalysisRepository analysisRepository;
	
	@Autowired
	private Producer<AnalysisMessage> analysisProducer;
	
	public void createJob(final String analysisId) {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("analysis", analysis);
			JobDetail job = JobBuilder.newJob(AnalysisJob.class)
					  .withIdentity(analysisId, analysis.getConfiguration().getProjectId() + "-" + analysis.getConfiguration().getVersionId())
					  .setJobData(jobDataMap)
					  .build();
			Trigger trigger = TriggerBuilder.newTrigger()
					  .withIdentity(analysis.getId(), analysis.getConfiguration().getProjectId() + "-" + analysis.getConfiguration().getVersionId())
					  .startAt(analysis.getStartTime())
					  .build();
			scheduler.scheduleJob(job, trigger);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, analysis.getConfiguration().getProjectId(), analysis.getConfiguration().getVersionId(), AnalysisStatus.SCHEDULED));
			log.info("Scheduled analysis with id: " + analysisId);
		} catch (SchedulerException e) {
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.FAILED));
			log.error("Failed to schedule analysis with id: " + analysisId);
		}
	}
}
