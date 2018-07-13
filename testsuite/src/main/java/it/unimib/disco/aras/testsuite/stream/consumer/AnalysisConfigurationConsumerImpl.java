package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisConfigurationMessage;
import lombok.Data;

@Service
@Data
public class AnalysisConfigurationConsumerImpl implements Consumer<AnalysisConfigurationMessage> {
	
	private List<AnalysisConfigurationMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public AnalysisConfigurationConsumerImpl() {
		this.messages = new LinkedList<>();
		this.latch = new CountDownLatch(1);
	}
	
	@StreamListener(TestConfigurationStream.CONFIGURATIONS_INPUT)
	public void consume(@Payload AnalysisConfigurationMessage configuration) {
		this.latch.countDown();
		this.messages.add(configuration);
	}
}
