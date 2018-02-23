package it.unimib.disco.aras.projectsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProjectsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsServiceApplication.class, args);
	}
}
