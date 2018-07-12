package it.unimib.disco.aras.notificationsservice.stream.message;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
	private String id;
	private NotificationType type;
}
