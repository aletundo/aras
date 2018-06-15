package it.unimib.disco.aras.analysesexecutorservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Analysis {
	@Id
	private String id;
	private Date startTime;
	private Date endTime;
	private AnalysisConfiguration configuration;
}
