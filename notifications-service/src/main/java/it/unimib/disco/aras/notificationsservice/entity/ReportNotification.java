package it.unimib.disco.aras.notificationsservice.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import it.unimib.disco.aras.notificationsservice.stream.message.ReportStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Class ReportNotification.
 */
@Document

/**
 * Instantiates a new report notification.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see it.unimib.disco.aras.notificationsservice.entity.Notification#hashCode()
 */
@EqualsAndHashCode(callSuper = true)

/* (non-Javadoc)
 * @see it.unimib.disco.aras.notificationsservice.entity.Notification#toString()
 */
@ToString(callSuper = true)
@Data
public class ReportNotification extends Notification {
	
	/** The report status. */
	private ReportStatus reportStatus;
	
	/** The report id. */
	private String reportId;
	
	/** The analysis id. */
	private String analysisId;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Builder
	private ReportNotification(String id, NotificationType notificationType, Date createdAt, ReportStatus reportStatus,
			String reportId, String analysisId) {
		super(id, notificationType, createdAt);
		this.reportStatus = reportStatus;
		this.analysisId = analysisId;
		this.reportId = reportId;
	}
}
