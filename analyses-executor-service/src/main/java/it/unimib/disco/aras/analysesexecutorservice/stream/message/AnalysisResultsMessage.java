package it.unimib.disco.aras.analysesexecutorservice.stream.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisResultsMessage {
	private String id;
	private String analysisId;
	private String downloadUriString;
}
