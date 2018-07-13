package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisMessage;
import lombok.Data;

@Service
@Data
public class AnalysisConsumerImpl implements Consumer<AnalysisMessage> {
	
	private List<AnalysisMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public AnalysisConsumerImpl() {
		this.messages = new LinkedList<>();
		this.latch = new CountDownLatch(4);
	}
	
	@StreamListener(TestConfigurationStream.ANALYSES_INPUT)
	public void consume(@Payload AnalysisMessage analysis) {
		this.latch.countDown();
		this.messages.add(analysis);
	}
}
