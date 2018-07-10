package it.unimib.disco.aras.testsuite.stream.message;

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
