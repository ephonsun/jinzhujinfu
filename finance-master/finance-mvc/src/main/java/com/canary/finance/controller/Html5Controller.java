package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.VERTICAL_BAR;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.MarketingService;
import com.canary.finance.service.NewsBulletinService;
import com.canary.finance.service.OperationService;
import com.canary.finance.service.ProductService;
import com.canary.finance.service.TradeService;
import com.canary.finance.util.ConstantUtil;
import com.canary.finance.util.SecurityUtils;

@Controller
public class Html5Controller extends BaseController {
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private NewsBulletinService newsBulletinService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private MarketingService marketingService;
	
	@RequestMapping(value="/rest/bank/quota/intro", method=RequestMethod.GET)
	public String limit(HttpServletRequest request){
		request.setAttribute("banks", this.marketingService.getPayBankList());
		return "/h5/bank-quota";
	}
	
	@RequestMapping("/rest/faq")
    public String apphtml(Map<String, Object> model){
		model.put("faqs", this.marketingService.getFaqList());
    	return "/h5/faq";
    }
	
	@RequestMapping("/rest/coupon/description")
	public String couponDescription(){
		return "/h5/coupon-desc";
	}
	
	@RequestMapping("/rest/news/detail")
	public String getNewsBulletinDetail(int newsId, Model model) {
		model.addAttribute("news", this.newsBulletinService.getNewsBulletin(newsId));
		return "/h5/news-detail";
	}	
	
	@RequestMapping("/rest/activity/detail")
	public String getActivityDetail(int activityId, Model model) {
		model.addAttribute("activity", operationService.getActivity(activityId));
		return "/h5/activity-detail";
	}	
	
	@RequestMapping("/rest/product/description")
	public String productDescription(int productId, Map<String, Object> model){
		model.put("product", productService.getProduct(productId));
		return "/h5/product-detail";
	}
	
	@RequestMapping("/rest/product/risk")
	public String productRisk(int productId, Map<String, Object> model){
		model.put("product", productService.getProduct(productId));
		return "/h5/product-risk";
	}
	
	@RequestMapping("/rest/product/material")
	public String productMaterial(int productId, Map<String, Object> model){
		model.put("product", productService.getProduct(productId));
		return "/h5/product-material";
	}
	
	@RequestMapping("/rest/product/protocol")
	public String protocol() {
		return "/h5/product-protocol";
	}
	
	@RequestMapping("/rest/insurance")
	public String insurance() {
		return "/h5/insurance";
	}

	@RequestMapping("/rest/guide")
	public String guide() {
		return "/h5/guide";
	}
	
	@RequestMapping("/rest/invitation")
	public String invitation() {
		return "/h5/invitation";
	}
	
	@RequestMapping("/channel/register/{channelId}")
	public String register(@PathVariable("channelId")int channelId, String inviter, Device device, Model model) {
		model.addAttribute("channelId", channelId);
		model.addAttribute("inviter", inviter);
		if (device.isMobile()) {
			return "/h5/register";
		} else {
			return "register";
		}
	}
	
	@RequestMapping("/channel/download/{channelId}")
	public String download(@PathVariable("channelId")int channelId, Model model) {
		model.addAttribute("channelId", channelId);
		AppVersion version = operationService.getLatestAppVersion("apk");
		if (version != null) {
			model.addAttribute("url", version.getUrl());
		}
		return "/h5/download";
	}
	
	@RequestMapping(value="/rest/customer/trade/password/reset", method=RequestMethod.GET)
	public String tradePasswordReSet(int platform, String cellphone, Model model) throws Exception {
		if (platform == 0) {
			model.addAttribute("reqUrl", properties.getAppTradePasswordSetUrl());
		} else {
			model.addAttribute("reqUrl", properties.getWebTradePasswordSetUrl());
		}
		model.addAttribute("merchantNO", properties.getMerchantCode());
		model.addAttribute("cellphone", cellphone);
		String orderNO = OrderNOPrefixEnum.JZM.name()+idWorker.nextValue();
		model.addAttribute("orderNO", orderNO);
		model.addAttribute("type", String.valueOf(3));
		String backUrl = properties.getWebUrl()+"/rest/customer/trade/password/reset/url";
		model.addAttribute("backUrl", backUrl);
		String signatureStr=SecurityUtils.sign(String.valueOf(3)+VERTICAL_BAR+cellphone+VERTICAL_BAR+properties.getMerchantCode()+VERTICAL_BAR+orderNO);
		model.addAttribute("signature", signatureStr);
		return "pay/reset-password";
	}
	
