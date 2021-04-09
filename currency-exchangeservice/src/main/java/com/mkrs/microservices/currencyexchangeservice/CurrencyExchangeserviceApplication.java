package com.mkrs.microservices.currencyexchangeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CurrencyExchangeserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeserviceApplication.class, args);
	}

}
