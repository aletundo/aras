package it.unimib.disco.aras.analysesexecutorservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new analysis.
 */
@NoArgsConstructor
@Document
public class Analysis {
	
	/** The id. */
	@Id
	private String id;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The configuration. */
	private AnalysisConfiguration configuration;
}
