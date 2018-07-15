package it.unimib.disco.aras.notificationsservice.stream.message;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Instantiates a new notification message.
 *
 * @param id
 *            the id
 * @param type
 *            the type
 */
@AllArgsConstructor

/**
 * Instantiates a new notification message.
 */
@NoArgsConstructor
public class NotificationMessage {
	
	/** The id. */
	private String id;
	
	/** The type. */
	private NotificationType type;
}
