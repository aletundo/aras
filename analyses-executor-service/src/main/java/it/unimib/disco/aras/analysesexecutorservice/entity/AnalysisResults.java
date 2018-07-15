package it.unimib.disco.aras.analysesexecutorservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class AnalysisResults.
 */
@Document

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new analysis results.
 */
@NoArgsConstructor

/**
 * Instantiates a new analysis results.
 *
 * @param id
 *            the id
 * @param analysisId
 *            the analysis id
 * @param resultsPath
 *            the results path
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class AnalysisResults {
	
	/** The id. */
	@Id
	private String id;
	
	/** The analysis id. */
	private String analysisId;
	
	/** The results path. */
	private String resultsPath;
}
