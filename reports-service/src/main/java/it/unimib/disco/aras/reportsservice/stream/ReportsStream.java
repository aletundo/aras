package it.unimib.disco.aras.reportsservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ReportsStream {
    final String OUTPUT = "reports-out";

    @Output(OUTPUT)
    MessageChannel outboundReports();
}
