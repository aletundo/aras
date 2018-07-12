package it.unimib.disco.aras.notificationsservice.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import it.unimib.disco.aras.notificationsservice.stream.message.AnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class StatusNotification extends Notification{
	private AnalysisStatus analysisStatus;
	private String analysisId;
	
	@Builder
	private StatusNotification(String id, NotificationType notificationType, Date createdAt, AnalysisStatus analysisStatus,
			String analysisId) {
		super(id, notificationType, createdAt);
		this.analysisStatus = analysisStatus;
		this.analysisId = analysisId;
	}
}
