package com.dkolesnik.springplayground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
	exclude = {
		SecurityAutoConfiguration.class,
		ErrorMvcAutoConfiguration.class
	}, scanBasePackages = {
		"com.dkolesnik.springplayground"
	})
@EntityScan(basePackages = {"com.dkolesnik.springplayground.model"})
@EnableAsync
@Slf4j
public class SpringplaygroundApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Throwable {
		/**
		 * Initializing Spring Boot Application
		 */
		final SpringApplication springApplication = new SpringApplication(SpringplaygroundApplication.class);

		/**
		 * Get Current Environment
		 */
		ConfigurableApplicationContext applicationContext = springApplication.run(args);
		final Environment environment = applicationContext.getEnvironment();

		showStartupMessages(environment);
	}

	private static void showStartupMessages(Environment environment) {

	}
	
}