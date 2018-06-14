package com.canary.finance.service.impl;

import static com.canary.finance.util.ConstantUtil.CODE;
import static com.canary.finance.util.ConstantUtil.COMMA;
import static com.canary.finance.util.ConstantUtil.DATA;
import static com.canary.finance.util.ConstantUtil.MSG;
import static com.canary.finance.util.ConstantUtil.SMSCODE;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.enumeration.ResponseEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.service.SmsService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@Service
public class SmsServiceImpl implements SmsService {
	private static final JwtBuilder BUILDER = Jwts.builder();
	@Autowired
	protected CanaryFinanceProperties properties;
	
	@Override
	public JSONObject send(String cellphone, String type, SmsPatternEnum pattern) {
		JSONObject result = new JSONObject();
		String code = this.getSmsCode(999999);
		String text = String.format(pattern.getPattern(), code);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String signature = properties.getHuaxingSignature();
		nvps.add(new BasicNameValuePair("content", signature + text));
		nvps.add(new BasicNameValuePair("mobile", cellphone));
		String response = execute(properties.getRemotingUrl()+properties.getHuaxingSmsUri(), nvps);
		
		if(StringUtils.isNotBlank(response)) { 
			JSONObject json = JSON.parseObject(response);
			if(StringUtils.equals(json.getString("returnstatus"), "Success")) {
				long currentMillis = System.currentTimeMillis();
				long expireInMillis = currentMillis+1200_000;
				String clientId = type + cellphone;
				String accessToken = BUILDER.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(this.properties.getPrivateKey()))
					.setIssuer(this.properties.getIssuer())
					.setIssuedAt(new Date(currentMillis))
					.setSubject(SMSCODE)
					.setId(code)
					.setAudience(clientId)
					.setExpiration(new Date(expireInMillis))
					.compact();
				JSONObject  data = new JSONObject();
				data.put("token", accessToken);
				result.put(DATA, data);
				result.put(CODE, ResponseEnum.SUCCESS.getCode());
				result.put(MSG, ResponseEnum.SUCCESS.getMsg());
				return result;
			}
        } 
		List<NameValuePair> flowNvps = new ArrayList<NameValuePair>();
		try {
			flowNvps.add(new BasicNameValuePair("msg", URLEncoder.encode(text, UTF_8)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		flowNvps.add(new BasicNameValuePair("mobile", cellphone));
		String flowResponse = execute(properties.getRemotingUrl()+properties.getFlowSmsUrl(), nvps);
		if (StringUtils.isNotBlank(flowResponse)) {
			String[] infos = StringUtils.split(flowResponse, COMMA);
			if (infos != null && infos.length == 2 && StringUtils.startsWith(infos[1], "0")) {
				long currentMillis = System.currentTimeMillis();
				long expireInMillis = currentMillis+1200_000;
				String clientId = type + cellphone;
				String accessToken = BUILDER.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(this.properties.getPrivateKey()))
					.setIssuer(this.properties.getIssuer())
					.setIssuedAt(new Date(currentMillis))
					.setSubject(SMSCODE)
					.setId(code)
					.setAudience(clientId)
					.setExpiration(new Date(expireInMillis))
					.compact();
				JSONObject  data = new JSONObject();
				data.put("token", accessToken);
				result.put(DATA, data);
				result.put(CODE, ResponseEnum.SUCCESS.getCode());
				result.put(MSG, ResponseEnum.SUCCESS.getMsg());
				return result;
			}
		}
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;
	}
	
	private String execute(String url, List<NameValuePair> nvps) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			if(nvps != null && nvps.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
			}
			CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				return EntityUtils.toString(httpResponse.getEntity(), UTF_8);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return null;
	}
	
	private String getSmsCode(int seed) {
		return StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(seed)), String.valueOf(seed).length(), String.valueOf(RandomUtils.nextInt(9)));
	}
}
