package it.unimib.disco.aras.analysesexecutorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
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

	@HandleAfterCreate
	public void handleAnalysisCreated(Analysis analysis) {
		log.debug("Analysis with id: " + analysis.getId() + " saved!");
		analysisJobService.createJob(analysis.getId());
		AnalysisMessage analysisMessage = AnalysisMessage.build(analysis.getId(),
				analysis.getConfiguration().getProjectId(), analysis.getConfiguration().getVersionId(),
				AnalysisStatus.CREATED);
		producer.dispatch(analysisMessage);
	}
	
	@HandleBeforeDelete
	public void handleAnalysisDeleting(Analysis analysis) {
		analysisJobService.deleteJob(analysis.getId());
	}
	
	@HandleBeforeSave
	public void handleAnalysisSaving(Analysis analysis) {
		analysisJobService.rescheduleJob(analysis.getId(), analysis.getStartTime());
	}
}
