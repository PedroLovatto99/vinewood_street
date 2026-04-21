package dev.project.VinewoodStreet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class VinewoodStreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(VinewoodStreetApplication.class, args);
	}

}
