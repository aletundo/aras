package it.unimib.disco.aras.reportsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 * The Class Report.
 */
@Document

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class Report {
	
	/** The id. */
	@Id
	private String id;
	
	/** The analysis id. */
	private String analysisId;
	
	/** The report path. */
	private String reportPath;
	
	/** The created at. */
	private Date createdAt;
}
