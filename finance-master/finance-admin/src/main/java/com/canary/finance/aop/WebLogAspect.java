package com.canary.finance.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);
	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private volatile boolean response = false;
	
    @Pointcut("execution(public * com.canary.finance.controller..*.*(..))")
    public void webLog() {
    	
    }
    
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    	startTime.set(System.currentTimeMillis());
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        String requestURI = request.getRequestURI();
        if(!requestURI.endsWith(".jsp") && ! classMethod.endsWith(".initBinder")) {
        	response = true;
        	LOGGER.info("Request URL: {}, Http Method: {}, IP: {}, Class Method: {}, Arguments: {}.", request.getRequestURL(), request.getMethod(), request.getRemoteAddr(), classMethod, Arrays.toString(joinPoint.getArgs()));
        }
    }
    
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
    	if(response) {
    		response = false;
    		LOGGER.info("Response: {}, Spend In {} Millisecond", ret, (System.currentTimeMillis()-startTime.get()));
    	}
    }
}
