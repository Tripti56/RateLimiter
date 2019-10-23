package com.ratelimiter.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserRateLimiterModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user;
	private String api;
	private Integer windowInSeconds;
	private Integer maxRequestsInWindow;
	private Integer timeBetweenCalls;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRateLimiterModel other = (UserRateLimiterModel) obj;
		if (api == null) {
			if (other.api != null)
				return false;
		} else if (!api.equals(other.api))
			return false;
		if (maxRequestsInWindow == null) {
			if (other.maxRequestsInWindow != null)
				return false;
		} else if (!maxRequestsInWindow.equals(other.maxRequestsInWindow))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((api == null) ? 0 : api.hashCode());
		result = prime * result + ((maxRequestsInWindow == null) ? 0 : maxRequestsInWindow.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

}
