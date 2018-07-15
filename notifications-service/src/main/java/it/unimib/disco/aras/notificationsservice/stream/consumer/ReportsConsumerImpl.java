package it.unimib.disco.aras.notificationsservice.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.service.NotificationService;
import it.unimib.disco.aras.notificationsservice.stream.ReportsStream;
import it.unimib.disco.aras.notificationsservice.stream.message.ReportMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportsConsumerImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class ReportsConsumerImpl implements Consumer<ReportMessage> {
	
	/** The notification service. */
	@Autowired
	private NotificationService notificationService;
	
	/* (non-Javadoc)
	 * @see it.unimib.disco.aras.notificationsservice.stream.consumer.Consumer#consume(java.lang.Object)
	 */
	@StreamListener(ReportsStream.INPUT)
	public void consume(@Payload ReportMessage report) {
		notificationService.sendReportNotification(report);
		log.debug("Report message about analysis " + report.getId() + " consumed!");
	}
}
