package com.canary.finance.remote.http;

import static com.canary.finance.remote.util.ConstantUtil.*;
import static com.canary.finance.remote.util.ConstantUtil.LOGGER;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.net.ssl.SSLContext;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import io.netty.util.CharsetUtil;

public class HttpsTask implements Callable<String>{
	private final String url;
	private final List<NameValuePair> parameters;
	private final JSONObject body;
	static {
		try {
			SSLContext sslcontext = SSLHttpContext.createIgnoreVerifySSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
			         .register("https", new SSLConnectionSocketFactory(sslcontext))
			         .build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			HttpClients.custom().setConnectionManager(connManager);
		} catch (Exception e) {
			LOGGER.error("init ssl context has occurred error, caused by: {}", e.getMessage());
		}
	}
	
	public HttpsTask(String url, List<NameValuePair> parameters) {
		this.url = url;
		this.parameters = parameters;
		this.body = null;
	}
	
	public HttpsTask(String url, boolean isPost, JSONObject body) {
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
			post.setEntity(new StringEntity(this.body.toString(), CharsetUtil.UTF_8));
		}
//		post.setHeader("Accept-Charset", UTF8);
//		post.setHeader("Content-type", CONTENT_TYPE);
//		post.setHeader("User-Agent", "Canary.Finance");
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