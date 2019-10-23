package com.ratelimiter.service;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ratelimiter.model.UserRateLimiterModel;

import lombok.extern.slf4j.Slf4j;
/**
 * This class implements the sliding window algorithm for rate limiting 
 * for user and api.
 * @author Tripti
 *
 */
@Service
@Slf4j
public class RateLimiterService {

	public static final String REDIS_CONFIG_KEY = "userConfig";
	@Autowired
	RedisTemplate<String, UserRateLimiterModel> userConfigRedisTemplate;

	@Autowired
	private RedisTemplate<String, Long> rateLimitRedisTemplate;

	BoundHashOperations<String, String, UserRateLimiterModel> userHashOps;

	BoundZSetOperations<String, Long> requestZSetOps;

	public boolean isAllowedToPass(String user, String api) {

		boolean isAllowed = false;

		if (user == null) {
			log.info("User passed to Rate Limiter is Null");
			return false;
		}

		try {

			userHashOps = userConfigRedisTemplate.boundHashOps(REDIS_CONFIG_KEY);
			String key = user+"_"+api;
			UserRateLimiterModel config = (UserRateLimiterModel) userHashOps.get(key);
			if (config == null) {
				log.info("Adding new user Configuration in Rate Limiter for User");
				config = new UserRateLimiterModel();
				config.setUser(user);
				config.setApi(api);
				config.setWindowInSeconds(60);
				config.setMaxRequestsInWindow(10);
				config.setTimeBetweenCalls(1);

				userHashOps.put(key, config);
			} else {
				// do something for fetching user/api
			}

			requestZSetOps = rateLimitRedisTemplate.boundZSetOps(key);
			if (requestZSetOps == null)

				return false;

			long timeWindowInMilli = TimeUnit.MILLISECONDS.convert(config.getWindowInSeconds(), TimeUnit.SECONDS);
			long currentTime = Instant.now().toEpochMilli();
			long currentWindowStartTime = currentTime - timeWindowInMilli;

			requestZSetOps.removeRangeByScore(0.0, (double) currentWindowStartTime);

			int requestSize = requestZSetOps.rangeByScore(currentWindowStartTime, currentTime).size();

			if ((requestSize + 1) <= config.getMaxRequestsInWindow()) {
				isAllowed = true;
				requestZSetOps.add(currentTime, currentTime);
			}
		} catch (Exception e) {
			log.info("USer Rate Limit Configuration not maintained - Request Allowed");
			log.info("Exception : ", e);
			isAllowed = true;
		}

		return isAllowed;
	}

}
