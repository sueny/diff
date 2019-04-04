package com.ssm.diff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DiffApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiffApplication.class, args);
	}

}