	@RequestMapping(value="/rest/customer/trade/password/reset/url", method=RequestMethod.GET)
	public String tradePasswordReSetResult(Model model) throws Exception {
		return "pay/reset-password-result";
	}
	
	@RequestMapping(value="/rest/customer/recharge", method=RequestMethod.GET)
	public String recharge(int platform, String cellphone, int amount, Model model) throws Exception {
		String url = "";
		if (platform == 0) {
			url = properties.getAppRechargeUrl();
			model.addAttribute("reqUrl", properties.getAppRechargeUrl());
		} else {
			url = properties.getWebRechargeUrl();
			amount = amount*100;
			model.addAttribute("reqUrl", properties.getWebRechargeUrl());
		}
		model.addAttribute("merchantNO", properties.getMerchantCode());
		model.addAttribute("cellphone", cellphone);
		String orderNO = OrderNOPrefixEnum.JZR.name()+idWorker.nextValue();
		model.addAttribute("orderNO", orderNO);
		model.addAttribute("amount", String.valueOf(amount));
		String backUrl = properties.getWebUrl()+"/rest/customer/recharge/back/url/"+platform;
		String notifyUrl = properties.getWebUrl()+"/rest/customer/recharge/notify/url";
		model.addAttribute("backUrl", backUrl);
		model.addAttribute("notifyUrl", notifyUrl);
		String signatureStr=SecurityUtils.sign(String.valueOf(amount)+VERTICAL_BAR+notifyUrl+VERTICAL_BAR+cellphone+VERTICAL_BAR+
				properties.getMerchantCode()+VERTICAL_BAR+orderNO+VERTICAL_BAR+backUrl);
		model.addAttribute("signature", signatureStr);
		LOGGER.info("recharge params:" + url + VERTICAL_BAR + String.valueOf(amount)+VERTICAL_BAR+notifyUrl+VERTICAL_BAR+cellphone+VERTICAL_BAR+
				properties.getMerchantCode()+VERTICAL_BAR+orderNO+VERTICAL_BAR+backUrl+VERTICAL_BAR+signatureStr);
		return "pay/recharge";
	}
	
	@RequestMapping(value="/rest/customer/withdraw", method=RequestMethod.GET)
	public String withdraw(int platform, String cellphone, double amount, Model model) throws Exception {
		String url = "";
		if (platform == 0) {
			url = properties.getAppWithdrawUrl();
			model.addAttribute("reqUrl", properties.getAppWithdrawUrl());
		} else {
			url = properties.getWebWithdrawUrl();
			amount = amount*100;
			model.addAttribute("reqUrl", properties.getWebWithdrawUrl());
		}
		long withdrawAmount = Math.round(amount);
		model.addAttribute("merchantNO", properties.getMerchantCode());
		model.addAttribute("cellphone", cellphone);
		String orderNO = OrderNOPrefixEnum.JZR.name()+idWorker.nextValue();
		model.addAttribute("orderNO", orderNO);
		model.addAttribute("amount", String.valueOf(withdrawAmount));
		String backUrl = properties.getWebUrl()+"/rest/customer/withdraw/back/url/"+platform;
		String notifyUrl = properties.getWebUrl()+"/rest/customer/withdraw/notify/url";
		model.addAttribute("backUrl", backUrl);
		model.addAttribute("notifyUrl", notifyUrl);
		String signatureStr=SecurityUtils.sign(String.valueOf(withdrawAmount)+VERTICAL_BAR+notifyUrl+VERTICAL_BAR+cellphone+VERTICAL_BAR+
				properties.getMerchantCode()+VERTICAL_BAR+orderNO+VERTICAL_BAR+backUrl);
		model.addAttribute("signature", signatureStr);
		LOGGER.info("withdraw params:" + url + VERTICAL_BAR + String.valueOf(withdrawAmount)+VERTICAL_BAR+notifyUrl+VERTICAL_BAR+cellphone+VERTICAL_BAR+
				properties.getMerchantCode()+VERTICAL_BAR+orderNO+VERTICAL_BAR+backUrl+VERTICAL_BAR+signatureStr);
		return "pay/withdraw";
	}
	
