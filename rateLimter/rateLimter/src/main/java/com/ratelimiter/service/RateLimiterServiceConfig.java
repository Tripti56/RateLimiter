package com.ratelimiter.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ratelimiter.model.UserRateLimiterModel;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the service logic for saving, retrieving configuration 
 * in the reate limiter application
 * @author Tripti
 *
 */
@Service
@Slf4j
public class RateLimiterServiceConfig {
	private static final String USER_CONFIG = "userConfig";

	@Autowired
	private RedisTemplate userConfigRedisTemplate;

	public void saveRateLimitConfig(UserRateLimiterModel userLimiterModel) {

		String user = userLimiterModel.getUser();
		String api = userLimiterModel.getApi();
		String key = user+"_"+api;
		UserRateLimiterModel userLimiterModel1 =  (UserRateLimiterModel) userConfigRedisTemplate.opsForHash()
				.get(USER_CONFIG, key);
		if (userLimiterModel1 != null) {
			// Update configuration with new parameters
			userConfigRedisTemplate.opsForHash().put(USER_CONFIG, key, userLimiterModel1);
		} else {
			
			// Create user configuration with new parameters
			userConfigRedisTemplate.opsForHash().put(USER_CONFIG, key, userLimiterModel);
		}

		UserRateLimiterModel userLimiterModel2 =  (UserRateLimiterModel) userConfigRedisTemplate.opsForHash()
				.get(USER_CONFIG, key);
		
		log.info("Configuration done successfully");

	}

	public UserRateLimiterModel getRateLimitConfig(String userWithApi) {
		UserRateLimiterModel userLimiterModel = (UserRateLimiterModel) userConfigRedisTemplate.opsForHash()
				.get(USER_CONFIG, userWithApi);
		return userLimiterModel;

	}

	public Set<UserRateLimiterModel> getRateLimitConfig() {
		UserRateLimiterModel userLimiterModel = null;
		Set<String> userSet = userConfigRedisTemplate.opsForHash().keys(USER_CONFIG);
		HashSet<UserRateLimiterModel> userLimiterModels = new HashSet<>();

		for (String user : userSet) {
			userLimiterModel = (UserRateLimiterModel) userConfigRedisTemplate.opsForHash().get(USER_CONFIG, user);
			userLimiterModels.add(userLimiterModel);
		}
		return userLimiterModels;
	}
	
	public void deleteAll() {
		log.info("Keys removed"+userConfigRedisTemplate.delete(USER_CONFIG));
	}

}
