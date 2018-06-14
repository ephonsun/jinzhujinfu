package com.canary.finance.remote.http;

import static com.canary.finance.remote.util.ConstantUtil.EMPTY;
import static com.canary.finance.remote.util.ConstantUtil.LOGGER;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import io.netty.util.CharsetUtil;

public class HttpTask implements Callable<String>{
	private final String url;
	private final List<NameValuePair> parameters;
	private final String body;
	
	public HttpTask(String url, List<NameValuePair> parameters) {
		this.url = url;
		this.parameters = parameters;
		this.body = null;
	}
	
	public HttpTask(String url, String body) {
		this.url = url;
		this.body = body;
		this.parameters = null;
	}
	
	@Override
	public String call() throws Exception {
		HttpPost post = new HttpPost(this.url);
		if(this.parameters != null) {
			post.setEntity(new UrlEncodedFormEntity(this.parameters, CharsetUtil.UTF_8));
		} else if(this.body != null) {
			post.setEntity(new StringEntity(this.body, CharsetUtil.UTF_8));
		}
		try {
			return this.invoke(post);
		} finally {
			post.releaseConnection();
		}
	}
	
	private String invoke(HttpUriRequest request) {
		if(request != null && request.getURI() != null) {
			try(final CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
				try(final CloseableHttpResponse response = closeableHttpClient.execute(request)) {
					return EntityUtils.toString(response.getEntity(), CharsetUtil.UTF_8);
				}
			} catch (IOException e) {
				LOGGER.error("invoke request[{}] has occurred error, caused by: {}", this.url, e.getMessage());
			}
		}
		
		return EMPTY;
	}
}