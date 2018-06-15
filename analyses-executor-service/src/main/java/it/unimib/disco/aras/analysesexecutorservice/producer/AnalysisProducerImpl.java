package it.unimib.disco.aras.analysesexecutorservice.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;
import it.unimib.disco.aras.analysesexecutorservice.streams.AnalysesStreams;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalysisProducerImpl implements Producer<Analysis> {

	@Autowired
	private AnalysesStreams analysesStreams;

	@Override
	public void dispatch(final Analysis analysis) {
		MessageChannel messageChannel = analysesStreams.outboundAnalyses();

		messageChannel.send(MessageBuilder.withPayload(analysis)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
		log.info("Analysis with id: " + analysis.getId() + " dispatched!");
	}

}
