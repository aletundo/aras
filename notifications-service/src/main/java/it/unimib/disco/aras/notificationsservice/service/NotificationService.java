package it.unimib.disco.aras.notificationsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import it.unimib.disco.aras.notificationsservice.repository.NotificationRepository;
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
	private NotificationRepository notificationRepository;
	
	public void sendAnalysisStatusNotification(AnalysisMessage analysis) {
		// TODO: 
		// - get subscribers
		// - get subscribers' channels
		// - store notification
		// - run a dedicated job for each channel (eg. QuartzJob)
		// - dispatch a NotificationMessage on Kafka
		
		notificationProducer.dispatch(NotificationMessage.build("", NotificationType.STATUS));
	}
}
