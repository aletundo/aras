package it.unimib.disco.aras.notificationsservice.stream.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.stream.AnalysesStream;
import it.unimib.disco.aras.notificationsservice.stream.message.ReportMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportsConsumerImpl implements Consumer<ReportMessage> {
	
	@StreamListener(AnalysesStream.INPUT)
	public void consume(@Payload ReportMessage report) {
		log.debug("Report message about analysis " + report.getId() + " consumed!");
	}
}
