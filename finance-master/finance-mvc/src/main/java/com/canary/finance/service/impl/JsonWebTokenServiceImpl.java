package com.canary.finance.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.service.JsonWebTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@Service
public class JsonWebTokenServiceImpl implements JsonWebTokenService {
	private static final JwtBuilder BUILDER = Jwts.builder();
	private static final JwtParser PARSER = Jwts.parser();
	@Autowired
	private CanaryFinanceProperties properties;
	
	@Override
	public String getCompact(String identify, String subject, long ttlMillis, Map<String, Object> claims) {
		long currentMillis = System.currentTimeMillis();
		Date now = new Date(currentMillis);
		JwtBuilder builder = BUILDER.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(this.properties.getPrivateKey()))
				.setIssuer(this.properties.getIssuer())
				.setIssuedAt(now)
				.setSubject(subject)
				.setId(identify)
				.setAudience(identify);
		if(ttlMillis > 0) {
			Date expiration = new Date(currentMillis+ttlMillis);
			builder.setExpiration(expiration);
		}
		if(claims != null && claims.size() > 0) {
			builder.setClaims(claims);
		}
		
		return builder.compact();
	}

	@Override
	public Claims parseToken(String jwt) throws Exception {
		return PARSER.setSigningKey(TextCodec.BASE64.encode(this.properties.getPrivateKey())).parseClaimsJws(jwt).getBody();
	}
}
