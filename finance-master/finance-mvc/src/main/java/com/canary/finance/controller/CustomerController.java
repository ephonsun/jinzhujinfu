package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.RESP_DESC;
import static com.canary.finance.util.ConstantUtil.SUCCESS;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alibaba.fastjson.JSONObject;
//import com.canary.finance.domain.BranchBank;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.Customer;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.ResponseEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.MarketingService;
import com.canary.finance.service.SmsService;
import com.canary.finance.util.ConstantUtil;
import com.canary.finance.util.ValidationUtil;

import io.jsonwebtoken.Claims;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping("/")
public class CustomerController extends BaseController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MarketingService marketingService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@RequestMapping(value="register", method=RequestMethod.GET)
	public String register(Model model) {

		return "register";
	}
	
	@RequestMapping(value="register/{inviter}", method=RequestMethod.GET)
	public String register(@PathVariable("inviter")String inviter, Model model) {
		
		model.addAttribute("inviter", StringUtils.trimToEmpty(inviter));
		return "register";
	}
	
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(String cellphone, String captcha, String code, String inviter, HttpServletRequest request, HttpServletResponse response) {
		if(!StringUtils.equalsIgnoreCase(captcha, code)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("inviter", inviter);
			request.setAttribute("message", this.getMessage("error.captcha"));
			return "register";
		}
		
		if(this.existCustomer(cellphone)) {
			request.setAttribute("result", "success");
			request.setAttribute("message", this.getMessage("customer.exist", new String[] {cellphone}));
			request.setAttribute("function", "register");
			return "message";
		}
		
		this.sendSms(cellphone, SmsPatternEnum.REGISTER, response);
		request.setAttribute("cellphone", cellphone);
		request.setAttribute("inviter", inviter);
		return "register-confirm";
	}
	
	@RequestMapping(value="register/confirm", method=RequestMethod.POST)
	public String confirmRegister(String cellphone, String password, String sms, String inviter, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!this.verifySms(cellphone, sms, SmsPatternEnum.REGISTER, request)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("inviter", inviter);
			request.setAttribute("message", this.getMessage("error.sms"));
			return "register-confirm";
		}
		
		if(this.existCustomer(cellphone)) {
			request.setAttribute("result", "success");
			request.setAttribute("message", this.getMessage("customer.exist", new String[] {cellphone}));
			request.setAttribute("function", "register");
			return "message";
		}
		
		Customer customer = new Customer();		
		customer.setCellphone(cellphone);
		customer.setLoginPassword(DigestUtils.md5Hex(password));
		customer.setRegisterTime(new Date());
		
		Channel channel = new Channel();
		if(StringUtils.isNotBlank(inviter)) {
			channel.setId(2);	//invitation channel.
			customer.setInviterPhone(new String(Base64Utils.decodeFromString(inviter), UTF_8));
		} else {
			channel.setId(1);	//official channel.
			customer.setInviterPhone("");
		}
		customer.setChannel(channel);
		
		if(this.customerService.saveCustomer(customer)) {
			request.setAttribute("result", "success");
			request.setAttribute("message", this.getMessage("register.success", new String[] {cellphone}));
		} else {
			request.setAttribute("result", "failure");
			request.setAttribute("message", this.getMessage("register.failure", new String[] {cellphone}));
		}
		request.setAttribute("function", "register");
		return "message";
	}
	
	@RequestMapping(value="channel/register/confirm", method=RequestMethod.POST)
	public String channelRegister(String cellphone, String password, String sms, String inviter, Integer channelId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!this.verifySms(cellphone, sms, SmsPatternEnum.REGISTER, request)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("inviter", inviter);
			request.setAttribute("message", this.getMessage("error.sms"));
			return "h5/register";
		}
		
		if(this.existCustomer(cellphone)) {
			request.setAttribute("result", "success");
			request.setAttribute("message", this.getMessage("customer.exist", new String[] {cellphone}));
			request.setAttribute("function", "register");
			return "h5/register";
		}
		
		Customer customer = new Customer();		
		customer.setCellphone(cellphone);
		customer.setLoginPassword(DigestUtils.md5Hex(password));
		customer.setRegisterTime(new Date());
		
		Channel channel = new Channel();
		if(StringUtils.isNotBlank(inviter)) {
			channel.setId(2);	//invitation channel.
			customer.setInviterPhone(new String(Base64Utils.decodeFromString(inviter), UTF_8));
		} else {
			channelId = channelId == null ? 1 : channelId;
			channel.setId(channelId);	//official channel.
			customer.setInviterPhone("");
		}
		customer.setChannel(channel);
		
		if(this.customerService.saveCustomer(customer)) {
			return "h5/download";
		} else {
			request.setAttribute("result", "failure");
			request.setAttribute("message", this.getMessage("register.failure", new String[] {cellphone}));
			return "h5/register";
		}
	}
	
	@RequestMapping(value="password/modify", method=RequestMethod.GET)
	public String modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		this.sendSms(claims.getId(), SmsPatternEnum.LOGIN_PASSWORD, response);
		request.setAttribute("cellphone", claims.getId());
		return "password-reset-confirm";
		
	}
	
	@RequestMapping(value="password/reset", method=RequestMethod.GET)
	public String resetPassword(Model model) {
		
		return "password-reset";
	}
	
	@RequestMapping(value="password/reset", method=RequestMethod.POST)
	public String resetPassword(String cellphone, String captcha, String code, HttpServletRequest request, HttpServletResponse response) {
		if(!StringUtils.equalsIgnoreCase(captcha, code)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("message", this.getMessage("error.captcha"));
			return "password-reset";
		}
		
		if(!this.existCustomer(cellphone)) {
			request.setAttribute("result", "failure");
			request.setAttribute("message", this.getMessage("customer.not.exist", new String[] {cellphone}));
			request.setAttribute("function", "reset-password");
			return "message";
		}
		
		this.sendSms(cellphone, SmsPatternEnum.LOGIN_PASSWORD, response);
		request.setAttribute("cellphone", cellphone);
		return "password-reset-confirm";
	}
	
	@RequestMapping(value="password/reset/confirm", method=RequestMethod.POST)
	public String confirmResetPassword(String cellphone, String password, String sms, HttpServletRequest request, HttpServletResponse response) {
		if(!this.verifySms(cellphone, sms, SmsPatternEnum.LOGIN_PASSWORD, request)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("message", this.getMessage("error.sms"));
			return "password-reset-confirm";
		}
		
		if(!this.existCustomer(cellphone)) {
			request.setAttribute("result", "failure");
			request.setAttribute("message", this.getMessage("customer.not.exist", new String[] {cellphone}));
			request.setAttribute("function", "reset-password");
			return "message";
		}
		
		Customer customer = this.customerService.getCustomer(cellphone);
		customer.setLoginPassword(DigestUtils.md5Hex(password));
		if(this.customerService.saveCustomer(customer)) {
			request.setAttribute("result", "success");
			request.setAttribute("message", this.getMessage("password.set.success", new String[] {cellphone}));
		} else {
			request.setAttribute("result", "failure");
			request.setAttribute("message", this.getMessage("password.set.failure", new String[] {cellphone}));
		}
		request.setAttribute("function", "reset-password");
		return "message";
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String login(Model model) {

		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(String cellphone, String password, String captcha, String code, HttpServletRequest request, HttpServletResponse response) {
		if(!StringUtils.equalsIgnoreCase(captcha, code)) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("message", this.getMessage("error.captcha"));
			return "login";
		}
		
		Customer customer = this.customerService.getCustomer(cellphone);
		if(customer == null || !StringUtils.equalsIgnoreCase(customer.getLoginPassword(), DigestUtils.md5Hex(password))) {
			request.setAttribute("cellphone", cellphone);
			request.setAttribute("message", this.getMessage("error.login"));
			return "login";
		}
		
		String subject = this.getMessage("cookie.login");
		//String compact = this.jwtService.getCompact(cellphone, subject, this.getMidnightMillis(), null);
		String compact = this.jwtService.getCompact(cellphone, subject, 0, null);	
		Cookie cookie = new Cookie(subject, compact);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			LOGGER.info("cookie[{}={}] will be removing.", cookie.getName(), cookie.getValue());
			if(StringUtils.equalsIgnoreCase(cookie.getName(), this.getMessage("cookie.login"))) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/";
	}
	
	@RequestMapping(value="/customer/{customerId:\\d+}/open", method=RequestMethod.GET)
	public String openAccount(@PathVariable("customerId")int customerId, HttpServletRequest request) {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		request.setAttribute("customer", this.customerService.getCustomer(customerId));
		request.setAttribute("banks", this.marketingService.getPayBankList());
		request.setAttribute("provinces", this.marketingService.getProvinces());
		return "customer/open";
	}
	
	@RequestMapping(value="/customer/{customerId:\\d+}/open", method=RequestMethod.POST)
	public String openAccount(@PathVariable("customerId")int customerId, String city, Customer customer, HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		if (!ValidationUtil.isIdCard(customer.getIdcard()) || !ValidationUtil.isChinese(customer.getName())) {
			request.setAttribute("message", ResponseEnum.IDCARD_NAME_ERROR.getMsg());
			request.setAttribute("customer", this.customerService.getCustomer(customerId));
			request.setAttribute("banks", this.marketingService.getPayBankList());
			request.setAttribute("provinces", this.marketingService.getProvinces());
			return "customer/open";
		}
		Customer user = customerService.getCustomer(customerId);
		if(user == null) {
			request.setAttribute("message", ResponseEnum.REGISTER_FIRST.getMsg());
			request.setAttribute("customer", this.customerService.getCustomer(customerId));
			request.setAttribute("banks", this.marketingService.getPayBankList());
			request.setAttribute("provinces", this.marketingService.getProvinces());
			return "customer/open";
		}
		Customer cust = customerService.getCustomerWithIdcard(customer.getIdcard());
		if(cust != null && cust.getId() > 0) {
			request.setAttribute("message", ResponseEnum.IDCARD_BINDED.getMsg());
			request.setAttribute("customer", this.customerService.getCustomer(customerId));
			request.setAttribute("banks", this.marketingService.getPayBankList());
			request.setAttribute("provinces", this.marketingService.getProvinces());
			return "customer/open";
		}
		if (StringUtils.isNotBlank(user.getCardNO())) {
			request.setAttribute("message", ResponseEnum.IDCARD_BINDED.getMsg());
			request.setAttribute("customer", this.customerService.getCustomer(customerId));
			request.setAttribute("banks", this.marketingService.getPayBankList());
			request.setAttribute("provinces", this.marketingService.getProvinces());
			return "customer/open";
		}
//		BranchBank branchBank = this.marketingService.getBranchBank(customer.getBankNO(), city);
		customer.setTradePassword(DigestUtils.md5Hex(customer.getTradePassword()));
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("capAcntNo", customer.getCardNO()));
		nvps.add(new BasicNameValuePair("certif_id", customer.getIdcard()));
		nvps.add(new BasicNameValuePair("city_id", city));
		nvps.add(new BasicNameValuePair("cust_nm", customer.getName()));
		nvps.add(new BasicNameValuePair("mchnt_txn_ssn", OrderNOPrefixEnum.JZO.name()+String.valueOf(this.idWorker.nextValue())));
		nvps.add(new BasicNameValuePair("mobile_no", customer.getCellphone()));
		nvps.add(new BasicNameValuePair("parent_bank_id", customer.getBankNO()));
		nvps.add(new BasicNameValuePair("password", StringUtils.lowerCase(customer.getTradePassword())));
		String response = invokeHttp(properties.getRemotingUrl()+properties.getOpenAccountUrl(), nvps);
		LOGGER.info("open account for {}, result is {}", customer.getCellphone(), response);
		if(StringUtils.isNotBlank(response)) {
			XMLSerializer xmlSerializer = new XMLSerializer();
	        JSON json = xmlSerializer.read(response);
	        if (json != null) {
	        	JSONObject respJSON = JSONObject.parseObject(json.toString());
	        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
	        		this.customerService.saveCustomer(customer);
	        		request.setAttribute("function", "customer-bank");
	    			request.setAttribute("result", "success");
	    			request.setAttribute("referer", request.getHeader("Referer"));
	    			request.setAttribute("message", this.getMessage("customer.openaccount.success"));
	    			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/asset/my/1";
	        	} else {
	        		request.setAttribute("function", "customer-bank");
	    			request.setAttribute("result", "failure");
	    			request.setAttribute("referer", request.getHeader("Referer"));
	    			request.setAttribute("message", respJSON.getJSONObject(PLAIN).getString(RESP_DESC));
	    			request.setAttribute("customer", this.customerService.getCustomer(customerId));
	    			request.setAttribute("banks", this.marketingService.getPayBankList());
	    			request.setAttribute("provinces", this.marketingService.getProvinces());
	    			return "customer/open";
	        	}
	        }
		}
		
		request.setAttribute("function", "customer-bank");
		request.setAttribute("result", "failure");
		request.setAttribute("referer", request.getHeader("Referer"));
		request.setAttribute("message", this.getMessage("customer.openaccount.failure"));
		request.setAttribute("customer", this.customerService.getCustomer(customerId));
		request.setAttribute("banks", this.marketingService.getPayBankList());
		request.setAttribute("provinces", this.marketingService.getProvinces());
		return "customer/open";
	}
	
	@RequestMapping(value="/customer/deposit", method=RequestMethod.GET)
	public String deposit(HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		if(customer == null || customer.getId() <= 0) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		
		request.setAttribute("platform", 1);
		request.setAttribute("customer", customer);
		request.setAttribute("bank", this.marketingService.getPayBank(customer.getBankNO()));
		return "customer/deposit";
	}
	
	@RequestMapping(value="/customer/withdraw", method=RequestMethod.GET)
	public String withdraw(HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		if(customer == null || customer.getId() <= 0) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		
		double balance = 0.00d;
		String orderNO = OrderNOPrefixEnum.JZM.name()+idWorker.nextValue();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("cust_no", customer.getCellphone()));
		nvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
		String response = invokeHttp(properties.getRemotingUrl()+properties.getAccountBalanceUrl(), nvps);
		if (StringUtils.isNotBlank(response)) {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer();
		        JSON json = xmlSerializer.read(response);
		        if (json != null) {
		        	JSONObject respJSON = JSONObject.parseObject(json.toString());
		        	if (respJSON != null && respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		JSONObject result = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result");
		        		if(result != null && result.containsKey("ca_balance")) {
		        			Double caBalance = result.getDouble("ca_balance");
		        			if(caBalance != null) {
		        				balance = caBalance;
		        			}
		        		}
		        	}
		        }
			} catch (Exception e) {
				LOGGER.error("get customer[{}]'s balance error: {}", customer.getCellphone(), e.getMessage());
				balance = 0.00;
			}
		}
		request.setAttribute("platform", 1);
		request.setAttribute("balance", balance/100.0);
		request.setAttribute("customer", customer);
		return "customer/withdraw";
	}
	
	private boolean existCustomer(String cellphone) {
		Customer customer = this.customerService.getCustomer(cellphone);
		if(customer != null && customer.getId() > 0) {
			return true;
		}
		return false;
	}
	
	private void sendSms(String cellphone, SmsPatternEnum pattern, HttpServletResponse response) {
		JSONObject result = this.smsService.send(cellphone, pattern.getKey(), pattern);
		LOGGER.info("sms send for {}, result is {}", cellphone, result.toString());
		if(ResponseEnum.SUCCESS.getCode() == result.getIntValue(ConstantUtil.CODE)) {
			Cookie cookie = new Cookie(pattern.getKey()+cellphone, result.getJSONObject(ConstantUtil.DATA).getString("token"));
			cookie.setHttpOnly(true);
			cookie.setMaxAge(1200);
			response.addCookie(cookie);
		}
	}
	
	private boolean verifySms(String cellphone, String sms, SmsPatternEnum pattern, HttpServletRequest request) {
		String cookieName = pattern.getKey()+cellphone;
		Cookie cookie = this.getCookie(request, cookieName);
		if(cookie != null) {
			LOGGER.info("sms jwt compact: {}", cookie.getValue());
			try {
				Claims claims = this.jwtService.parseToken(cookie.getValue());
				if(claims != null && StringUtils.equals(claims.getAudience(), cookieName) && StringUtils.equals(claims.getId(), sms)) {
					return true;
				}
			} catch (Exception e) {
				LOGGER.error("verify [{}]sms error: {}", cookieName, e.getMessage());
			}
		}
		return false;
	}
}
