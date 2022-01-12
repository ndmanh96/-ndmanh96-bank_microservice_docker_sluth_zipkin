package com.manhcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class BankConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankConfigServerApplication.class, args);
	}

}
