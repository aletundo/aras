package it.unimib.disco.aras.analysesconfiguratorservice.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnalysisConfiguration {
    
    @Id
    private String id;
    private String projectId;
    private String versionId;
    private Map<String, String> arcanParameters;
}
