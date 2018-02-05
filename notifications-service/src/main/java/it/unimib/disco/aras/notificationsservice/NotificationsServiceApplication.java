package it.unimib.disco.aras.notificationsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
@EnableDiscoveryClient
public class NotificationsServiceApplication {

	private static Logger logger = LoggerFactory.getLogger(NotificationsServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NotificationsServiceApplication.class, args);
	}

	@StreamListener(Sink.INPUT)
	public void loggerSink(SinkTimeInfo sinkTimeInfo) {
		logger.info("Received: " + sinkTimeInfo.toString());
	}

	public static class SinkTimeInfo{

		private String time;
		private String label;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public void setSinkLabel(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		@Override
		public String toString() {
			return "SinkTimeInfo [time=" + time + ", label=" + label + "]";
		}

	}
}
