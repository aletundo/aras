package it.unimib.disco.aras.reportsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Report {
	@Id
	private String id;
	private String analysisId;
	private String reportPath;
	private Date createdAt;
}
