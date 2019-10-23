package com.workSample.rateLimter;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RateLimterApplicationTests {

	//@Test
	public static void main(String[] args) {

	 SpringApplication.run(RateLimterApplicationTests.class, args);
	
 }}
