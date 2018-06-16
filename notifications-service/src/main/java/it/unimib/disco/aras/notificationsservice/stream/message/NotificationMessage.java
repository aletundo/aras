package it.unimib.disco.aras.notificationsservice.stream.message;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(staticName="build")
@RequiredArgsConstructor
public class NotificationMessage {
	private String id;
	private NotificationType type;
}
