/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.stream.message;

import java.util.Map;

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
 * @param versionId
 *            the version id
 * @param arcanParameters
 *            the arcan parameters
 * @return the analysis configuration message
 */
@AllArgsConstructor(staticName="build")

/**
 * Instantiates a new analysis configuration message.
 */
@RequiredArgsConstructor
public class AnalysisConfigurationMessage {
    
    /** The id. */
    private String id;
    
    /** The project id. */
    private String projectId;
    
    /** The version id. */
    private String versionId;
    
    /** The arcan parameters. */
    private Map<String, Boolean> arcanParameters;
}
