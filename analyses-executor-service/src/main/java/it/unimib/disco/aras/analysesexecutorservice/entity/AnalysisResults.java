package it.unimib.disco.aras.analysesexecutorservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResults {
	@Id
	private String id;
	private String analysisId;
	private String resultsPath;
}
