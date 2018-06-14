package com.canary.finance.config;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.canary.finance.intercepter.SessionInterceptorAdapter;

public class CanaryFinanceWebAppConfigurer extends WebMvcConfigurerAdapter {
	private final MessageSource messageSource;
	private final List<String> ignoreUri;
	
	public CanaryFinanceWebAppConfigurer(MessageSource messageSource, List<String> ignoreUri) {
		this.messageSource = messageSource;
		this.ignoreUri = ignoreUri;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionInterceptorAdapter(this.messageSource, this.ignoreUri)).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
