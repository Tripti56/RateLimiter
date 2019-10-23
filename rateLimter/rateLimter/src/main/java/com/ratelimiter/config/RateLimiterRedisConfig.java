package com.ratelimiter.config;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.ratelimiter.model.UserRateLimiterModel;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * This class implements the redis configuration including redis data structure for rate limiter service
 * @author Tripti
 *
 */

@Slf4j
@Configuration
public class RateLimiterRedisConfig {

	@Bean(name = "redisConnection")
	RedisConnectionFactory connectionFactory() {
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnReturn(true);
		jedisPoolConfig.setMaxTotal(5000);
		jedisPoolConfig.setMaxIdle(0);
		RedisStandaloneConfiguration redisConfig = new  RedisStandaloneConfiguration("localhost",6379);
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().poolConfig(jedisPoolConfig).build();
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisConfig, jedisClientConfiguration);
		return jedisConFactory;

	}
	
	@Bean
	RedisTemplate<String, UserRateLimiterModel> userConfigRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, UserRateLimiterModel> userRedisTemplate = new RedisTemplate<>();
		userRedisTemplate.setConnectionFactory(connectionFactory);
		userRedisTemplate.setEnableTransactionSupport(true);
		return userRedisTemplate;
	}

	@Bean
	RedisTemplate<String, Long> rateLimitRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> longRedisTemplate = new RedisTemplate<>();
		longRedisTemplate.setConnectionFactory(connectionFactory);
		longRedisTemplate.setEnableTransactionSupport(true);
		return longRedisTemplate;
	}

}
