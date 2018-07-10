package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisConfigurationMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class AnalysisConfigurationConsumerImpl implements Consumer<AnalysisConfigurationMessage> {
	
	private ConcurrentMap<Long, AnalysisConfigurationMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public AnalysisConfigurationConsumerImpl() {
		this.messages = new ConcurrentHashMap<>();
		this.latch = new CountDownLatch(1);
	}
	
	@StreamListener(TestConfigurationStream.CONFIGURATIONS_INPUT)
	public void consume(@Payload AnalysisConfigurationMessage configuration) {
		log.info("Consumed configuration message!");
		this.latch.countDown();
		this.messages.put(this.latch.getCount() + 1, configuration);
	}
}
