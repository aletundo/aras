package it.unimib.disco.aras.testsuite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;

@SpringBootApplication(exclude = TestSupportBinderAutoConfiguration.class)
public class TestsuiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestsuiteApplication.class, args);
	}
}
