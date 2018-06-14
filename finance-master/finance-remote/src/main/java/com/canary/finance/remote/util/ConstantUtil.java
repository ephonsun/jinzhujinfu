package com.canary.finance.remote.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ConstantUtil {
	public static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(ConstantUtil.class);
	public static final String AND = "&";
	public static final String EQUAL = "=";
	public static final String VERTICAL_BAR = "|";
	public static final String COLON = ":";
	public static final String SEMICOLON = ";";
	public static final String SLASH = "/";
	public static final String UTF8 = "UTF-8";
	public static final String DOT = ".";
	public static final String QUESTION_MARK = "?";
	public static final String EMPTY = "";
	public static final String COMMA = ",";
	public static final String NEWLINE = ",\r\n";
	public static final String CODE = "code";
	public static final String MSG = "msg";
	public static final String RESULT = "result";
	public static final int SO_BACKLOG = 1024;
	public static final int DEFAULT_THREAD_SIZE = 10;
	public static final int MAX_FRAME_LENGTH = 1048576;
	public static final String NUMBER_DATE_FORMAT = "yyyyMMdd";
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";
	
	public static final String HUAXING_SMS_SEND = "/huaxing/sms/send";
	public static final String OPEN_ACCOUNT = "/open/account";
	public static final String ACCOUNT_BALANCE = "/account/balance";
	public static final String PRE_AUTH = "/pre/auth";
	public static final String TRANSFER_ACCOUNT = "/transfer/account";
	public static final String ALLOCATE_FUNDS = "/allocate/funds";
	public static final String FLOW_SMS = "/flow/sms";
	public static final String HUAXING_USER_KEY = "account";
	public static final String HUAXING_PASSWORD_KEY = "password"; 
	public static final String HUAXING_ACTION_KEY = "action"; 
	public static final String HUAXING_EXTNO_KEY = "extno"; 
	public static final String HUAXING_SENDTIME_KEY = "sendTime"; 
	public static final String HUAXING_MERCHANT_KEY = "userid"; 
	
	public static final String EMAY_SMS_SEND="/emay/sms/send";
	public static final String PUSH_URL="/push";
	public static final String EMAY_CD_KEY="cdkey";
	public static final String EMAY_PASSWORD_KEY="password";
	public static final String EMAY_SERIAL_KEY="addserial";
	public static final String EMAY_SMS_PRIORITY_KEY="smspriority";
	
	public static List<NameValuePair> getNameValuePairList(final String content) {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		if(content != null && content.indexOf(EQUAL) != -1) {
			List<String> listNVP = Arrays.asList(content.split(AND));
			listNVP.forEach(nvp -> {
				if(nvp != null && nvp.indexOf(EQUAL) != -1) {
					List<String> parameter = Arrays.asList(nvp.split(EQUAL));
					if(parameter.size() == 2) {
						nvps.add(new BasicNameValuePair(parameter.get(0), parameter.get(1)));
					} else {
						nvps.add(new BasicNameValuePair(parameter.get(0), EMPTY));
					}
				}
			});
		}
		return nvps;
	}
	
	public static List<NameValuePair> getHttpParameters(final String content) {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		if(content != null) {
			String[] nameValuePairs = content.split(AND);
			if(nameValuePairs != null && nameValuePairs.length > 0) {
				for(String nameValuePair : nameValuePairs) {
					String[] parameter = nameValuePair.split(EQUAL);
					if(parameter != null && parameter.length == 2) {
						nvps.add(new BasicNameValuePair(parameter[0], parameter[1]));
					}
				}
			}
		}
		return nvps;
	}
	
	public static List<NameValuePair> getNameValuePairList(final Map<String, Object> map) throws UnsupportedEncodingException {
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		if(map != null && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				nvps.add(new BasicNameValuePair(key, URLEncoder.encode(map.get(key).toString(), CharsetUtil.UTF_8.name())));
			}
		}
		return nvps;
	}
	
	public static Optional<NameValuePair> getNameValuePair(List<NameValuePair> nvps, String parameter) {
		Optional<List<NameValuePair>> optional = Optional.ofNullable(nvps);
		if(optional.isPresent()) {
			return optional.get().stream().filter(nvp->nvp.getName().equals(parameter)).findFirst();
		}
		return Optional.empty();
	}
	
	public static Optional<String> getPrameter(List<NameValuePair> nvps, String name) {
		Optional<List<NameValuePair>> optional = Optional.ofNullable(nvps);
		if(optional.isPresent()) {
			Optional<NameValuePair> nvp = optional.get().stream().filter(p->p.getName().equals(name)).findFirst();
			if(nvp.isPresent()) {
				return Optional.of(nvp.get().getValue());
			}
		}
		return Optional.empty();
	}
}