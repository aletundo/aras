package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.ReportMessage;
import lombok.Data;

@Service
@Data
public class ReportConsumerImpl implements Consumer<ReportMessage> {
	
	private List<ReportMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public ReportConsumerImpl() {
		this.messages = new LinkedList<>();
		this.latch = new CountDownLatch(1);
	}
	
	@StreamListener(TestConfigurationStream.REPORTS_INPUT)
	public void consume(@Payload ReportMessage report) {
		this.latch.countDown();
		this.messages.add(report);
	}
}
