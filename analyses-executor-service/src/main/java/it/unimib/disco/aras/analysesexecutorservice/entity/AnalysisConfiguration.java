package it.unimib.disco.aras.analysesexecutorservice.entity;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnalysisConfiguration {
    
    private String projectId;
    private String versionId;
    private Map<String, String> arcanParameters;
}
