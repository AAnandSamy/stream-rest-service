package com.org.aci.stream.service;

import com.org.aci.stream.service.constants.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TransactionApp {
	public static void main(String[] args) {
		SpringApplication.run(TransactionApp.class, args);
	}
}
