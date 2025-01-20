package com.capstone.qwikpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QwikpayApplication {

	public static void main(String[] args) {
		System.setProperty("log4j2.configurationFile", "classpath:log4j2.xml");
		System.setProperty("logging.file.name", "logs/qwikpay.log");
		SpringApplication.run(QwikpayApplication.class, args);
	}

}
