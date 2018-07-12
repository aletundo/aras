package it.unimib.disco.aras.notificationsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
	@Id
	private String id;
	private NotificationType notificationType;
	private Date createdAt;
	
}
