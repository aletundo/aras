package it.unimib.disco.aras.reportsservice.stream.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportMessage {
	private String id;
	private String analysisId;
	private String downloadUri;
}