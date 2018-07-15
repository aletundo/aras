package it.unimib.disco.aras.analysesexecutorservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.analysesexecutorservice.entity.AnalysisResults;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisResultsMessage;
import it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisResultsEventHandler.
 */
@Service
@RepositoryEventHandler(AnalysisResults.class)

/** The Constant log. */
@Slf4j
public class AnalysisResultsEventHandler {

	/** The producer. */
	@Autowired
	private Producer<AnalysisResultsMessage> producer;

	/**
	 * Handle analysis results created.
	 *
	 * @param analysisResults
	 *            the analysis results
	 */
	@HandleAfterCreate
	public void handleAnalysisResultsCreated(AnalysisResults analysisResults) {
		log.debug("Analysis results with id: " + analysisResults.getId() + " saved!");
		String downloadUriString = String.format("/results/%s/download", analysisResults.getId());
		AnalysisResultsMessage analysisResultsMessage = AnalysisResultsMessage.builder()
				.analysisId(analysisResults.getAnalysisId())
				.id(analysisResults.getId())
				.downloadUriString(downloadUriString)
				.build();
		producer.dispatch(analysisResultsMessage);
	}
}
