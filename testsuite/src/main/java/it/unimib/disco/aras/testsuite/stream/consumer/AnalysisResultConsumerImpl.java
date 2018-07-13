package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.testsuite.stream.TestConfigurationStream;
import it.unimib.disco.aras.testsuite.stream.message.AnalysisResultsMessage;
import lombok.Data;

@Service
@Data
public class AnalysisResultConsumerImpl implements Consumer<AnalysisResultsMessage> {
	
	private List<AnalysisResultsMessage> messages;
	private CountDownLatch latch;
	
	@Autowired
	public AnalysisResultConsumerImpl() {
		this.messages = new LinkedList<>();
		this.latch = new CountDownLatch(1);
	}
	
	@StreamListener(TestConfigurationStream.RESULTS_INPUT)
	public void consume(@Payload AnalysisResultsMessage result) {
		this.latch.countDown();
		this.messages.add(result);
	}
}
