package com.elite.resort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication

public class ResortApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResortApplication.class, args);
	}

}
