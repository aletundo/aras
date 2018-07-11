package it.unimib.disco.aras.analysesexecutorservice.repository;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisJobService;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisStatus;
import it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@RepositoryEventHandler(Analysis.class)
@Slf4j
public class AnalysisEventHandler {

	@Autowired
	private Producer<AnalysisMessage> producer;
	
	@Autowired
	private AnalysisJobService analysisJobService;
	
	@HandleBeforeCreate
	public void handleCreatingAnalysis(Analysis analysis) {

		if(null == analysis.getStartTime()) {
			analysis.setStartTime(Date.from(Instant.now()));
		}
		if(checkInputString(analysis.getConfiguration().getProjectId())) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
		if(checkInputString(analysis.getConfiguration().getVersionId())) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
		// TODO: validate Arcan Parameters
	}

	@HandleAfterCreate
	public void handleAnalysisCreated(Analysis analysis) {
		log.debug("Analysis with id: " + analysis.getId() + " saved!");
		AnalysisMessage analysisMessage = AnalysisMessage.build(analysis.getId(),
				analysis.getConfiguration().getProjectId(), analysis.getConfiguration().getVersionId(),
				AnalysisStatus.CREATED);
		producer.dispatch(analysisMessage);
		analysisJobService.createJob(analysis.getId());
	}
	
	@HandleBeforeDelete
	public void handleAnalysisDeleting(Analysis analysis) {
		analysisJobService.deleteJob(analysis.getId());
	}
	
	@HandleBeforeSave
	public void handleAnalysisSaving(Analysis analysis) {
		analysisJobService.rescheduleJob(analysis.getId(), analysis.getStartTime());
	}
	
	private boolean checkInputString(String input) {
		return (input == null || input.trim().length() == 0);
	}
}
