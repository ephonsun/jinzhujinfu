package com.canary.finance.repo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canary.finance.config.CanaryFinanceProperties;
import com.warrenstrange.googleauth.ICredentialRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;

public class JWTGoogleauthCredentialRepository implements ICredentialRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTGoogleauthCredentialRepository.class);
	private final CanaryFinanceProperties properties;
	
	public JWTGoogleauthCredentialRepository(CanaryFinanceProperties properties) {
		this.properties = properties;
	}

	@Override
	public String getSecretKey(String accessToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(this.properties.getPrivateKey())).parseClaimsJws(accessToken);
			return claims.getBody().getId();			
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
				| SignatureException | IllegalArgumentException e) {
			LOGGER.error("get google authenticator secret key from {} error: {}", accessToken, e.getMessage());
		}
		
		return null;
	}

	@Override
	public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
		// TODO because jwt, so need not to save anything.
		LOGGER.warn("no need to save {}'s google authenticator credentials({}).", userName, secretKey);
	}

}
