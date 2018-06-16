package it.unimib.disco.aras.notificationsservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.service.NotificationService;
import it.unimib.disco.aras.notificationsservice.stream.AnalysesStream;
import it.unimib.disco.aras.notificationsservice.stream.message.AnalysisMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisConsumerImpl implements Consumer<AnalysisMessage> {
	
	@Autowired
	private NotificationService notificationService;
	
	@StreamListener(AnalysesStream.INPUT)
	public void consume(@Payload AnalysisMessage analysis) {
		log.debug("Analysis message about analysis " + analysis.getId() + " consumed!");
		notificationService.sendAnalysisStatusNotification(analysis);
	}
}
