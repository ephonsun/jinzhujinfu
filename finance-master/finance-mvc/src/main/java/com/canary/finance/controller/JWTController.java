package com.canary.finance.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.canary.finance.pojo.ResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;

@RestController
@RequestMapping("/jwt")
public class JWTController extends BaseController {
	
	@PostMapping("/acess/token")
	public ResponseDTO<String> getAccessToken(String clientId, String subject) {
		ResponseDTO<String> response = new ResponseDTO<>();
		if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(subject)) {
			response.setCode(HttpServletResponse.SC_BAD_REQUEST);
			response.setMsg("invalid request, please confirm request parameter.");
			return response;
		}
		
		long currentMillis = System.currentTimeMillis();
		String accessToken = BUILDER.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(this.properties.getPrivateKey()))
			.setIssuer(this.properties.getIssuer())
			.setIssuedAt(new Date(currentMillis))
			.setSubject(subject)
			.setId(clientId)
			.setAudience(clientId)
			.setExpiration(new Date(currentMillis+this.properties.getExpireInMillis()))
			.compact();
		response.setCode(HttpServletResponse.SC_OK);
		response.setData(accessToken);
		return response;
	}
	
	@PostMapping("/authenticate")
	public ResponseDTO<String> authenticate(String clientId, String accessToken) {
		ResponseDTO<String> response = new ResponseDTO<>();
		if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(accessToken)) {
			response.setCode(HttpServletResponse.SC_BAD_REQUEST);
			response.setMsg("invalid request, please confirm request parameter.");
			return response;
		}
		
		try {
			Jws<Claims> claims = PARSER.setSigningKey(TextCodec.BASE64.encode(this.properties.getPrivateKey())).parseClaimsJws(accessToken);
			response.setCode(HttpServletResponse.SC_OK);
			response.setData(claims.getBody().getId());
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
				| SignatureException | IllegalArgumentException e) {
			response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
			response.setMsg(e.getMessage());
		}
		return response;
	}
}
