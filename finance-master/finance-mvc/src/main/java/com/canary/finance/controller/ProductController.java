package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.DEFAULT_DECIMAL_FORMAT;
import static com.canary.finance.util.ConstantUtil.EMPTY;
import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.NUMBER_DATE_FORMAT;
import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.SUCCESS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.domain.Coupon;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.Product;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.OrderService;
import com.canary.finance.service.ProductService;

import io.jsonwebtoken.Claims;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@RequestMapping("/list")
	public String getProductList(Integer category, Integer start, Integer end, Integer page, Model model) {
		int size = 8;
		if(category == null) {
			category = 0;
		}
		int total = this.productService.getProductCount(category, start, end);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("products", this.productService.getProductList(category, start, end, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			model.addAttribute("total", 0);
			model.addAttribute("products", new Product[0]);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("systemTime", sdf.format(new Date()));
		model.addAttribute("categories", this.productService.getCategoryList(1));
		model.addAttribute("category", category);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "product/list";
	}
	
	@RequestMapping("/list/{base64}")
	public String forwardPorductList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getProductList(json.getInteger("category"), json.getInteger("start"), json.getInteger("end"), json.getInteger("page"), model); 
	}
	
	@RequestMapping("/detail/{productId:\\d+}")
	public String getProductDetail(@PathVariable("productId") int productId, HttpServletRequest request) {
		int total = this.orderService.getProductOrderCount(productId);
		if(total > 0) {
			request.setAttribute("total", total);
			request.setAttribute("orders", this.orderService.getPorductOrderList(productId, 0, 20));
		} else {
			request.setAttribute("total", 0);
			request.setAttribute("orders", new CustomerOrder[0]);
		}
		String cookieName = this.getMessage("cookie.login");
		request.setAttribute("isLogin", this.isLogin(request, cookieName));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("systemTime", sdf.format(new Date()));
		Product product = this.productService.getProduct(productId);
		request.setAttribute("product", this.productService.getProduct(productId));
		return "product/detail";
	}
	
	@RequestMapping(value="/{productId:\\d+}/order", method=RequestMethod.POST)
	public String getProductOrder(@PathVariable("productId")int productId, Integer page, Integer size, Model model) {
		int total = this.orderService.getProductOrderCount(productId);
		if(total > 0) {
			model.addAttribute("orders", this.orderService.getPorductOrderList(productId, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("orders", new CustomerOrder[0]);
		}
		return "content";
	}
	
	@RequestMapping("/buy/{productId:\\d+}")
	public String forwordPorductPurchase(@PathVariable("productId")int productId, int number, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		if(!this.isLogin(request, cookieName)) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/login";
		}
		Cookie cookie = this.getCookie(request, cookieName);
		Claims claims = this.jwtService.parseToken(cookie.getValue());
		Customer customer = this.customerService.getCustomer(claims.getId());
		if(customer != null && StringUtils.isNotBlank(customer.getIdcard())) {
			int sumPrincipal = this.orderService.getCustomerSumPrincipal(customer.getId());
			Product product = this.productService.getProduct(productId);
			int amount = number*product.getLowestMoney();
			List<CustomerCoupon> customerCoupons = this.customerService.getCustomerCouponList(customer.getId(), 1);
			
			request.setAttribute("customer", customer);
			request.setAttribute("product", product);
			request.setAttribute("coupons", this.getProductCoupon(product, amount, sumPrincipal, customerCoupons));
			request.setAttribute("amount", amount);
			request.setAttribute("portion", number);
			request.setAttribute("profit", this.getProductProfit(product, amount));
			double balance = 0.00; 
			String orderNO = OrderNOPrefixEnum.JZM.name()+idWorker.nextValue();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("cust_no", customer.getCellphone()));
			nvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
			String responseData = invokeHttp(properties.getRemotingUrl()+properties.getAccountBalanceUrl(), nvps);
			if (StringUtils.isNotBlank(responseData)) {
				try {
					XMLSerializer xmlSerializer = new XMLSerializer();
			        JSON json = xmlSerializer.read(responseData);
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
			request.setAttribute("balance", balance/100.0);
			return "/product/buy"; 
		} else {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/customer/"+customer.getId()+"/open";
		}
	}
	
	private String getProductProfit(Product product, int amount) {
		DecimalFormat decimalFormat = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);
		BigDecimal decimalAmount = new BigDecimal(Double.toString(amount*product.getFinancePeriod()*((product.getYearIncome()+product.getIncreaseInterest())*100)));
		double profit = decimalAmount.divide(new BigDecimal("3650000"), 2, RoundingMode.HALF_UP).doubleValue();
		return decimalFormat.format(profit);
	}
	
	private List<Coupon> getProductCoupon(Product product, int amount, int sumPrincipal, List<CustomerCoupon> customerCoupons) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(NUMBER_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		int currentDate = Integer.valueOf(dateFormat.format(calendar.getTime()));
		List<Coupon> coupons = new LinkedList<Coupon>();
		if(customerCoupons != null && customerCoupons.size() > 0) {
			for(CustomerCoupon customerCoupon : customerCoupons) {
				Coupon coupon = customerCoupon.getCoupon();
				if(coupon.getInvestAmount() > 0) {
					/* single usable */
					if(coupon.getCategory() == 1) {
						if(coupon.getInvestAmount() > amount) {
							continue;
						}
					/* sum usable */
					} else if(coupon.getCategory() == 2) {
						if(coupon.getInvestAmount() > sumPrincipal) {
							continue;
						}
					} else {
						//TODO donothing.
					}
					
				}
				
				if(coupon.getFinancePeriod() > product.getFinancePeriod()) {
					continue;
				}
				
				int expiryDate = Integer.valueOf(StringUtils.replace(coupon.getExpiryDate(), MINUS, EMPTY));
				if(currentDate > expiryDate) {
					continue;
				}
				
				coupons.add(coupon);
			}
		}
		
		return coupons;
	}
}
