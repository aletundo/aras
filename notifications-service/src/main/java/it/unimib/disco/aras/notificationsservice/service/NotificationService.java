package it.unimib.disco.aras.notificationsservice.service;

import java.sql.Date;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import it.unimib.disco.aras.notificationsservice.entity.StatusNotification;
import it.unimib.disco.aras.notificationsservice.repository.StatusNotificationRepository;
import it.unimib.disco.aras.notificationsservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.notificationsservice.stream.message.NotificationMessage;
import it.unimib.disco.aras.notificationsservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	private Producer<NotificationMessage> notificationProducer;

	@Autowired
	private StatusNotificationRepository notificationRepository;

	@Autowired
	private EmailService emailService;

	public void sendAnalysisStatusNotification(AnalysisMessage analysis) {
		// TODO:
		// - get subscribers
		// - get subscribers' channels
		// - run a dedicated job for each channel (eg. QuartzJob)
		emailService.sendSimpleMessage(System.getenv("NOTIFICATIONS_TO_ADDRESS_DEV"),
				"[ARAS] Analysis status notification",
				"The analysis with id " + analysis.getId() + " is on status " + analysis.getStatus());
		StatusNotification notification = StatusNotification.builder().analysisStatus(analysis.getStatus())
				.analysisId(analysis.getId()).createdAt(Date.from(Instant.now()))
				.notificationType(NotificationType.STATUS).build();
		notification = notificationRepository.save(notification);
		notificationProducer
				.dispatch(NotificationMessage.builder().id(notification.getId()).type(NotificationType.STATUS).build());
	}
}
