package com.canary.finance.remote.netty;

import static com.canary.finance.remote.util.ConstantUtil.ACCOUNT_BALANCE;
import static com.canary.finance.remote.util.ConstantUtil.FLOW_SMS;
import static com.canary.finance.remote.util.ConstantUtil.AND;
import static com.canary.finance.remote.util.ConstantUtil.CONTENT_TYPE;
import static com.canary.finance.remote.util.ConstantUtil.EMAY_SMS_SEND;
import static com.canary.finance.remote.util.ConstantUtil.ALLOCATE_FUNDS;
import static com.canary.finance.remote.util.ConstantUtil.EQUAL;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_ACTION_KEY;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_EXTNO_KEY;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_MERCHANT_KEY;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_PASSWORD_KEY;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_SENDTIME_KEY;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_SMS_SEND;
import static com.canary.finance.remote.util.ConstantUtil.HUAXING_USER_KEY;
import static com.canary.finance.remote.util.ConstantUtil.LOGGER;
import static com.canary.finance.remote.util.ConstantUtil.NUMBER_DATE_FORMAT;
import static com.canary.finance.remote.util.ConstantUtil.OPEN_ACCOUNT;
import static com.canary.finance.remote.util.ConstantUtil.PRE_AUTH;
import static com.canary.finance.remote.util.ConstantUtil.PUSH_URL;
import static com.canary.finance.remote.util.ConstantUtil.SLASH;
import static com.canary.finance.remote.util.ConstantUtil.TRANSFER_ACCOUNT;
import static com.canary.finance.remote.util.ConstantUtil.VERTICAL_BAR;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.canary.finance.config.RemoteProperties;
import com.canary.finance.remote.http.HttpFutureListener;
import com.canary.finance.remote.http.HttpTask;
import com.canary.finance.remote.http.JPushServiceTask;
import com.canary.finance.remote.util.ConstantUtil;
import com.canary.finance.util.SecurityUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;

public class RemoteHandler extends ChannelInboundHandlerAdapter {
	private static final List<String> WHITE_LIST = Arrays.asList(
		"127.0.0.1", 		//localhost
		"192.168.", 		//192.168.*.*
		"172.16.", 			//172.16.*.*
		"10."				//10.*.*.*
	);
	private static final List<String> BUSINESS_URI = Arrays.asList(
		HUAXING_SMS_SEND,
		EMAY_SMS_SEND,
		OPEN_ACCOUNT,
		PRE_AUTH,
		FLOW_SMS,
		TRANSFER_ACCOUNT,
		ALLOCATE_FUNDS,
		ACCOUNT_BALANCE,
		PUSH_URL,
		SLASH
	);
	private final RemoteProperties properties;
	
