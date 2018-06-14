package com.canary.finance.service;

import java.util.Map;

import io.jsonwebtoken.Claims;

public interface JsonWebTokenService {
	String getCompact(String identify, String subject, long ttlMillis, Map<String, Object> claims);
	Claims parseToken(String jwt) throws Exception;
}
