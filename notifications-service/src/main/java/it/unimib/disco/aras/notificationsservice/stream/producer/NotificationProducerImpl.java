package it.unimib.disco.aras.notificationsservice.stream.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.unimib.disco.aras.notificationsservice.stream.NotificationsStream;
import it.unimib.disco.aras.notificationsservice.stream.message.NotificationMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationProducerImpl implements Producer<NotificationMessage> {

	@Autowired
	private NotificationsStream notificationsStream;

	@Override
	public void dispatch(final NotificationMessage notification) {
		MessageChannel messageChannel = notificationsStream.outboundNotifications();

		messageChannel.send(MessageBuilder.withPayload(notification)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
		log.info("Notification message about notification " + notification.getId() + " dispatched!");
	}

}