	public RemoteHandler(RemoteProperties properties) {
		this.properties = properties;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(msg instanceof FullHttpMessage) {
			final FullHttpMessage message = (FullHttpMessage)msg;
			try {
				InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		    	String ip = address.getAddress().getHostAddress();
		    	Optional<String> optional = WHITE_LIST.stream().filter((s)-> ip.startsWith(s)).findFirst();
	    		if(optional.isPresent()) {
	    			this.handleHttpRequest(ctx, message);
	    		} else {
	    			this.echoHttpResponse(ctx, message, String.format(this.properties.getNotAcceptable(), ip));
	    		}
    		} catch (Exception e) {
    			this.echoHttpResponse(ctx, message, String.format(this.properties.getServerError(), e.getMessage()));
			} finally {
				ReferenceCountUtil.release(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("exception occurred in channel["+ctx.name()+"] cause by: " + cause.getMessage());
		ctx.close();
	}
	
	private void handleHttpRequest(final ChannelHandlerContext ctx, final FullHttpMessage message) throws Exception {
		if(message instanceof FullHttpRequest) {
			FullHttpRequest request = (FullHttpRequest) message;
			String uri = request.getUri();
			Optional<String> optional = BUSINESS_URI.stream().filter((s)-> s.equals(uri)).findFirst();
			if(optional.isPresent()) {
				String content = request.content().toString(CharsetUtil.UTF_8);
				if(content != null) {
					try {
						content = URLDecoder.decode(content, CharsetUtil.UTF_8.name());
					} catch (UnsupportedEncodingException e) {
						this.echoHttpResponse(ctx, message, String.format(this.properties.getServerError(), e.getMessage()));
					}
				}
				
				HttpMethod method = request.getMethod();
				if(HttpMethod.POST.equals(method)) {
					switch(uri) {
						case HUAXING_SMS_SEND:
							this.sendSmsHuaxing(ctx, message, content);
							break;
						case FLOW_SMS:
							this.sendSmsFlow(ctx, message, content);
							break;
						case OPEN_ACCOUNT:
							this.openAccount(ctx, message, content);
							break;
						case PRE_AUTH:
							this.preAuth(ctx, message, content);
							break;
						case TRANSFER_ACCOUNT:
							this.transferAccount(ctx, message, content);
							break;
						case ALLOCATE_FUNDS:
							this.allocateFunds(ctx, message, content);
							break;
						case ACCOUNT_BALANCE:
							this.getAccountBlance(ctx, message, content);
							break;
						case PUSH_URL:
							this.jPushMessage(ctx, message, content);
							break;
						case SLASH:
						default:
							this.echoHttpResponse(ctx, message, this.properties.getDefaultMessage());
							break;
					}
				} else {
					this.echoHttpResponse(ctx, message, String.format(this.properties.getNotAcceptable(), method.name()));
				}
			} else {
				this.echoHttpResponse(ctx, message, String.format(this.properties.getNotAcceptable(), uri));
			}
		} else if(message instanceof HttpContent) {
			HttpContent content = (HttpContent) message;
			ByteBuf buf = content.content();
			buf.release();
		} else {
			//do nothing.
		}
	}
	
	private void jPushMessage(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		if(content != null) {
			String apikey = properties.getJpushApikey();
			String secretkey = properties.getJpushSecretkey();
			String[] nameValuePairs = content.split(AND);
			if(nameValuePairs != null && nameValuePairs.length >= 2) {
				Map<String, String> map = new HashMap<String, String>();
				for(String nameValuePair : nameValuePairs) {
					String[] parameter = nameValuePair.split(EQUAL);
					if(parameter != null && parameter.length == 2) {  
						map.put(parameter[0], parameter[1]);
					}
				}
				JPushServiceTask task = new JPushServiceTask(apikey, secretkey, map);
				Future<String> future = ctx.executor().submit(task);
				future.addListener(new HttpFutureListener(ctx, "", properties.getDefaultMessage()));
			} else {
				this.echoHttpResponse(ctx, message, String.format(properties.getNotAcceptable(), content));
			}
		} else {
			this.echoHttpResponse(ctx, message, String.format(properties.getNotAcceptable(), content));
		}
	}

	
	private void sendSmsHuaxing(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String smsSendURL = properties.getSmsHuaxingUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() >= 2) {
			nvps.add(new BasicNameValuePair(HUAXING_MERCHANT_KEY, ""));
			nvps.add(new BasicNameValuePair(HUAXING_USER_KEY, this.properties.getSmsHuaxingUser()));
			nvps.add(new BasicNameValuePair(HUAXING_PASSWORD_KEY, DigestUtils.md5Hex(properties.getSmsHuaxingPassword()).toUpperCase()));
			nvps.add(new BasicNameValuePair(HUAXING_ACTION_KEY, this.properties.getSmsHuaxingAction()));
			nvps.add(new BasicNameValuePair(HUAXING_EXTNO_KEY, ""));
			nvps.add(new BasicNameValuePair(HUAXING_SENDTIME_KEY, ""));
			this.invokeHttp(ctx, smsSendURL, nvps);
		} else {
			this.echoHttpResponse(ctx, message, content);
		}
	}

	private void sendSmsFlow(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String smsSendURL = properties.getFlowUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() >= 2) {
			nvps.add(new BasicNameValuePair("account", this.properties.getFlowAccount()));
			nvps.add(new BasicNameValuePair("pswd", this.properties.getFlowPassword()));
			nvps.add(new BasicNameValuePair("needstatus", "true"));
			this.invokeHttp(ctx, smsSendURL, nvps);
		} else {
			this.echoHttpResponse(ctx, message, content);
		}
	}
	
	private void openAccount(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String openAccountUrl = properties.getOpenAccountUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() == 8) {
			String bankName = "";
			String capAcntNm = "";
			String capAcntNo = getHttpParameter(nvps, "capAcntNo");
			String idcard = getHttpParameter(nvps, "certif_id");
			String cityId = getHttpParameter(nvps, "city_id");
			String name = getHttpParameter(nvps, "cust_nm");
			String email = "";
			String lpassword = "";
			String merchantNO = properties.getMerchantCode();
			String orderNO = getHttpParameter(nvps, "mchnt_txn_ssn");
			String cellphone = getHttpParameter(nvps, "mobile_no");
			String bankNO = getHttpParameter(nvps, "parent_bank_id");
			String password = getHttpParameter(nvps, "password");
 			String rem = "";
			String version = properties.getFuiouVersion();
			String signatureStr=SecurityUtils.sign(bankName+VERTICAL_BAR+capAcntNm+VERTICAL_BAR+capAcntNo+VERTICAL_BAR+idcard+VERTICAL_BAR+cityId
					+VERTICAL_BAR+name+VERTICAL_BAR+email+VERTICAL_BAR+lpassword+VERTICAL_BAR+merchantNO+VERTICAL_BAR+orderNO+VERTICAL_BAR+cellphone
					+VERTICAL_BAR+bankNO+VERTICAL_BAR+password+VERTICAL_BAR+rem+VERTICAL_BAR+version);
			nvps.add(new BasicNameValuePair("bank_nm", bankName));
			nvps.add(new BasicNameValuePair("capAcntNm", capAcntNm));
			nvps.add(new BasicNameValuePair("email", email));
			nvps.add(new BasicNameValuePair("lpassword", lpassword));
			nvps.add(new BasicNameValuePair("mchnt_cd", merchantNO));
			nvps.add(new BasicNameValuePair("password", password));
			nvps.add(new BasicNameValuePair("rem", rem));
			nvps.add(new BasicNameValuePair("ver", version));
			nvps.add(new BasicNameValuePair("signature", signatureStr));
			this.invokeHttp(ctx, openAccountUrl, nvps);
		} else {
			this.echoHttpResponse(ctx, message, String.format(properties.getNotAcceptable(), content));
		}
	}
	
