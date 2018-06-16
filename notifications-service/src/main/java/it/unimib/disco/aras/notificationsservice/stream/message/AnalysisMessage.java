package it.unimib.disco.aras.notificationsservice.stream.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(staticName="build")
@RequiredArgsConstructor
public class AnalysisMessage {
	private String id;
	private String projectId;
	private String projectVersion;
	private AnalysisStatus status;
}
