/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new analysis configuration.
 */
@NoArgsConstructor
public class AnalysisConfiguration {
    
    /** The id. */
    @Id
    private String id;
    
    /** The project id. */
    private String projectId;
    
    /** The version id. */
    private String versionId;
    
    /** The arcan parameters. */
    private Map<String, Boolean> arcanParameters;
}
