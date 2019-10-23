package com.ratelimiter.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratelimiter.model.UserRateLimiterModel;
import com.ratelimiter.service.RateLimiterService;
import com.ratelimiter.service.RateLimiterServiceConfig;
/**
 * This class acts as an interface for the user to configure, view and fire requests to
 * rate limiter application
 * @author Tripti
 *
 */
@RestController
@RequestMapping("/api/v1/*")
public class RateLimiterController {

	@Autowired
	RateLimiterServiceConfig rateLimiterServiceConfig;

	@Autowired
	RateLimiterService rateLimiterService;

	@GetMapping("/isRateLimitingAllowed/{user}/{api}")
	String getDevelopers(@PathVariable String user, @PathVariable String api) {
		boolean isAllowed = rateLimiterService.isAllowedToPass(user, api);
		if (isAllowed)
			return "User access allowed";
		else
			return "User usage exceeded allowed limit. Please try again later";
	}

	@GetMapping("/getAllConfigurations")
	Set<UserRateLimiterModel> getAllConfigurations() {
		return rateLimiterServiceConfig.getRateLimitConfig();
	}

	@PostMapping("/configure")
	String configureUserLimit(@RequestBody UserRateLimiterModel userRateLimiterModel) {
		userRateLimiterModel.setTimeBetweenCalls(1);
		userRateLimiterModel.setWindowInSeconds(60);
		rateLimiterServiceConfig.saveRateLimitConfig(userRateLimiterModel);
		return "Configuration saved successfully";
	}

	@PutMapping("/update/{api}/{user}/{rateLimit}")
	String updateDeveloperConfiguration(@RequestBody Integer rateLimit, @PathVariable String api,
			@PathVariable String user) {
		UserRateLimiterModel userRateLimiterModel = new UserRateLimiterModel();
		userRateLimiterModel.setUser(user);
		userRateLimiterModel.setApi(api);
		userRateLimiterModel.setMaxRequestsInWindow(rateLimit);
		rateLimiterServiceConfig.saveRateLimitConfig(userRateLimiterModel);
		return "Configuration updated successfully";
	}
	
	@GetMapping("/deleteAllConfigurations")
	String deleteAllConfigurations() {
		rateLimiterServiceConfig.deleteAll();
		return "All config deleted successfully";
	}

}
