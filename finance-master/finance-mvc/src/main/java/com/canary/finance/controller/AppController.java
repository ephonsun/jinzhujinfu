package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.CODE;
import static com.canary.finance.util.ConstantUtil.DATA;
import static com.canary.finance.util.ConstantUtil.MSG;
import static com.canary.finance.util.ConstantUtil.NORMAL_DATETIME_FORMAT;
import static com.canary.finance.util.ConstantUtil.NORMAL_DATE_FORMAT;
import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.RESP_DESC;
import static com.canary.finance.util.ConstantUtil.SUCCESS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.Product;
import com.canary.finance.enumeration.MessageSceneEnum;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.ProductCategoryEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.enumeration.ResponseEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.pojo.CustomerBalanceVO;
import com.canary.finance.pojo.CustomerMessageVO;
import com.canary.finance.pojo.CustomerOrderVO;
import com.canary.finance.pojo.InvitorCouponVO;
import com.canary.finance.pojo.NewsVO;
import com.canary.finance.pojo.ProductVO;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.MarketingService;
import com.canary.finance.service.NewsBulletinService;
import com.canary.finance.service.OperationService;
import com.canary.finance.service.ProductService;
import com.canary.finance.service.SmsService;
import com.canary.finance.service.TradeService;
import com.canary.finance.util.ValidationUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping("/rest")
public class AppController extends BaseController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private MarketingService marketingService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private ProductService productService;
	@Autowired
	private NewsBulletinService newsBulletinService;
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@RequestMapping(value="/customer/asset", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject content(int customerId) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if(customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		int totalPrincipal = customerService.getTotalPrincipal(customerId);
		double unPaybackProfit = customerService.getUnPaybackProfit(customerId);
		double paybackProfit = customerService.getPaybackProfit(customerId);
		double balance = 0.00; 
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
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		balance = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result").getDoubleValue("ca_balance");
		        	}
		        }
			} catch (Exception e) {
				balance = 0.00;
			}
		}
		JSONObject data = new JSONObject();
		data.put("totalAsset", totalPrincipal + unPaybackProfit + balance/100.0);
		data.put("accumulationProfit", unPaybackProfit + paybackProfit);
		data.put("balance", balance/100.0);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/balance", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject customerBalance(int customerId) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if(customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		double balance = 0.00; 
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
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		balance = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result").getDoubleValue("ca_balance");
		        	}
		        }
			} catch (Exception e) {
				balance = 0.00;
			}
		}
		JSONObject data = new JSONObject();
		data.put("balance", balance/100.0);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/register", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject regsiterUser(String cellphone, String password, String code, String token, Integer channelId, String friendCellphone, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		if(!ValidationUtil.isMobile(cellphone)) {
			result.put(CODE, ResponseEnum.MOBILE_FORMAT_ERROR.getCode());
			result.put(MSG, ResponseEnum.MOBILE_FORMAT_ERROR.getMsg());
			return result;
		}
		channelId = channelId == null ? 1 : channelId;
		try {
			JwtParser parser = Jwts.parser();
			Jws<Claims> claims = parser.setSigningKey(TextCodec.BASE64.encode(properties.getPrivateKey())).parseClaimsJws(token);
			if (claims == null || claims.getBody() == null || !StringUtils.equals(claims.getBody().getId(), code)) {
				result.put(CODE, ResponseEnum.VALIDATE_CODE_ERROR.getCode());
				result.put(MSG, ResponseEnum.VALIDATE_CODE_ERROR.getMsg());
				return result;
			}
		} catch (Exception e) {
			result.put(CODE, ResponseEnum.VALIDATE_CODE_ERROR.getCode());
			result.put(MSG, ResponseEnum.VALIDATE_CODE_ERROR.getMsg());
			return result;
		}
		Customer customer = customerService.getCustomer(cellphone);
		if(customer != null && customer.getId() > 0) {
			result.put(CODE, ResponseEnum.ALREADY_REGISTER.getCode());
			result.put(MSG, ResponseEnum.ALREADY_REGISTER.getMsg());		
			return result;
		}
		
		customer = new Customer();
		customer.setCellphone(cellphone);
		customer.setLoginPassword(DigestUtils.md5Hex(password));
		customer.setRegisterTime(Calendar.getInstance().getTime());
		if(channelId >= 0){
			Channel channel=new Channel();
			channel.setId(channelId);
			customer.setChannel(channel);
		}
		
		if(ValidationUtil.isMobile(friendCellphone)){
			Customer friend = customerService.getCustomer(friendCellphone);
			if(friend != null && friend.getId() > 0){
				customer.getChannel().setId(2);
				customer.setInviterPhone(friendCellphone);
			}
		}
		if(customerService.saveCustomer(customer)) {
			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
			JSONObject data = new JSONObject();
			data.put("user", customerService.getCustomer(cellphone));
			result.put(DATA, data);
			return result;
		}
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;
	}
	
	@RequestMapping(value="/customer/purchase", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject purchaseProduct(int customerId, int productId, Integer couponId, int portion,
			int payType, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if (customer == null || customer.getId() == 0) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		couponId = couponId == null ? 0 : couponId;
		Product product = productService.getProduct(productId);
		if (product == null || product.getId() == 0 || product.getMerchant() == null || StringUtils.isBlank(product.getMerchant().getCellphone()) 
				|| product.getActualAmount() >= product.getTotalAmount()) {
			result.put(CODE, ResponseEnum.PRODUCT_SOLD_OUT.getCode());
			result.put(MSG, ResponseEnum.PRODUCT_SOLD_OUT.getMsg());
			return result;
		}
		if (product.getCategory() != null && StringUtils.equals(ProductCategoryEnum.NOVICE.name(), product.getCategory().getProperty())) {
			int times = tradeService.getCustomerOrderCount(productId, customerId);
			if (times > 0) {
				result.put(CODE, ResponseEnum.NOVICE_LIMIT.getCode());
				result.put(MSG, ResponseEnum.NOVICE_LIMIT.getMsg());
				return result;
			}
		}
		if ((product.getActualAmount()+portion*product.getLowestMoney()) > product.getTotalAmount()) {
			result.put(CODE, ResponseEnum.PRODUCT_BALANCE.getCode());
			result.put(MSG, String.format(ResponseEnum.PRODUCT_BALANCE.getMsg(), (product.getTotalAmount()-product.getActualAmount())));
			return result;
		}
		double balance = 0.00; 
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
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		balance = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result").getDoubleValue("ca_balance");
		        	}
		        }
			} catch (Exception e) {
				balance = 0.00;
			}
		}
		if (balance < product.getLowestMoney()*portion*100) {
			result.put(CODE, ResponseEnum.LACK_OF_BALANCE.getCode());
			result.put(MSG, ResponseEnum.LACK_OF_BALANCE.getMsg());
			return result;
		}
		String payOrderNO = OrderNOPrefixEnum.JZP.name()+idWorker.nextValue();
		List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
		payNvps.add(new BasicNameValuePair("cust_no", customer.getCellphone()));
		payNvps.add(new BasicNameValuePair("merchantAccount", product.getMerchant().getCellphone()));
		payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", payOrderNO));
		payNvps.add(new BasicNameValuePair("amt", String.valueOf(portion*product.getLowestMoney()*100)));
		String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getPreAuthUrl(), payNvps);
		if (StringUtils.isNotBlank(payResponse)) {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer();
		        JSON json = xmlSerializer.read(payResponse);
		        if (json != null) {
		        	JSONObject respJSON = JSONObject.parseObject(json.toString());
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		String contractNO = respJSON.getJSONObject(PLAIN).getString("contract_no"); 
		        		String paybackNO = OrderNOPrefixEnum.JZB.name()+idWorker.nextValue();
			    		boolean saveResult = tradeService.saveOrder(product, customer, payOrderNO, couponId, portion, payType, paybackNO, contractNO);
			    		if (saveResult) {
			    			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			    			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
			    			return result;
			    		} 
		        	}
		        }
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;
		/*String payOrderNO = OrderNOPrefixEnum.JZP.name()+idWorker.nextValue();
		List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
		payNvps.add(new BasicNameValuePair("inAccount", properties.getMerchantAccount()));
		payNvps.add(new BasicNameValuePair("outAccount", customer.getCellphone()));
		payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", payOrderNO));
		payNvps.add(new BasicNameValuePair("amt", String.valueOf(portion*product.getLowestMoney()*100)));
		String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getTransferAccountUrl(), payNvps);
		JSONObject respJSON = null;
		if (StringUtils.isNotBlank(payResponse)) {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer();
		        JSON json = xmlSerializer.read(payResponse);
		        if (json != null) {
		        	respJSON = JSONObject.parseObject(json.toString());
		        }
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
    	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
    		String paybackNO = OrderNOPrefixEnum.JZB.name()+idWorker.nextValue();
    		boolean saveResult = tradeService.saveOrder(product, customer, payOrderNO, couponId, portion, payType, paybackNO);
    		if (saveResult) {
    			result.put(CODE, ResponseEnum.SUCCESS.getCode());
    			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
    			return result;
    		}
    	}
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;*/
	}
	
	@RequestMapping(value="/customer/withdraw/validation", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject purchaseProduct(int customerId, int amount, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if (customer == null || customer.getId() == 0) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		double balance = 0.00; 
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
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		balance = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result").getDoubleValue("ca_balance");
		        	}
		        }
			} catch (Exception e) {
				balance = 0.00;
			}
		}
		if (balance < amount) {
			result.put(CODE, ResponseEnum.LACK_OF_BALANCE.getCode());
			result.put(MSG, ResponseEnum.LACK_OF_BALANCE.getMsg());
			return result;
		}
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/customer/find/password", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject findUserLoginPassword(String cellphone, String password, String code, String token) {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		try {
			JwtParser parser = Jwts.parser();
			Jws<Claims> claims = parser.setSigningKey(TextCodec.BASE64.encode(properties.getPrivateKey())).parseClaimsJws(token);
			if (claims == null || claims.getBody() == null || !StringUtils.equals(claims.getBody().getId(), code)) {
				result.put(CODE, ResponseEnum.VALIDATE_CODE_ERROR.getCode());
				result.put(MSG, ResponseEnum.VALIDATE_CODE_ERROR.getMsg());
				return result;
			}
		} catch (Exception e) {
			result.put(CODE, ResponseEnum.VALIDATE_CODE_ERROR.getCode());
			result.put(MSG, ResponseEnum.VALIDATE_CODE_ERROR.getMsg());
			return result;
		}
		Customer customer = customerService.getCustomer(cellphone);
		if(customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		customer.setLoginPassword(DigestUtils.md5Hex(password));
		boolean res = customerService.saveCustomer(customer);
		if(res){
			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
			return result;
		}else{
			result.put(CODE, ResponseEnum.FAIL.getCode());
			result.put(MSG, ResponseEnum.FAIL.getMsg());
			return result;
		}
	}
	
	@RequestMapping(value="/customer/open/account", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject openAccount(int customerId, String name, String idcard, String tradePassword, String cardNO, String cityId, String bankNO, String bankName) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		if (!ValidationUtil.isIdCard(idcard) || !ValidationUtil.isChinese(name)) {
			result.put(CODE, ResponseEnum.IDCARD_NAME_ERROR.getCode());
			result.put(MSG, ResponseEnum.IDCARD_NAME_ERROR.getMsg());
			return result;
		}
		Customer customer = customerService.getCustomer(customerId);
		if(customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		Customer cust = customerService.getCustomerWithIdcard(idcard);
		if(cust != null && cust.getId() > 0) {
			result.put(CODE, ResponseEnum.IDCARD_BINDED.getCode());
			result.put(MSG, ResponseEnum.IDCARD_BINDED.getMsg());
			return result;
		}
		if (StringUtils.isNotBlank(customer.getCardNO())) {
			result.put(CODE, ResponseEnum.IDCARD_BINDED.getCode());
			result.put(MSG, ResponseEnum.IDCARD_BINDED.getMsg());
			return result;
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("capAcntNo", cardNO));
		nvps.add(new BasicNameValuePair("certif_id", idcard));
		nvps.add(new BasicNameValuePair("city_id", cityId));
		nvps.add(new BasicNameValuePair("cust_nm", name));
		nvps.add(new BasicNameValuePair("mchnt_txn_ssn", OrderNOPrefixEnum.JZO.name()+String.valueOf(this.idWorker.nextValue())));
		nvps.add(new BasicNameValuePair("mobile_no", customer.getCellphone()));
		nvps.add(new BasicNameValuePair("parent_bank_id", bankNO));
		nvps.add(new BasicNameValuePair("password", StringUtils.lowerCase(DigestUtils.md5Hex(tradePassword))));
		String response = invokeHttp(properties.getRemotingUrl()+properties.getOpenAccountUrl(), nvps);
		if (StringUtils.isNotBlank(response)) {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer();
		        JSON json = xmlSerializer.read(response);
		        if (json != null) {
		        	JSONObject respJSON = JSONObject.parseObject(json.toString());
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		customer.setBankName(bankName);
		        		customer.setBankNO(bankNO);
		        		customer.setCardNO(cardNO);
		        		customer.setIdcard(idcard);
		        		customer.setName(name);
		        		customerService.saveCustomer(customer);
		        		result.put(CODE, ResponseEnum.SUCCESS.getCode());
						result.put(MSG, ResponseEnum.SUCCESS.getMsg());
						return result;
		        	} else {
		        		result.put(CODE, ResponseEnum.FAIL.getCode());
						result.put(MSG, respJSON.getJSONObject(PLAIN).getString(RESP_DESC));
						return result;
		        	}
		        }
			} catch (Exception e) {
				result.put(CODE, ResponseEnum.FAIL.getCode());
				result.put(MSG, ResponseEnum.FAIL.getMsg());
				return result;
			}
		}
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;
	}
	
	@RequestMapping(value="/customer/sms/code", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getSmsCode(String cellphone, String type, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		if(!ValidationUtil.isMobile(cellphone)) {
			result.put(CODE, ResponseEnum.MOBILE_FORMAT_ERROR.getCode());
			result.put(MSG, ResponseEnum.MOBILE_FORMAT_ERROR.getMsg());
			return result;
		}
		if (StringUtils.equals(type, SmsPatternEnum.REGISTER.getKey())) {
			Customer customer = customerService.getCustomer(cellphone);
			if (customer != null && customer.getId() > 0) {
				result.put(CODE, ResponseEnum.ALREADY_REGISTER.getCode());
				result.put(MSG, ResponseEnum.ALREADY_REGISTER.getMsg());
				return result;
			}
			return smsService.send(cellphone, type, SmsPatternEnum.REGISTER);
		}
		Customer customer = customerService.getCustomer(cellphone);
		if (customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		if (StringUtils.equals(type, SmsPatternEnum.LOGIN_PASSWORD.getKey())) {
			return smsService.send(cellphone, type, SmsPatternEnum.LOGIN_PASSWORD);
		}
		if (StringUtils.equals(type, SmsPatternEnum.TRADE_PASSWORD.getKey())) {
			return smsService.send(cellphone, type, SmsPatternEnum.TRADE_PASSWORD);
		}
		result.put(CODE, ResponseEnum.FAIL.getCode());
		result.put(MSG, ResponseEnum.FAIL.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/order")
	@ResponseBody
	public JSONObject getOrder(int customerId, Integer isPayback, Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		int size = DEFAULT_PAGE_SIZE;
		isPayback = isPayback == null ? 0 : isPayback;
		int total = this.tradeService.getOrderForCount(customerId, isPayback);
		if(total > 0) {
			data.put("total", total);
			data.put("orders", this.tradeService.getOrderForList(customerId, isPayback, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			data.put("total", 0);
			data.put("orders", new ArrayList<CustomerOrderVO>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/invitor/list")
	@ResponseBody
	public JSONObject getInvitorList(String cellphone, Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		int size = DEFAULT_PAGE_SIZE;
		int total = this.customerService.getInvitorCount(cellphone);
		if(total > 0) {
			data.put("total", total);
			List<Customer> inviters = this.customerService.getInvitorList(cellphone, this.getOffset(page, size), this.getPageSize(size));
			for (Customer customer : inviters) {
				if (customer.getTradeTime() != null && customer.getTradeTime().getTime() < 0) {
					customer.setTradeTime(null);
				} 
			}
			data.put("invitors", inviters);
		}else{
			data.put("total", 0);
			data.put("invitors", new ArrayList<Customer>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/invitors")
	@ResponseBody
	public JSONObject getInvitorNum(String cellphone) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("invitorNum", this.customerService.getInvitorCount(cellphone));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/invitor/coupon/list")
	@ResponseBody
	public JSONObject getInvitorCouponList(String cellphone, Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		Customer customer = customerService.getCustomer(cellphone);
		int size = DEFAULT_PAGE_SIZE;
		int total = 0;
		if (customer != null && customer.getId() > 0) {
			total = this.customerService.getInvitorCouponForCount(customer.getId());
			if(total > 0) {
				data.put("total", total);
				data.put("coupons", this.customerService.getInvitorCouponForList(customer.getId(), this.getOffset(page, size), this.getPageSize(size)));
			}else{
				data.put("total", 0);
				data.put("coupons", new ArrayList<InvitorCouponVO>());
			}
		} else {
			data.put("total", 0);
			data.put("coupons", new ArrayList<InvitorCouponVO>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/invitor/coupons")
	@ResponseBody
	public JSONObject getInvitorCouponNum(String cellphone) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		Customer customer = customerService.getCustomer(cellphone);
		if (customer != null && customer.getId() > 0) {
			data.put("couponNum", this.customerService.getInvitorCouponForCount(customer.getId()));
		} else {
			data.put("couponNum", 0);
		}
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/balance/record")
	@ResponseBody
	public JSONObject getRecord(int customerId, Integer type, Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		int size = DEFAULT_PAGE_SIZE;
		type = type == null ? -1 : type;
		int total = this.tradeService.getCustomerBalanceCount(customerId, type);
		if(total > 0) {
			data.put("total", total);
			List<CustomerBalanceVO> records = this.tradeService.getCustomerBalanceList(customerId, type, this.getOffset(page, size), this.getPageSize(size));
			if (records != null && records.size() > 0) {
				for (CustomerBalanceVO balance : records) {
					if (balance.getCategory() == PurposeEnum.PAY_SUCCESS.getType()
							|| balance.getCategory() == PurposeEnum.WITHDRAW.getType()) {
						balance.setAmount(0-balance.getAmount());
					}
				}
			}
			data.put("records", records);
		}else{
			data.put("total", 0);
			data.put("records", new ArrayList<CustomerBalanceVO>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/order/detail")
	@ResponseBody
	public JSONObject getOrderDetail(String orderNO) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("order", tradeService.getOrderDetail(orderNO));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/customer/coupon")
	@ResponseBody
	public JSONObject getCoupons(int customerId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("coupons", customerService.getCouponList(customerId));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/customer/login", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject login(String cellphone, String password){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		if(!ValidationUtil.isMobile(cellphone)) {
			result.put(CODE, ResponseEnum.MOBILE_FORMAT_ERROR.getCode());
			result.put(MSG, ResponseEnum.MOBILE_FORMAT_ERROR.getMsg());
			return result;
		}
		Customer customer = customerService.getCustomer(cellphone);
		if (customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		if(!StringUtils.equals(DigestUtils.md5Hex(password), customer.getLoginPassword())){
			result.put(CODE, ResponseEnum.ACCOUNT_ERROR.getCode());
			result.put(MSG, ResponseEnum.ACCOUNT_ERROR.getMsg());
			return result;
		}
		if (StringUtils.isNotBlank(customer.getTradePassword())) {
			customer.setTradePassword("******");
		}
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("user", customer);
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/password", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject setUserLoginPassword(int customerId, String oldPassword, String newPassword) {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if(customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		if(!StringUtils.equals(DigestUtils.md5Hex(oldPassword), customer.getLoginPassword())){
			result.put(CODE, ResponseEnum.OLD_PASSWORD_ERROR.getCode());
			result.put(MSG, ResponseEnum.OLD_PASSWORD_ERROR.getMsg());
			return result;
		}
		if(StringUtils.equals(oldPassword, newPassword)){
			result.put(CODE, ResponseEnum.PASSWORD_CAN_NOTEQU.getCode());
			result.put(MSG, ResponseEnum.PASSWORD_CAN_NOTEQU.getMsg());
			return result;
		}
		customer.setLoginPassword(DigestUtils.md5Hex(newPassword));
		boolean res = customerService.saveCustomer(customer);
		if(res){
			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
			return result;
		}else{
			result.put(CODE, ResponseEnum.FAIL.getCode());
			result.put(MSG, ResponseEnum.FAIL.getMsg());
			return result;
		}
	}
	
	@RequestMapping(value="/customer/info", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getCustomer(int customerId){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Customer customer = customerService.getCustomer(customerId);
		if (customer == null) {
			result.put(CODE, ResponseEnum.REGISTER_FIRST.getCode());
			result.put(MSG, ResponseEnum.REGISTER_FIRST.getMsg());
			return result;
		}
		if (StringUtils.isNotBlank(customer.getTradePassword())) {
			customer.setTradePassword("******");
		}
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("user", customer);
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/message", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getCustomerMessage(int customerId){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		List<CustomerMessage> messages = customerService.getMessageList(customerId);
		if (messages != null && messages.size() > 0) {
			List<CustomerMessageVO> messageVOs = new ArrayList<CustomerMessageVO>();
			SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_DATE_FORMAT);
			for (CustomerMessage message : messages) {
				CustomerMessageVO vo = new CustomerMessageVO();
				vo.setId(message.getId());
				vo.setTitle(MessageSceneEnum.getMsg(message.getScene()));
				vo.setContent(message.getMessage());
				vo.setStatus(message.getStatus());
				vo.setTime(dateFormat.format(message.getEffectTime()));
				messageVOs.add(vo);
			}
			data.put("messages", messageVOs);
		} else {
			data.put("messages", new ArrayList<CustomerMessageVO>());
		}
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/unread/message/count", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getCustomerUnreadMessage(int customerId){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("unreadCount", customerService.getUnreadCount(customerId));
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/customer/read/message", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject readMessage(int messageId){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		if (customerService.read(messageId)) {
			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		} else {
			result.put(CODE, ResponseEnum.FAIL.getCode());
			result.put(MSG, ResponseEnum.FAIL.getMsg());
		}
		return result;
	}
	
	@RequestMapping(value="/service/banner", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getMaterials(Integer platform) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		platform = platform == null ? 1 : platform;
		data.put("banners", operationService.getAppPictureList());
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/materials", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getMaterials(Integer page, Integer size, Integer type) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		size = size == null ? DEFAULT_PAGE_SIZE : size;
		type = type == null ? 1 : type;
		int total = operationService.getNewsBulletinCount(type);
		if(total > 0){
			data.put("materials", operationService.getNewsBulletinList(type, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			data.put("materials", null);
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/material/detail", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getMaterialDetail(Integer materialId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		materialId = materialId == null ? 0 : materialId;
		data.put("material", operationService.getNewsBulletin(materialId));
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/activities", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getActivities(Integer page, Integer size) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		size = size == null ? DEFAULT_PAGE_SIZE : size;
		int total = operationService.getActivityCount();
		if(total > 0){
			data.put("activities", operationService.getActivityList(this.getOffset(page, size), this.getPageSize(size)));
		}else{
			data.put("activities", null);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		data.put("systemTime", dateFormat.format(Calendar.getInstance().getTime()));
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/service/news")
	@ResponseBody
	public JSONObject getNewsBulletinList(int type, Integer page, Integer size, Model model) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		int total = this.newsBulletinService.getNewsBulletinCount(type);
		if(total > 0){
			List<NewsVO> newsList = new ArrayList<NewsVO>();
			List<NewsBulletin> list = this.newsBulletinService.getNewsBulletinList(type, this.getOffset(page, size), this.getPageSize(size));
			if (list != null && list.size() > 0) {
				for(NewsBulletin news : list) {
					NewsVO vo = new NewsVO();
					vo.setId(news.getId());
					if (news.getNews() != null) {
						vo.setNewsDate(news.getNews().getNewsDate());
						vo.setRemark(news.getNews().getTitle());
						if (StringUtils.contains(news.getNews().getUrl(), "news/detail/")) {
							vo.setType(1);
						} else {
							vo.setType(2);
						}
						vo.setUrl(news.getNews().getUrl());
					}
					newsList.add(vo);
				}
			}
			data.put("news", newsList);
		}else{
			data.put("news", null);
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/activity/detail", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getActivityDetail(Integer activityId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		activityId = activityId == null ? 0 : activityId;
		data.put("activity", operationService.getActivity(activityId));
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/latest/version", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getLatestVersion(String postfix) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		AppVersion version = operationService.getLatestAppVersion(postfix);
		data.put("appVersion", version);
		result.put(DATA, data);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping(value="/service/faq", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getFaq(){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("faqs", this.marketingService.getFaqList());
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/service/pay/bank", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getPayBanks(){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("banks", this.marketingService.getPayBankList());
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/service/provinces", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getProvinces(){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("provinces", this.marketingService.getProvinces());
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/service/cities", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject ss(String provinceId){
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		result.put(CODE, ResponseEnum.SUCCESS.getCode());
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("cities", this.marketingService.getCities(provinceId));
		result.put(DATA, data);
		return result;
	}
	
	@RequestMapping(value="/service/feedback", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject feedback(String cellphone, String content, String appVersion, 
			String phoneModel, String deviceVersion) {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Feedback feedback = new Feedback();
		feedback.setContact(cellphone);
		feedback.setContent(content);
		feedback.setAppVersion(appVersion);
		feedback.setPhoneModel(phoneModel);
		feedback.setDeviceVersion(deviceVersion);
		feedback.setFeedTime(Calendar.getInstance().getTime());
		if (this.marketingService.saveFeedback(feedback)) {
			result.put(CODE, ResponseEnum.SUCCESS.getCode());
			result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		} else {
			result.put(CODE, ResponseEnum.FAIL.getCode());
			result.put(MSG, ResponseEnum.FAIL.getMsg());
		}
		return result;
	}
	
	@RequestMapping("/product/list")
	@ResponseBody
	public JSONObject getProductList(Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 1 : page;
		int size = DEFAULT_PAGE_SIZE;
		int total = this.productService.getProductCount(0, 0, 0);
		if(total > 0) {
			data.put("total", total);
			data.put("products", wrapProducts(this.productService.getProductList(0, 0, 0, this.getOffset(page, size), this.getPageSize(size))));
		}else{
			data.put("total", 0);
			data.put("products", new ArrayList<ProductVO>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		data.put("systemTime", new SimpleDateFormat(NORMAL_DATETIME_FORMAT).format(new Date()));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}

	@RequestMapping("/product/order")
	@ResponseBody
	public JSONObject getProductOrder(int productId, Integer page) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		page = page == null ? 0 : page;
		int size = DEFAULT_PAGE_SIZE;
		int total = this.tradeService.getCustomerOrderCount(productId);
		if(total > 0) {
			data.put("total", total);
			data.put("orders", this.tradeService.getCustomerOrderList(productId, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			data.put("total", 0);
			data.put("orders", new ArrayList<ProductVO>());
		}
		data.put("size", this.getPageSize(size));
		data.put("page", this.getPage(page));
		data.put("pages", this.getTotalPage(total, size));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/product/recommendation")
	@ResponseBody
	public JSONObject getRecommendationProductList() {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		int size = 4;
		int page = 1;
		data.put("products", wrapProducts(this.productService.getProductList(0, 0, 0, this.getOffset(page, size), this.getPageSize(size))));
		data.put("systemTime", new SimpleDateFormat(NORMAL_DATETIME_FORMAT).format(new Date()));
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/product/detail")
	@ResponseBody
	public JSONObject getProductDetail(int productId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		data.put("systemTime", dateFormat.format(new Date()));
		Product product = productService.getProduct(productId);
		if (product != null) {
			data.put("raisedTime", dateFormat.format(product.getRaisedTime()));
		}
		data.put("product", product);
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(DATA, data);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	@RequestMapping("/product/purchase/limit")
	@ResponseBody
	public JSONObject getProductPurchaseTimes(int productId, int customerId) {
		JSONObject result = new JSONObject();
		result.put(DATA, null);
		Product product = productService.getProduct(productId);
		if (product == null || product.getCategory() == null) {
			result.put(CODE, ResponseEnum.NOVICE_LIMIT.getCode());
			result.put(MSG, ResponseEnum.NOVICE_LIMIT.getMsg());
			return result;
		}
		if (product != null && product.getCategory() != null && StringUtils.equals(ProductCategoryEnum.NOVICE.name(), product.getCategory().getProperty())) {
			int times = tradeService.getCustomerOrderCount(productId, customerId);
			if (times > 0) {
				result.put(CODE, ResponseEnum.NOVICE_LIMIT.getCode());
				result.put(MSG, ResponseEnum.NOVICE_LIMIT.getMsg());
				return result;
			}
		}
		result.put(CODE, HttpServletResponse.SC_OK);
		result.put(MSG, ResponseEnum.SUCCESS.getMsg());
		return result;
	}
	
	private List<ProductVO> wrapProducts(List<Product> products) {
		List<ProductVO> productDTOs = new ArrayList<ProductVO>();
		if (products == null || products.size() == 0) {
			return productDTOs;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		SimpleDateFormat format = new SimpleDateFormat(NORMAL_DATE_FORMAT);
		for (Product product : products) {
			ProductVO dto = new ProductVO();
			dto.setId(product.getId());
			dto.setName(product.getName());
			dto.setActualAmount(product.getActualAmount());
			if (product.getCategory() != null) {
				dto.setCategory(product.getCategory().getProperty());
			}
			if (product.getInterestDate() != null) {
				dto.setInterestDate(format.format(product.getInterestDate()));
			} else {
				dto.setInterestDate("");
			}
			dto.setFinancePeriod(product.getFinancePeriod());
			dto.setHighestMoney(product.getHighestMoney());
			dto.setIncreaseInterest(product.getIncreaseInterest());
			dto.setLabel(product.getLabel());
			dto.setLowestMoney(product.getLowestMoney());
			dto.setRaisedTime(dateFormat.format(product.getRaisedTime()));
			dto.setTotalAmount(product.getTotalAmount());
			dto.setYearIncome(product.getYearIncome());
			productDTOs.add(dto);
		}
		return productDTOs;
	}
}
