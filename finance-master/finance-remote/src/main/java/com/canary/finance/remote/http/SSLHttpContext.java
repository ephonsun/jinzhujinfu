package com.canary.finance.remote.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLHttpContext {
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	    SSLContext sc = SSLContext.getInstance("SSL");  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(X509Certificate[] certificate, String param) throws CertificateException {
	        	
	        }  
	  
	        @Override  
	        public void checkServerTrusted(X509Certificate[] certificate, String param) throws CertificateException {
	        	
	        }  
	  
	        @Override  
	        public X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	    sc.init(null, new TrustManager[] {trustManager}, null);  
	    return sc;  
	}  
}
