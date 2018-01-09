package com.radio.nuclies.doseRates.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.radio.nuclies.doseRates.controller",
										"com.radio.nuclies.doseRates.service",
										"com.radio.nuclies.doseRates.handler"})
public class DoseRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoseRatesApplication.class, args);
	}
}
