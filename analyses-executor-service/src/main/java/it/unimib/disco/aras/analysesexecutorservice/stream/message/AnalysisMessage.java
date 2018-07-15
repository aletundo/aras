package it.unimib.disco.aras.analysesexecutorservice.stream.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Builds the.
 *
 * @param id
 *            the id
 * @param projectId
 *            the project id
 * @param projectVersion
 *            the project version
 * @param status
 *            the status
 * @return the analysis message
 */
@AllArgsConstructor(staticName="build")

/**
 * Instantiates a new analysis message.
 */
@RequiredArgsConstructor
public class AnalysisMessage {
	
	/** The id. */
	private String id;
	
	/** The project id. */
	private String projectId;
	
	/** The project version. */
	private String projectVersion;
	
	/** The status. */
	private AnalysisStatus status;
}