	@RequestMapping(value="/rest/customer/recharge/back/url/{platform}", method=RequestMethod.POST)
	public String rechargeResult(@PathVariable("platform")int platform, String resp_code, String resp_desc, String login_id, String mchnt_txn_ssn, int amt, String signature, Model model) throws Exception {
		model.addAttribute("amount", amt/100.0);
		model.addAttribute("code", resp_code);
		if (StringUtils.equals(ConstantUtil.SUCCESS, resp_code)) {
			CustomerBalance customerBalance = tradeService.getCustomerBalance(mchnt_txn_ssn);
			if (customerBalance == null) {
				Customer customer = customerService.getCustomer(login_id);
				if (customer != null && customer.getId() > 0) {
					customerBalance = new CustomerBalance();
					customerBalance.setAmount(amt);
					customerBalance.setCategory(PurposeEnum.RECHARGE.getType());
					customerBalance.setResponseCode(resp_code);
					customerBalance.setResponseDesc(PurposeEnum.RECHARGE.getPurpose());
					customerBalance.setCustomer(customer);
					customerBalance.setCreateTime(Calendar.getInstance().getTime());
					customerBalance.setSerialNO(mchnt_txn_ssn);
					tradeService.saveCustomerBalance(customerBalance);
				}
			}
		}
		if (platform == 0) {
			return "pay/recharge-result";
		} else {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/asset/my/1";
		}
	}
	
	@RequestMapping(value="/rest/customer/recharge/notify/url", method=RequestMethod.POST)
	public void rechargeAsynResult(String mobile_no, String mchnt_txn_ssn, int amt, String signature, HttpServletResponse response, Model model) throws Exception {
		CustomerBalance customerBalance = tradeService.getCustomerBalance(mchnt_txn_ssn);
		if (customerBalance == null) {
			Customer customer = customerService.getCustomer(mobile_no);
			if (customer != null && customer.getId() > 0) {
				customerBalance = new CustomerBalance();
				customerBalance.setAmount(amt);
				customerBalance.setCategory(PurposeEnum.RECHARGE.getType());
				customerBalance.setResponseCode(ConstantUtil.SUCCESS);
				customerBalance.setResponseDesc(PurposeEnum.RECHARGE.getPurpose());
				customerBalance.setCustomer(customer);
				customerBalance.setCreateTime(Calendar.getInstance().getTime());
				customerBalance.setSerialNO(mchnt_txn_ssn);
				tradeService.saveCustomerBalance(customerBalance);
			}
		}
		response.getWriter().write("success");
	}
	
	@RequestMapping(value="/rest/customer/withdraw/back/url/{platform}", method=RequestMethod.POST)
	public String withdrawResult(@PathVariable("platform")int platform, String resp_code, String resp_desc, String login_id, String mchnt_txn_ssn, int amt, String signature, Model model) throws Exception {
		model.addAttribute("amount", amt/100.0);
		model.addAttribute("code", resp_code);
		if (StringUtils.equals(ConstantUtil.SUCCESS, resp_code)) {
			CustomerBalance customerBalance = tradeService.getCustomerBalance(mchnt_txn_ssn);
			if (customerBalance == null) {
				Customer customer = customerService.getCustomer(login_id);
				if (customer != null && customer.getId() > 0) {
					customerBalance = new CustomerBalance();
					customerBalance.setAmount(amt);
					customerBalance.setCategory(PurposeEnum.WITHDRAW.getType());
					customerBalance.setResponseCode(resp_code);
					customerBalance.setResponseDesc(PurposeEnum.WITHDRAW.getPurpose());
					customerBalance.setCustomer(customer);
					customerBalance.setCreateTime(Calendar.getInstance().getTime());
					customerBalance.setSerialNO(mchnt_txn_ssn);
					tradeService.saveCustomerBalance(customerBalance);
				}
			}
		}
		if (platform == 0) {
			return "pay/withdraw-result";
		} else {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/asset/my/1";
		}
	}
	
	@RequestMapping(value="/rest/customer/withdraw/notify/url", method=RequestMethod.POST)
	public void withdrawAsynResult(String mobile_no, String mchnt_txn_ssn, int amt, String signature, HttpServletResponse response, Model model) throws Exception {
		CustomerBalance customerBalance = tradeService.getCustomerBalance(mchnt_txn_ssn);
		if (customerBalance == null) {
			Customer customer = customerService.getCustomer(mobile_no);
			if (customer != null && customer.getId() > 0) {
				customerBalance = new CustomerBalance();
				customerBalance.setAmount(amt);
				customerBalance.setCategory(PurposeEnum.WITHDRAW.getType());
				customerBalance.setResponseCode(ConstantUtil.SUCCESS);
				customerBalance.setResponseDesc(PurposeEnum.WITHDRAW.getPurpose());
				customerBalance.setCustomer(customer);
				customerBalance.setCreateTime(Calendar.getInstance().getTime());
				customerBalance.setSerialNO(mchnt_txn_ssn);
				tradeService.saveCustomerBalance(customerBalance);
			}
		}
		response.getWriter().write("success");
	}
}