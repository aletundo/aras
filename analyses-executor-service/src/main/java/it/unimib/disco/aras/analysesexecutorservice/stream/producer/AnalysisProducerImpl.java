package it.unimib.disco.aras.analysesexecutorservice.stream.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.unimib.disco.aras.analysesexecutorservice.stream.AnalysesStream;
import it.unimib.disco.aras.analysesexecutorservice.stream.message.AnalysisMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AnalysisProducerImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class AnalysisProducerImpl implements Producer<AnalysisMessage> {

	/** The analyses streams. */
	@Autowired
	private AnalysesStream analysesStreams;

	/* (non-Javadoc)
	 * @see it.unimib.disco.aras.analysesexecutorservice.stream.producer.Producer#dispatch(java.lang.Object)
	 */
	@Override
	public void dispatch(final AnalysisMessage analysis) {
		MessageChannel messageChannel = analysesStreams.outboundAnalyses();

		messageChannel.send(MessageBuilder.withPayload(analysis)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
		log.debug("Analysis message about analysis " + analysis.getId() + " dispatched!");
	}

}
