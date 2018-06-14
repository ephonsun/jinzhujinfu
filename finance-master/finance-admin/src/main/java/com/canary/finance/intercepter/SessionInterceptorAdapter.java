package com.canary.finance.intercepter;

import static com.canary.finance.util.ConstantUtil.SLASH;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.canary.finance.domain.Admin;
import com.canary.finance.pojo.SessionVO;

public class SessionInterceptorAdapter extends HandlerInterceptorAdapter {
	private final MessageSource messageSource;
	private final List<String> ignoreUri;
	
	public SessionInterceptorAdapter(MessageSource messageSource, List<String> ignoreUri) {
		this.messageSource = messageSource;
		this.ignoreUri = ignoreUri;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean match = ignoreUri.stream().anyMatch(s->{
			String requestURI = request.getRequestURI();
			return  requestURI.equals(SLASH) || requestURI.startsWith(s);
		});
		if(!match) {
			Object security = request.getSession().getAttribute("session_canary_key");
			if(security == null) {
				request.setAttribute("message", messageSource.getMessage("no.session", null, Locale.getDefault()));
				request.getRequestDispatcher(SLASH).forward(request, response);
				return false;
			}
			SessionVO session = (SessionVO)security;
			Admin admin = session.getAdmin();
			if(admin == null) {
				request.setAttribute("message", messageSource.getMessage("no.session", null, Locale.getDefault()));
				request.getRequestDispatcher(SLASH).forward(request, response);
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	@Override  
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception { 
		//TODO do nothing.
	}

}