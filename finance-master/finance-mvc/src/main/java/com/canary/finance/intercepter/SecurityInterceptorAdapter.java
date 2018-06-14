package com.canary.finance.intercepter;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptorAdapter extends HandlerInterceptorAdapter {
	private final MessageSource messageSource;
	private final List<String> securityUri;
	
	public SecurityInterceptorAdapter(MessageSource messageSource, List<String> securityUri) {
		this.messageSource = messageSource;
		this.securityUri = securityUri;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		if(!requestURI.endsWith(".jsp")) {
			boolean match = securityUri.stream().anyMatch(s->{
				return  request.getRequestURI().startsWith(s);
			});
			if(match) {
				String loginCookieName = this.messageSource.getMessage("cookie.login", null, Locale.getDefault());
				Cookie loginCookie = this.getLoginCookie(request, loginCookieName);
				if(loginCookie == null) {
					response.sendRedirect("/login");
					return false;
				}
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	@Override  
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception { 
		//TODO do nothing.
	}
	
	private Cookie getLoginCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(StringUtils.equalsIgnoreCase(cookie.getName(), cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}
}