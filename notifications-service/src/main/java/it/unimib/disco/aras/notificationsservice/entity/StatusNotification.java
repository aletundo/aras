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

/**
 * The Class StatusNotification.
 */
@Document

/**
 * Instantiates a new status notification.
 *
 * @param analysisStatus
 *            the analysis status
 * @param analysisId
 *            the analysis id
 */
@AllArgsConstructor

/**
 * Instantiates a new status notification.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see it.unimib.disco.aras.notificationsservice.entity.Notification#hashCode()
 */
@EqualsAndHashCode(callSuper=true)

/* (non-Javadoc)
 * @see it.unimib.disco.aras.notificationsservice.entity.Notification#toString()
 */
@ToString(callSuper=true)
@Data
public class StatusNotification extends Notification{
	
	/** The analysis status. */
	private AnalysisStatus analysisStatus;
	
	/** The analysis id. */
	private String analysisId;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Builder
	private StatusNotification(String id, NotificationType notificationType, Date createdAt, AnalysisStatus analysisStatus,
			String analysisId) {
		super(id, notificationType, createdAt);
		this.analysisStatus = analysisStatus;
		this.analysisId = analysisId;
	}
}
