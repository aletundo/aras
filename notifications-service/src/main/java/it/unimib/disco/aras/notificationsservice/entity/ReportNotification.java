package it.unimib.disco.aras.notificationsservice.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import it.unimib.disco.aras.notificationsservice.stream.message.ReportStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ReportNotification extends Notification {
	
	private ReportStatus reportStatus;
	private String reportId;

	@Builder
	private ReportNotification(String id, NotificationType notificationType, Date createdAt, ReportStatus reportStatus,
			String reportId) {
		super(id, notificationType, createdAt);
		this.reportStatus = reportStatus;
		this.reportId = reportId;
	}
}
