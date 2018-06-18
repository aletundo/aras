package it.unimib.disco.aras.analysesconfiguratorservice.stream.message;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(staticName="build")
@RequiredArgsConstructor
public class AnalysisConfigurationMessage {
    private String id;
    private String projectId;
    private String versionId;
    private Map<String, String> arcanParameters;
}
