package hw6.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationApplication.class, args);
	}

}
