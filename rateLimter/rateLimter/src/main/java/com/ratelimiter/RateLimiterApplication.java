package com.ratelimiter;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
/**
 * This class marks the entry point of rate limiter application
 * @author Tripti
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
public class RateLimiterApplication {
	
	@Autowired
	RedisProperties redisProperties;
	
	

	public static void main(String[] args) {
		SpringApplication.run(RateLimiterApplication.class, args);
	}
	
	@PostConstruct
	  void init() {
		Duration d = Duration.ofHours(1);
				redisProperties.setTimeout(d);
	    System.out.println("value");
	  }

}
