package org.cloudfoundry.training.routeservice.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RateLimitRouteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitRouteServiceApplication.class, args);
	}
}