	private void getAccountBlance(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String accountBalanceUrl = properties.getAccountBalanceUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() == 2) {
			String customerNO = getHttpParameter(nvps, "cust_no");
			String orderNO = getHttpParameter(nvps, "mchnt_txn_ssn");
			String merchantNO = properties.getMerchantCode();
			SimpleDateFormat format = new SimpleDateFormat(NUMBER_DATE_FORMAT);
			String systemDate = format.format(Calendar.getInstance().getTime());
			String signatureStr=SecurityUtils.sign(customerNO+VERTICAL_BAR+merchantNO+VERTICAL_BAR+systemDate+VERTICAL_BAR+orderNO);
			nvps.add(new BasicNameValuePair("mchnt_txn_dt", systemDate));
			nvps.add(new BasicNameValuePair("mchnt_cd", merchantNO));
			nvps.add(new BasicNameValuePair("signature", signatureStr));
			this.invokeHttp(ctx, accountBalanceUrl, nvps);
		} else {
			this.echoHttpResponse(ctx, message, String.format(properties.getNotAcceptable(), content));
		}
	}
	
	private void transferAccount(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String transferAccouontUrl = properties.getTransferAccountUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() == 4) {
			String outAccount = getHttpParameter(nvps, "outAccount");
			String orderNO = getHttpParameter(nvps, "mchnt_txn_ssn");
			String contractNO = "";
			String version = properties.getFuiouVersion();
			String inAccount = getHttpParameter(nvps, "inAccount");
			String amt = getHttpParameter(nvps, "amt");
			String merchantNO = properties.getMerchantCode();
			String rem = "";
			String signatureStr=SecurityUtils.sign(amt+VERTICAL_BAR+contractNO+VERTICAL_BAR+inAccount+VERTICAL_BAR+merchantNO
					+VERTICAL_BAR+orderNO+VERTICAL_BAR+outAccount+VERTICAL_BAR+rem+VERTICAL_BAR+version);
			List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
			payNvps.add(new BasicNameValuePair("ver", version));
			payNvps.add(new BasicNameValuePair("mchnt_cd", merchantNO));
			payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
			payNvps.add(new BasicNameValuePair("out_cust_no", outAccount));
			payNvps.add(new BasicNameValuePair("in_cust_no", inAccount));
			payNvps.add(new BasicNameValuePair("amt", amt));
			payNvps.add(new BasicNameValuePair("contract_no", contractNO));
			payNvps.add(new BasicNameValuePair("rem", rem));
			payNvps.add(new BasicNameValuePair("signature", signatureStr));
			this.invokeHttp(ctx, transferAccouontUrl, payNvps);
		} else {
			this.echoHttpResponse(ctx, message, content);
		}
	}
	
	private void allocateFunds(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String transferAccouontUrl = properties.getAllocateFundsUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() == 4) {
			String outAccount = getHttpParameter(nvps, "outAccount");
			String orderNO = getHttpParameter(nvps, "mchnt_txn_ssn");
			String contractNO = "";
			String version = properties.getFuiouVersion();
			String inAccount = getHttpParameter(nvps, "inAccount");
			String amt = getHttpParameter(nvps, "amt");
			String merchantNO = properties.getMerchantCode();
			String rem = "";
			String signatureStr=SecurityUtils.sign(amt+VERTICAL_BAR+contractNO+VERTICAL_BAR+inAccount+VERTICAL_BAR+merchantNO
					+VERTICAL_BAR+orderNO+VERTICAL_BAR+outAccount+VERTICAL_BAR+rem+VERTICAL_BAR+version);
			List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
			payNvps.add(new BasicNameValuePair("ver", version));
			payNvps.add(new BasicNameValuePair("mchnt_cd", merchantNO));
			payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
			payNvps.add(new BasicNameValuePair("out_cust_no", outAccount));
			payNvps.add(new BasicNameValuePair("in_cust_no", inAccount));
			payNvps.add(new BasicNameValuePair("amt", amt));
			payNvps.add(new BasicNameValuePair("contract_no", contractNO));
			payNvps.add(new BasicNameValuePair("rem", rem));
			payNvps.add(new BasicNameValuePair("signature", signatureStr));
			this.invokeHttp(ctx, transferAccouontUrl, payNvps);
		} else {
			this.echoHttpResponse(ctx, message, content);
		}
	}
	
	private void preAuth(final ChannelHandlerContext ctx, final FullHttpMessage message, final String content) {
		String preAuthUrl = properties.getPreAuthUrl();
		List<NameValuePair> nvps = ConstantUtil.getHttpParameters(content);
		if(nvps != null && nvps.size() == 4) {
			String customerNO = getHttpParameter(nvps, "cust_no");
			String orderNO = getHttpParameter(nvps, "mchnt_txn_ssn");
			String version = properties.getFuiouVersion();
			String amt = getHttpParameter(nvps, "amt");
			String merchantAccountId = getHttpParameter(nvps, "merchantAccount");
			String merchantNO = properties.getMerchantCode();
			String rem = "";
			String signatureStr=SecurityUtils.sign(amt+VERTICAL_BAR+merchantAccountId+VERTICAL_BAR+merchantNO
					+VERTICAL_BAR+orderNO+VERTICAL_BAR+customerNO+VERTICAL_BAR+rem+VERTICAL_BAR+version);
			nvps.add(new BasicNameValuePair("ver", version));
			nvps.add(new BasicNameValuePair("mchnt_cd", merchantNO));
			nvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
			nvps.add(new BasicNameValuePair("out_cust_no", customerNO));
			nvps.add(new BasicNameValuePair("in_cust_no", merchantAccountId));
			nvps.add(new BasicNameValuePair("amt", amt));
			nvps.add(new BasicNameValuePair("rem", rem));
			nvps.add(new BasicNameValuePair("signature", signatureStr));
			this.invokeHttp(ctx, preAuthUrl, nvps);
		} else {
			this.echoHttpResponse(ctx, message, content);
		}
	}
	
	private String getHttpParameter(List<NameValuePair> nvps, String parameterName) {
		if(nvps != null && nvps.size() > 0) {
			for(NameValuePair nvp : nvps) {
				if(StringUtils.equals(parameterName, nvp.getName())) {
					return nvp.getValue();
				}
			}
		}
		return null;
	}
	
	private void invokeHttp(final ChannelHandlerContext ctx, final String url,final List<NameValuePair> nvps) {
		HttpTask workerTask = new HttpTask(url, nvps);
		Future<String> future = ctx.executor().submit(workerTask);
		future.addListener(new HttpFutureListener(ctx, url, properties.getDefaultMessage()));
	}
	
	private void echoHttpResponse(final ChannelHandlerContext ctx, final FullHttpMessage message, String result) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes(CharsetUtil.UTF_8)));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, CONTENT_TYPE);
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
		if(HttpHeaders.isKeepAlive(message)) {
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(response);
		ctx.flush();
	}
}