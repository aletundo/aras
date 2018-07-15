package it.unimib.disco.aras.notificationsservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.notificationsservice.stream.AnalysesStream;
import it.unimib.disco.aras.notificationsservice.stream.NotificationsStream;
import it.unimib.disco.aras.notificationsservice.stream.ReportsStream;

/**
 * The Class StreamsConfiguration.
 */
@EnableBinding(value={AnalysesStream.class, ReportsStream.class, NotificationsStream.class})
public class StreamsConfiguration {}
