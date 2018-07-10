package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.NotificationMessage;
import lombok.Data;

@Service
@Data
public class NotificationConsumerImpl implements Consumer<NotificationMessage> {
	private ConcurrentMap<Long, NotificationMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public NotificationConsumerImpl() {
		this.messages = new ConcurrentHashMap<>();
		this.latch = new CountDownLatch(1);
	}
	
	@StreamListener(TestConfigurationStream.NOTIFICATIONS_INPUT)
	public void consume(@Payload NotificationMessage notification) {
		this.latch.countDown();
		this.messages.put(this.latch.getCount() + 1, notification);
	}
}
