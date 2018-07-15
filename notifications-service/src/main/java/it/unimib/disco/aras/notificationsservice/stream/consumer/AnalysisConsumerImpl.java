package it.unimib.disco.aras.notificationsservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.service.NotificationService;
import it.unimib.disco.aras.notificationsservice.stream.AnalysesStream;
import it.unimib.disco.aras.notificationsservice.stream.message.AnalysisMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisConsumerImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class AnalysisConsumerImpl implements Consumer<AnalysisMessage> {
	
	/** The notification service. */
	@Autowired
	private NotificationService notificationService;
	
	/* (non-Javadoc)
	 * @see it.unimib.disco.aras.notificationsservice.stream.consumer.Consumer#consume(java.lang.Object)
	 */
	@StreamListener(AnalysesStream.INPUT)
	public void consume(@Payload AnalysisMessage analysis) {
		log.debug("Analysis message about analysis " + analysis.getId() + " consumed!");
		notificationService.sendAnalysisStatusNotification(analysis);
	}
}
