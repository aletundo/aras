package it.unimib.disco.aras.notificationsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new notification.
 *
 * @param id
 *            the id
 * @param notificationType
 *            the notification type
 * @param createdAt
 *            the created at
 */
@AllArgsConstructor

/**
 * Instantiates a new notification.
 */
@NoArgsConstructor
public class Notification {
	
	/** The id. */
	@Id
	private String id;
	
	/** The notification type. */
	private NotificationType notificationType;
	
	/** The created at. */
	private Date createdAt;
	
}
