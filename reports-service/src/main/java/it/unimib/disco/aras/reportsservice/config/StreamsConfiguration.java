package it.unimib.disco.aras.reportsservice.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import it.unimib.disco.aras.reportsservice.stream.AnalysisResultsStream;
import it.unimib.disco.aras.reportsservice.stream.ReportsStream;

@EnableBinding(value= {AnalysisResultsStream.class, ReportsStream.class})
public class StreamsConfiguration {
}
