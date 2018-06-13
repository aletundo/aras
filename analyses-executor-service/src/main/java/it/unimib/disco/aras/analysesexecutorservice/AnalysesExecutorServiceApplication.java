package it.unimib.disco.aras.analysesexecutorservice;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;


@SpringBootApplication
public class AnalysesExecutorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalysesExecutorServiceApplication.class, args);
    }

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource<TimeInfo> timerMessageSource() {
        return () -> MessageBuilder.withPayload(new TimeInfo(new Date().getTime() + "", "Executor")).build();
    }

    public static class TimeInfo {

        private String time;
        private String label;

        public TimeInfo(String time, String label) {
            super();
            this.time = time;
            this.label = label;
        }

        public String getTime() {
            return time;
        }

        public String getLabel() {
            return label;
        }

    }
}
