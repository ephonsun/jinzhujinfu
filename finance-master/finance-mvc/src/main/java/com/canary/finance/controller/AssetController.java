package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.SUCCESS;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.OrderService;

import io.jsonwebtoken.Claims;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping("/asset")
public class AssetController extends BaseController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@RequestMapping("/my/{type:\\d+}")
	public String getMyAsset(@PathVariable("type")int type, Integer page, Integer size, HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		int total = 0;
		if(type == 2) {
			total = this.orderService.getCustomerRefundOrderCount(customer.getId());
		} else {
			total = this.orderService.getCustomerProfitOrderCount(customer.getId());
		}
		request.setAttribute("now", Calendar.getInstance().getTime());
		
		int totalPrincipal = customerService.getTotalPrincipal(customer.getId());
		double unPaybackProfit = customerService.getUnPaybackProfit(customer.getId());
		double paybackProfit = customerService.getPaybackProfit(customer.getId());
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
				LOGGER.error("get customer[{}]'s balance error: {}", customer.getCellphone(), e.getMessage());
				balance = 0.00;
			}
		}
		request.setAttribute("sumAsset", totalPrincipal + unPaybackProfit + balance/100.0);
		request.setAttribute("accumulatedIncome", unPaybackProfit + paybackProfit);
		request.setAttribute("balance", balance/100.0);
		request.setAttribute("type", type);
		request.setAttribute("total", total);
		request.setAttribute("page", this.getPage(page));
		request.setAttribute("size", this.getPageSize(size));
		request.setAttribute("pages", this.getTotalPage(total, size));
		request.setAttribute("customer", customer);
		return "asset/my";
	}
	
	@RequestMapping(value="/my/{type:\\d+}/content", method=RequestMethod.POST)
	public String getTradeOrderListContent(@PathVariable("type")int type, int customerId, Integer page, Integer size, Model model) {
		if(type == 2) {
			model.addAttribute("orders", this.orderService.getCustomerRefundOrderList(customerId, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("orders", this.orderService.getCustomerProfitOrderList(customerId, this.getOffset(page, size), this.getPageSize(size)));
		}
		model.addAttribute("type", type);
		return "content";
	}
	
	@RequestMapping("/transaction")
	public String getTransaction(Integer page, Integer size, HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		int total = this.customerService.getCustomerBalanceCount(customer.getId());
		if(total > 0) {
			List<CustomerBalance> transactions = this.customerService.getCustomerBalanceList(customer.getId(), this.getOffset(page, size), this.getPageSize(size));
			if (transactions != null && transactions.size() > 0) {
				for (CustomerBalance balance : transactions) {
					if (balance.getCategory() == PurposeEnum.PAY_SUCCESS.getType()
							|| balance.getCategory() == PurposeEnum.WITHDRAW.getType()) {
						balance.setAmount(0-balance.getAmount());
					}
				}
			}
			request.setAttribute("transactions", transactions);
		} else {
			request.setAttribute("transactions", new CustomerBalance[0]);
		}
		request.setAttribute("customer", customer);
		request.setAttribute("total", total);
		request.setAttribute("page", this.getPage(page));
		request.setAttribute("size", this.getPageSize(size));
		request.setAttribute("pages", this.getTotalPage(total, size));
		return "asset/transaction";
	}
	
	@RequestMapping("/coupon")
	public String getCoupon(Integer page, Integer size, HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		int total = this.customerService.getCustomerCouponCount(customer.getId(), 1);
		if(total > 0) {
			request.setAttribute("coupons", this.customerService.getCustomerCouponList(customer.getId(), 1, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			request.setAttribute("coupons", new CustomerCoupon[0]);
		}
		request.setAttribute("customer", customer);
		request.setAttribute("total", total);
		request.setAttribute("page", this.getPage(page));
		request.setAttribute("size", this.getPageSize(size));
		request.setAttribute("pages", this.getTotalPage(total, size));
		return "asset/coupon";
	}
	
	@RequestMapping("/invitation/{type:\\d+}")
	public String getInvitation(@PathVariable("type")int type, Integer page, Integer size, HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		int total = 0;
		if(type == 2) {
			total = this.customerService.getInvitorCouponForCount(customer.getId());
		} else {
			total = this.customerService.getInvitationCount(customer.getCellphone());
		}
		request.setAttribute("base64", Base64Utils.encodeToString(customer.getCellphone().getBytes(UTF_8)));
		request.setAttribute("customer", customer);
		request.setAttribute("total", total);
		request.setAttribute("page", this.getPage(page));
		request.setAttribute("size", this.getPageSize(size));
		request.setAttribute("pages", this.getTotalPage(total, size));
		return "asset/invitation";
	}
	
	@RequestMapping(value="/invitation/{type:\\d+}/content", method=RequestMethod.POST)
	public String getInvitationListContent(@PathVariable("type")int type, int customerId, String cellphone, Integer page, Integer size, Model model) {
		if(type == 2) {
			model.addAttribute("invitations", this.customerService.getInvitorCouponForList(customerId, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("invitations", this.customerService.getInvitationList(cellphone, this.getOffset(page, size), this.getPageSize(size)));
		}
		model.addAttribute("type", type);
		return "content";
	}
	
	@RequestMapping("/setting")
	public String setting(HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		request.setAttribute("customer", this.customerService.getCustomer(claims.getId()));
		return "asset/setting";
	}
}
