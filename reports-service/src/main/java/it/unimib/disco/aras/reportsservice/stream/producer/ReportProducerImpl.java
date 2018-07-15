package it.unimib.disco.aras.reportsservice.stream.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import it.unimib.disco.aras.reportsservice.stream.ReportsStream;
import it.unimib.disco.aras.reportsservice.stream.message.ReportMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportProducerImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class ReportProducerImpl implements Producer<ReportMessage> {

	/** The reports stream. */
	@Autowired
	private ReportsStream reportsStream;

	/* (non-Javadoc)
	 * @see it.unimib.disco.aras.reportsservice.stream.producer.Producer#dispatch(java.lang.Object)
	 */
	@Override
	public void dispatch(final ReportMessage report) {
		MessageChannel messageChannel = reportsStream.outboundReports();

		messageChannel.send(MessageBuilder.withPayload(report)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
		log.debug("Report message about report " + report.getId() + " dispatched!");
	}

}
