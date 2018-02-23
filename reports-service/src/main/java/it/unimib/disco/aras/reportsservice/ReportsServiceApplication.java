package it.unimib.disco.aras.reportsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReportsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportsServiceApplication.class, args);
	}
}
