package com.canary.finance.config;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.canary.finance.intercepter.SecurityInterceptorAdapter;

public class CanaryFinanceWebAppConfigurer extends WebMvcConfigurerAdapter {
	private final MessageSource messageSource;
	private final List<String> securityUri;
	
	public CanaryFinanceWebAppConfigurer(MessageSource messageSource, List<String> securityUri) {
		this.messageSource = messageSource;
		this.securityUri = securityUri;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityInterceptorAdapter(this.messageSource, this.securityUri)).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
