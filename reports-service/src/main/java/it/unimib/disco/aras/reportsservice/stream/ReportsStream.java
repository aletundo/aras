package it.unimib.disco.aras.reportsservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * The Interface ReportsStream.
 */
public interface ReportsStream {
    
    /** The output. */
    final String OUTPUT = "reports-out";

    /**
	 * Outbound reports.
	 *
	 * @return the message channel
	 */
    @Output(OUTPUT)
    MessageChannel outboundReports();
}
