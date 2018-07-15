package it.unimib.disco.aras.reportsservice.stream.message;

import lombok.Builder;
import lombok.Data;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class ReportMessage {
	
	/** The id. */
	private String id;
	
	/** The analysis id. */
	private String analysisId;
	
	/** The download uri string. */
	private String downloadUriString;
	
	/** The report status. */
	private ReportStatus reportStatus;
}
