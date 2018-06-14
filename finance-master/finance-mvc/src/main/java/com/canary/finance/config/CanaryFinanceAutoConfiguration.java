package com.canary.finance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.canary.finance.repo.JWTGoogleauthCredentialRepository;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.ICredentialRepository;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

@Configuration
@EnableConfigurationProperties(CanaryFinanceProperties.class)
public class CanaryFinanceAutoConfiguration {
	@Autowired
	private CanaryFinanceProperties properties;
	@Autowired
	private MessageSource messageSource;
	
	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new CanaryFinanceWebAppConfigurer(this.messageSource, this.properties.getSecurityUri());
	}
	
	@Bean
	public SnowflakeDistributedIdRepository snowflakeIdWorker() {
		return new SnowflakeDistributedIdRepository(this.properties.getWorkerId(), this.properties.getDatacenterId());
	}
	
	@Bean
	public ICredentialRepository jwtCredentialRepository(CanaryFinanceProperties properties) {
		return new JWTGoogleauthCredentialRepository(properties);
	}
	
	@Bean
	public IGoogleAuthenticator googleAuthenticator(ICredentialRepository repository) {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		googleAuthenticator.setCredentialRepository(repository);
		return googleAuthenticator;
	}
}