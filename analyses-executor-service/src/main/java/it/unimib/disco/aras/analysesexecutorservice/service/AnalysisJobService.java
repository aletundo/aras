package it.unimib.disco.aras.analysesexecutorservice.service;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
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
	
	public void createJob(final String analysisId) throws RuntimeException {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			final String projectId = analysis.getConfiguration().getProjectId();
			final String versionId = analysis.getConfiguration().getVersionId();
			
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("analysis", analysis);
			JobDetail job = JobBuilder.newJob(AnalysisJob.class)
					  .withIdentity(analysisId, projectId + "-" + versionId)
					  .setJobData(jobDataMap)
					  .build();
			Trigger trigger = TriggerBuilder.newTrigger()
					  .withIdentity(analysis.getId(), projectId + "-" + versionId)
					  .startAt(analysis.getStartTime())
					  .build();
			
			scheduler.scheduleJob(job, trigger);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, projectId, versionId, AnalysisStatus.SCHEDULED));
			log.info("Scheduled analysis with id: " + analysisId);
		} catch (SchedulerException e) {
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.FAILED));
			log.error("Failed to schedule analysis with id: " + analysisId);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void deleteJob(final String analysisId) {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			final String projectId = analysis.getConfiguration().getProjectId();
			final String versionId = analysis.getConfiguration().getVersionId();
			
			scheduler.deleteJob(new JobKey(analysisId, projectId + "-" + versionId));
			log.info("Unscheduled and deleted analysis with id: " + analysisId);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.DELETED));
		} catch (SchedulerException e) {
			log.error("Failed to unschedule and delete analysis with id: " + analysisId);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void pauseJob(final String analysisId) {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			final String projectId = analysis.getConfiguration().getProjectId();
			final String versionId = analysis.getConfiguration().getVersionId();
			
			scheduler.pauseJob(new JobKey(analysisId, projectId + "-" + versionId));
			log.info("Paused analysis with id: " + analysisId);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.PAUSED));
		} catch (SchedulerException e) {
			log.error("Failed to pause analysis with id: " + analysisId);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void resumeJob(final String analysisId) {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			final String projectId = analysis.getConfiguration().getProjectId();
			final String versionId = analysis.getConfiguration().getVersionId();
			
			scheduler.resumeJob(new JobKey(analysisId, projectId + "-" + versionId));
			log.info("Paused analysis with id: " + analysisId);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.RUNNING));
		} catch (SchedulerException e) {
			log.error("Failed to pause analysis with id: " + analysisId);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void rescheduleJob(final String analysisId, Date startTime) {
		try {
			final Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new SchedulerException());
			final String projectId = analysis.getConfiguration().getProjectId();
			final String versionId = analysis.getConfiguration().getVersionId();
			
			Trigger trigger = TriggerBuilder.newTrigger()
					  .withIdentity(analysis.getId(), projectId + "-" + versionId)
					  .startAt(startTime)
					  .build();
			
			scheduler.rescheduleJob(new TriggerKey(analysisId, projectId + "-" + versionId), trigger);
			log.info("Rescheduled analysis with id: " + analysisId);
			analysisProducer.dispatch(AnalysisMessage.build(analysisId, "", "", AnalysisStatus.SCHEDULED));
		} catch (SchedulerException e) {
			log.error("Failed to reschedule analysis with id: " + analysisId);
			throw new RuntimeException(e.getMessage());
		}
	}
}
