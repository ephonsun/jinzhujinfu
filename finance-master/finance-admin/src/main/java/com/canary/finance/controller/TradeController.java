package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.CODE;
import static com.canary.finance.util.ConstantUtil.COMMA;
import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.NUMBER_DATE_FORMAT;
import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.SUCCESS;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.MerchantOrder;
import com.canary.finance.enumeration.MessageSceneEnum;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.pojo.ExportDataVO;
import com.canary.finance.pojo.OrderDTO;
import com.canary.finance.pojo.PaybackOrderVO;
import com.canary.finance.pojo.PaybackVO;
import com.canary.finance.repo.SnowflakeDistributedIdRepository;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.OperationService;
import com.canary.finance.service.ProductService;
import com.canary.finance.service.TradeService;
import com.canary.finance.util.ExcelUtil;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping("/trade")
public class TradeController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);
	private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()*2, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	});
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				LOGGER.info("executor cleaning resources ......");
				if(EXECUTOR != null && !EXECUTOR.isShutdown()) {
					EXECUTOR.shutdown();
				}
			}
		});
	}
	@Autowired
	private TradeService tradeService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SnowflakeDistributedIdRepository idWorker;
	
	@RequestMapping("/investor/order")
	public String getCustomerOrderList(OrderDTO orderDTO, Integer page, Integer size, Model model) {
		int total = this.tradeService.getCustomerOrderCount(orderDTO);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("orders", this.tradeService.getCustomerOrderList(orderDTO, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("orders", new CustomerOrder[0]);
		}
		
		if(orderDTO.getOrderType() == 1) {
			model.addAttribute("tradeAmount", this.tradeService.getCustomerOrderTotalPaybackAmount(orderDTO));
		} else {
			model.addAttribute("tradeAmount", this.tradeService.getCustomerOrderTotalTradeAmount(orderDTO));
		}
		model.addAttribute("channels", this.operationService.getChannelList(1));
		model.addAttribute("amountFrom", orderDTO.getAmountFrom());
		model.addAttribute("amountTo", orderDTO.getAmountTo());
		model.addAttribute("channelId", orderDTO.getChannelId());
		model.addAttribute("orderType", orderDTO.getOrderType());
		model.addAttribute("name", StringUtils.trimToEmpty(orderDTO.getName()));
		model.addAttribute("productName", StringUtils.trimToEmpty(orderDTO.getProductName()));
		model.addAttribute("cellphone", StringUtils.trimToEmpty(orderDTO.getCellphone()));
		model.addAttribute("orderNO", StringUtils.trimToEmpty(orderDTO.getOrderNO()));
		model.addAttribute("payType", orderDTO.getPayType());
		model.addAttribute("beginTime", StringUtils.trimToEmpty(orderDTO.getBeginTime()));
		model.addAttribute("endTime", StringUtils.trimToEmpty(orderDTO.getEndTime()));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "trade/investor/order";
	}
	
	@RequestMapping("/investor/balance")
	public String getCustomerBalanceList(OrderDTO orderDTO, Integer page, Integer size, Model model) {
		int total = this.tradeService.getCustomerBalanceCount(orderDTO);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("balances", this.tradeService.getCustomerBalanceList(orderDTO, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("balances", new CustomerBalance[0]);
		}
		
		model.addAttribute("channels", this.operationService.getChannelList(1));
		model.addAttribute("amountFrom", orderDTO.getAmountFrom());
		model.addAttribute("amountTo", orderDTO.getAmountTo());
		model.addAttribute("channelId", orderDTO.getChannelId());
		model.addAttribute("name", StringUtils.trimToEmpty(orderDTO.getName()));
		model.addAttribute("cellphone", StringUtils.trimToEmpty(orderDTO.getCellphone()));
		model.addAttribute("orderNO", StringUtils.trimToEmpty(orderDTO.getOrderNO()));
		model.addAttribute("orderType", orderDTO.getOrderType());
		model.addAttribute("beginTime", StringUtils.trimToEmpty(orderDTO.getBeginTime()));
		model.addAttribute("endTime", StringUtils.trimToEmpty(orderDTO.getEndTime()));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "trade/investor/balance";
	}
	
	@PostMapping("/investor/balance/{balanceId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateCustomerBalance(@PathVariable("balanceId")int balanceId, @PathVariable("operate")int status) {
		CustomerBalance balance = this.tradeService.getCustomerBalance(balanceId);
		if(balance != null && balance.getId() > 0) {
			balance.setStatus(status);
			return this.tradeService.saveCustomerBalance(balance);
		}
		return false;
	}
	
	@RequestMapping("/financier/loan")
	public String getMerchantLoanOrderList(OrderDTO orderDTO,  Integer page, Integer status, Integer size, Model model) {
		if(orderDTO == null) { 
			orderDTO = new OrderDTO();
			orderDTO.setStatus(-1);
		}
		status = status == null ? -1 : status; 
		orderDTO.setStatus(status);
		orderDTO.setOrderType(0);
		orderDTO.setOrderNO(null);
		
		int total = this.productService.getLoanProductCount(orderDTO.getProductName(), orderDTO.getMerchantId(), 
				orderDTO.getStatus(), orderDTO.getBeginTime(), orderDTO.getEndTime());
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("orders", this.productService.getLoanProductList(orderDTO.getProductName(), orderDTO.getMerchantId(), 
				orderDTO.getStatus(), orderDTO.getBeginTime(), orderDTO.getEndTime(), this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("orders", new MerchantOrder[0]);
		}
		
		model.addAttribute("financiers", this.customerService.getMerchantList(1));
		model.addAttribute("merchantId", orderDTO.getMerchantId());
		model.addAttribute("status", orderDTO.getStatus());
		model.addAttribute("beginTime", StringUtils.trimToEmpty(orderDTO.getBeginTime()));
		model.addAttribute("endTime", StringUtils.trimToEmpty(orderDTO.getEndTime()));
		model.addAttribute("productName", StringUtils.trimToEmpty(orderDTO.getProductName()));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(0, size));
		return "trade/financier/loan";
	}
	
	@RequestMapping("/payback/export")
	@ResponseBody
	public ResponseEntity<byte[]> exportPaybackData(OrderDTO orderDTO, Integer status, HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("");
		realPath = "/data/export/";
		String header = "\u5e8f\u53f7.\u5546\u6237\u5e94\u8fd8\u91d1\u989d.\u4ea7\u54c1\u540d\u79f0.\u5546\u6237\u540d\u79f0.\u5230\u671f\u65e5\u671f.\u7ea2\u5305\u91d1\u989d.\u4ea7\u54c1\u52a0\u606f.\u72b6\u6001";
		List<PaybackOrderVO> orders = this.productService.getPaybackProductList(orderDTO.getProductName(), orderDTO.getMerchantId(), 
				orderDTO.getStatus(), orderDTO.getBeginTime(), orderDTO.getEndTime(), 0, 0);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		String errorMessage = "";
		String fileName = "";
		if(orders != null && orders.size()> 0) {
			String finalFileName = "";
			List<Serializable> list = new LinkedList<Serializable>();
			for(PaybackOrderVO order: orders) {
				list.add(order);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(NUMBER_DATE_FORMAT);
			fileName = "paybackOrder"  + "_" + dateFormat.format(Calendar.getInstance().getTime()) + ".xls";
			finalFileName = ExcelUtil.writeToExcel(list, header, realPath, fileName);
			
			if(StringUtils.isNotBlank(finalFileName)) {
				return this.export(realPath, fileName, request);
			}  
			errorMessage = "no file find!";
		}
		errorMessage = "this is empty!";
		headers.setContentDispositionFormData("attachment", fileName);
		return new ResponseEntity<byte[]>(errorMessage.getBytes(), headers, org.springframework.http.HttpStatus.OK); 
	}
	
	@RequestMapping("/payback")
	public String getPaybackOrderList(OrderDTO orderDTO, Integer status, Integer page, Integer size, Model model) {
		if(orderDTO == null) {
			orderDTO = new OrderDTO();
			orderDTO.setStatus(-1);
		}
		status = status == null ? -1 : status; 
		orderDTO.setStatus(status);
		orderDTO.setOrderType(0);
		orderDTO.setOrderNO(null);
		
		int total = this.productService.getPaybackProductCount(orderDTO.getProductName(), orderDTO.getMerchantId(), 
				orderDTO.getStatus(), orderDTO.getBeginTime(), orderDTO.getEndTime());
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("orders", this.productService.getPaybackProductList(orderDTO.getProductName(), orderDTO.getMerchantId(), 
					orderDTO.getStatus(), orderDTO.getBeginTime(), orderDTO.getEndTime(), this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("orders", new MerchantOrder[0]);
		}
		
		model.addAttribute("financiers", this.customerService.getMerchantList(1));
		model.addAttribute("merchantId", orderDTO.getMerchantId());
		model.addAttribute("status", orderDTO.getStatus());
		model.addAttribute("beginTime", StringUtils.trimToEmpty(orderDTO.getBeginTime()));
		model.addAttribute("endTime", StringUtils.trimToEmpty(orderDTO.getEndTime()));
		model.addAttribute("productName", StringUtils.trimToEmpty(orderDTO.getProductName()));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(0, size));
		return "trade/financier/payback";
	}
	
	@RequestMapping("/payback/{productId:\\d+}")
	@ResponseBody
	public JSONObject payback(@PathVariable("productId")int productId, HttpServletRequest request) {
		JSONObject result = new JSONObject();
		//List<Payment> paymentList = this.tradeService.listPayments(productId);
		long paybackAmouont = 0;
		String merchantAccount = "";
		/*for (Payment payment : paymentList) {
			if(StringUtils.isNotBlank(payment.getOrderNO()) && payment.getPayDetail() != null 
					&& StringUtils.equals(payment.getSign(), DigestUtils.md5Hex(DOMAIN+payment.getPayDetail()))) {
				JSONObject payDetail =  JSONObject.parseObject(payment.getPayDetail(), JSONObject.class);
				paybackAmouont = Math.round(payDetail.getDoubleValue("paybackAmount")*100);
				merchantAccount = payment.getAccount();
			}
		}*/
		List<PaybackVO> paybackVOs = this.tradeService.listPaybackData(productId);
		if (paybackVOs != null && paybackVOs.size() > 0) {
			for (PaybackVO vo : paybackVOs) {
				paybackAmouont += Math.round(vo.getPrincipal()*100);
				paybackAmouont += Math.round(vo.getProfit()*100);
				paybackAmouont += Math.round(vo.getServiceCharge()*100);
				merchantAccount = vo.getOutAccount();
			}
		}
		long balance = getBalance(merchantAccount);
    	if (balance >= paybackAmouont) {
    		if (productService.updatePayback(productId, 2)) {
    			EXECUTOR.submit(new Task(productId, tradeService, productService, merchantAccount, properties, paybackVOs));
    			result.put(CODE, 200);
    		} else {
    			result.put(CODE, 404);
    		}
    	} else {
    		result.put(CODE, 203);
    	}
		return result;
	}
	
	@RequestMapping("/loan/remark/{productId:\\d+}")
	@ResponseBody
	public boolean remark(@PathVariable("productId")int productId, HttpServletRequest request) {
		return productService.updatePayback(productId, 1);
	}
	
	@RequestMapping("/loan/export/{productId:\\d+}")
	@ResponseBody
	public ResponseEntity<byte[]> exportData(@PathVariable("productId")int productId, HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("");
		realPath = "/data/export/";
		String header = "\u5e8f\u53f7.\u4ed8\u6b3e\u65b9\u767b\u5f55\u540d.\u4ed8\u6b3e\u65b9\u4e2d\u6587\u540d\u79f0.\u4ed8\u6b3e\u8d44\u91d1\u6765\u81ea\u51bb\u7ed3.\u6536\u6b3e\u65b9\u767b\u5f55\u540d.\u6536\u6b3e\u65b9\u4e2d\u6587\u540d\u79f0.\u6536\u6b3e\u540e\u7acb\u5373\u51bb\u7ed3.\u4ea4\u6613\u91d1\u989d.\u5907\u6ce8\u4fe1\u606f.\u9884\u6388\u6743\u5408\u540c\u53f7.\u4ea4\u6613\u6d41\u6c34";
		List<ExportDataVO> orders = tradeService.listOrders(productId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		String errorMessage = "";
		String fileName = "";
		if(orders != null && orders.size()> 0) {
			String finalFileName = "";
			List<Serializable> list = new LinkedList<Serializable>();
			for(ExportDataVO order: orders) {
				list.add(order);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(NUMBER_DATE_FORMAT);
			fileName = "PW03_" + dateFormat.format(Calendar.getInstance().getTime()) + "_" + StringUtils.leftPad(String.valueOf(productId), 4, '0') + ".xls";
			finalFileName = ExcelUtil.writeToExcel(list, header, realPath, fileName);
			
			if(StringUtils.isNotBlank(finalFileName)) {
				return this.export(realPath, fileName, request);
			}  
			errorMessage = "no file find!";
		}
		errorMessage = "this is empty!";
		headers.setContentDispositionFormData("attachment", fileName);
		return new ResponseEntity<byte[]>(errorMessage.getBytes(), headers, org.springframework.http.HttpStatus.OK); 
	}
	
	@RequestMapping("/financier/order")
	public String getMerchantOrderList(OrderDTO orderDTO, Integer page, Integer size, Model model) {
		if(orderDTO == null) {
			orderDTO = new OrderDTO();
		}
		orderDTO.setStatus(-1);
		
		int total = this.tradeService.getMerchantOrderCount(orderDTO);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("orders", this.tradeService.getMerchantOrderList(orderDTO, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("orders", new MerchantOrder[0]);
		}
		
		if(orderDTO.getOrderType() == 1) {
			model.addAttribute("tradeAmount", this.tradeService.getMerchantOrderTotalTradeAmount());
		} else {
			model.addAttribute("tradeAmount", this.tradeService.getMerchantOrderTotalPaybackAmount());
		}
		model.addAttribute("orderType", orderDTO.getOrderType());
		model.addAttribute("financiers", this.customerService.getMerchantList(1));
		model.addAttribute("merchantId", orderDTO.getMerchantId());
		model.addAttribute("beginTime", StringUtils.trimToEmpty(orderDTO.getBeginTime()));
		model.addAttribute("endTime", StringUtils.trimToEmpty(orderDTO.getEndTime()));
		model.addAttribute("orderNO", StringUtils.trimToEmpty(orderDTO.getOrderNO()));
		model.addAttribute("productName", StringUtils.trimToEmpty(orderDTO.getProductName()));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(0, size));
		return "trade/financier/order";
	}
	
	@PostMapping("/financier/order/{orderId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateMerchantOrder(@PathVariable("orderId")int orderId, @PathVariable("operate")int status) {
		MerchantOrder order = this.tradeService.getMerantOrder(orderId);
		if(order != null && order.getId() > 0) {
			order.setStatus(status);
			return this.tradeService.saveMerantOrder(order);
		}
		return false;
	}
	
	public class Task implements Callable<String>{
		private int productId;
		private TradeService tradeService;
		private ProductService productService;
		private String merchantAccount;
		private List<PaybackVO> paybackVOs;
		private CanaryFinanceProperties properties;
		
		public Task(int productId, TradeService tradeService, ProductService productService, String merchantAccount, 
				CanaryFinanceProperties properties, List<PaybackVO> paybackVOs){
			this.productId = productId;
			this.tradeService = tradeService;
			this.productService = productService;
			this.merchantAccount = merchantAccount;
			this.properties = properties;
			this.paybackVOs = paybackVOs;
		}

		@Override
		public String call() {
			JSONObject result = new JSONObject();
			for (PaybackVO vo : paybackVOs) {
				if(StringUtils.isNotBlank(vo.getPaybackNO()) && vo.getPrincipal() > 0) {
					try {
						String payOrderNO = vo.getPaybackNO();
						List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
						payNvps.add(new BasicNameValuePair("outAccount", merchantAccount));
						payNvps.add(new BasicNameValuePair("inAccount", vo.getInAccount()));
						payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", payOrderNO));
						payNvps.add(new BasicNameValuePair("amt", String.valueOf(Math.round(vo.getPrincipal()*100+vo.getProfit()*100))));
						for (int times = 0; times < 5; times++) {
							String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getAllocateFundsUrl(), payNvps);
							LOGGER.info("the payment task payback params {}, payback result {} ......", payNvps, payResponse);
							if (StringUtils.isNotBlank(payResponse)) {
								try {
									XMLSerializer xmlSerializer = new XMLSerializer();
									JSON json = xmlSerializer.read(payResponse);
									if (json != null) {
										JSONObject respJSON = JSONObject.parseObject(json.toString());
										if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
											try {
												CustomerOrder order = new CustomerOrder();
												order.setPaybackNO(payOrderNO);
												order.setPaybackAmount(vo.getPaybackAmount());
												order.setPaybackTime(Calendar.getInstance().getTime());
												List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
												customerOrders.add(order);
												if (customerOrders != null && customerOrders.size() > 0) {
													tradeService.paybackInBatch(customerOrders);
												}
												
												CustomerBalance balanceLog = new CustomerBalance();
												balanceLog.setAmount(Integer.parseInt(String.valueOf(Math.round(vo.getPaybackAmount()*100))));
												balanceLog.setCategory(PurposeEnum.PAYBACK.getType());
												balanceLog.setResponseCode(SUCCESS);
												balanceLog.setResponseDesc(PurposeEnum.PAYBACK.getPurpose()+MINUS+vo.getProductName());
												Customer customer = new Customer();
												customer.setId(vo.getCustomerId());
												balanceLog.setCustomer(customer);
												balanceLog.setCreateTime(Calendar.getInstance().getTime());
												balanceLog.setSerialNO(payOrderNO);
												List<CustomerBalance> balanceLogs = new ArrayList<CustomerBalance>();
												balanceLogs.add(balanceLog);
												if (balanceLogs != null && balanceLogs.size() > 0) {
													tradeService.saveBalanceLogInBatch(balanceLogs);
												}
												
												CustomerMessage message = new CustomerMessage();
												message.setCustomerId(vo.getCustomerId());
												message.setEffectTime(Calendar.getInstance().getTime());
												message.setScene(MessageSceneEnum.PAYBACK_SUCCESS.getScene());
												message.setStatus(0);
												String msg = String.format(SmsPatternEnum.PAYBACK_SUCCESS.getPattern(), vo.getProductName(), 
														vo.getPaybackAmount());
												message.setMessage(msg);
												List<CustomerMessage> customerMessages = new ArrayList<CustomerMessage>();
												customerMessages.add(message);
												if (customerMessages != null && customerMessages.size() > 0) {
													tradeService.saveMessagesInBatch(customerMessages);
												}
												
												send(vo.getInAccount(), msg);
												TimeUnit.SECONDS.sleep(2);
												
												break;
											} catch(Exception e) {
												LOGGER.error("the payment task occured error: {}", e.getMessage());
											}
										} else {
											try {
												TimeUnit.SECONDS.sleep(2);
											} catch(InterruptedException e) {
												//do nothing.
											}
										}
									}
								} catch (Exception e) {
									LOGGER.error(e.getMessage());
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("the payment task occured error: {}", e.getMessage());
					}
				}
			}
			double serviceCharge = 0;
			String paybackNO = "";
			int successCount = 0;
			for (PaybackVO vo : paybackVOs) {
				serviceCharge += vo.getServiceCharge();
				paybackNO = vo.getPaybackNO();
			}
			try {
				List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
				payNvps.add(new BasicNameValuePair("outAccount", merchantAccount));
				payNvps.add(new BasicNameValuePair("inAccount", properties.getFeeAccount()));
				payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", "CHARGE"+paybackNO));
				payNvps.add(new BasicNameValuePair("amt", String.valueOf(Math.round(serviceCharge*100))));
				for (int times = 0; times < 5; times++) {
					String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getAllocateFundsUrl(), payNvps);
					LOGGER.info("the payment task payback params {}, payback result {} ......", payNvps, payResponse);
					if (StringUtils.isNotBlank(payResponse)) {
						try {
							XMLSerializer xmlSerializer = new XMLSerializer();
							JSON json = xmlSerializer.read(payResponse);
							if (json != null) {
								JSONObject respJSON = JSONObject.parseObject(json.toString());
								if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
									successCount ++;
								} else {
									try {
										TimeUnit.SECONDS.sleep(2);
									} catch(InterruptedException e) {
										//do nothing.
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage());
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("the payment task occured error: {}", e.getMessage());
			}
			if (successCount > 0) {
				productService.updatePayback(productId, 4);
			} else {
				productService.updatePayback(productId, 3);
			}
			List<PaybackVO> couponPaybackVos = tradeService.listCouponData(productId);
			if (couponPaybackVos != null && couponPaybackVos.size() > 0) {
				for (PaybackVO vo : couponPaybackVos) {
					if(StringUtils.isNotBlank(vo.getPaybackNO()) && vo.getProfit() > 0) {
						try {
							String payOrderNO = vo.getPaybackNO();
							List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
							payNvps.add(new BasicNameValuePair("outAccount", properties.getCouponAccount()));
							payNvps.add(new BasicNameValuePair("inAccount", vo.getInAccount()));
							payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", "COUPON"+payOrderNO));
							payNvps.add(new BasicNameValuePair("amt", String.valueOf(Math.round(vo.getProfit()*100))));
							for (int times = 0; times < 5; times++) {
								String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getAllocateFundsUrl(), payNvps);
								LOGGER.info("the payment task payback params {}, payback result {} ......", payNvps, payResponse);
								if (StringUtils.isNotBlank(payResponse)) {
									try {
										XMLSerializer xmlSerializer = new XMLSerializer();
										JSON json = xmlSerializer.read(payResponse);
										if (json != null) {
											JSONObject respJSON = JSONObject.parseObject(json.toString());
											if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
												try {
													CustomerBalance balanceLog = new CustomerBalance();
													balanceLog.setAmount(Integer.parseInt(String.valueOf(Math.round(vo.getPaybackAmount()*100))));
													balanceLog.setCategory(PurposeEnum.COUPON.getType());
													balanceLog.setResponseCode(SUCCESS);
													balanceLog.setResponseDesc(PurposeEnum.COUPON.getPurpose());
													Customer customer = new Customer();
													customer.setId(vo.getCustomerId());
													balanceLog.setCustomer(customer);
													balanceLog.setCreateTime(Calendar.getInstance().getTime());
													balanceLog.setSerialNO(payOrderNO);
													List<CustomerBalance> balanceLogs = new ArrayList<CustomerBalance>();
													balanceLogs.add(balanceLog);
													if (balanceLogs != null && balanceLogs.size() > 0) {
														tradeService.saveBalanceLogInBatch(balanceLogs);
													}
													tradeService.updateCouponStatus(paybackNO, 1);
													TimeUnit.SECONDS.sleep(2);
													break;
												} catch(Exception e) {
													LOGGER.error("the payment task occured error: {}", e.getMessage());
												}
											} else {
												try {
													TimeUnit.SECONDS.sleep(2);
												} catch(InterruptedException e) {
													//do nothing.
												}
											}
										}
									} catch (Exception e) {
										LOGGER.error(e.getMessage());
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error("the payment task occured error: {}", e.getMessage());
						}
					}
				}
			}
			List<PaybackVO> interestPaybackVos = tradeService.listIncreaseInterestData(productId);
			if (interestPaybackVos != null && interestPaybackVos.size() > 0) {
				for (PaybackVO vo : interestPaybackVos) {
					if(StringUtils.isNotBlank(vo.getPaybackNO()) && vo.getPrincipal() > 0) {
						try {
							String payOrderNO = vo.getPaybackNO();
							List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
							payNvps.add(new BasicNameValuePair("outAccount", properties.getCouponAccount()));
							payNvps.add(new BasicNameValuePair("inAccount", vo.getInAccount()));
							payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", "INTEREST"+payOrderNO));
							payNvps.add(new BasicNameValuePair("amt", String.valueOf(Math.round(vo.getProfit()*100))));
							for (int times = 0; times < 5; times++) {
								String payResponse = invokeHttp(properties.getRemotingUrl()+properties.getAllocateFundsUrl(), payNvps);
								LOGGER.info("the payment task payback params {}, payback result {} ......", payNvps, payResponse);
								if (StringUtils.isNotBlank(payResponse)) {
									try {
										XMLSerializer xmlSerializer = new XMLSerializer();
										JSON json = xmlSerializer.read(payResponse);
										if (json != null) {
											JSONObject respJSON = JSONObject.parseObject(json.toString());
											if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
												try {
													CustomerBalance balanceLog = new CustomerBalance();
													balanceLog.setAmount(Integer.parseInt(String.valueOf(Math.round(vo.getPaybackAmount()*100))));
													balanceLog.setCategory(PurposeEnum.FEE.getType());
													balanceLog.setResponseCode(SUCCESS);
													balanceLog.setResponseDesc(PurposeEnum.FEE.getPurpose());
													Customer customer = new Customer();
													customer.setId(vo.getCustomerId());
													balanceLog.setCustomer(customer);
													balanceLog.setCreateTime(Calendar.getInstance().getTime());
													balanceLog.setSerialNO(payOrderNO);
													List<CustomerBalance> balanceLogs = new ArrayList<CustomerBalance>();
													balanceLogs.add(balanceLog);
													if (balanceLogs != null && balanceLogs.size() > 0) {
														tradeService.saveBalanceLogInBatch(balanceLogs);
													}
													tradeService.updateIncreaseInterestStatus(paybackNO, 1);
													TimeUnit.SECONDS.sleep(2);
													break;
												} catch(Exception e) {
													LOGGER.error("the payment task occured error: {}", e.getMessage());
												}
											} else {
												try {
													TimeUnit.SECONDS.sleep(2);
												} catch(InterruptedException e) {
													//do nothing.
												}
											}
										}
									} catch (Exception e) {
										LOGGER.error(e.getMessage());
									}
								}
							}
						} catch (Exception e) {
							LOGGER.error("the payment task occured error: {}", e.getMessage());
						}
					}
				}
			}
			return result.toJSONString();
		}
	}
	
	public long getBalance(String accountId) {
		long balance = 0; 
		String orderNO;
		try {
			orderNO = OrderNOPrefixEnum.JZM.name()+idWorker.nextValue();
		} catch (Exception e) {
			orderNO = OrderNOPrefixEnum.JZM.name()+System.currentTimeMillis();
			LOGGER.error("the payment task get balance orderNO error: {}", e.getMessage());
		}
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("cust_no", accountId));
		nvps.add(new BasicNameValuePair("mchnt_txn_ssn", orderNO));
		String response = invokeHttp(properties.getRemotingUrl()+properties.getAccountBalanceUrl(), nvps);
		if (StringUtils.isNotBlank(response)) {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer();
		        JSON json = xmlSerializer.read(response);
		        if (json != null) {
		        	JSONObject respJSON = JSONObject.parseObject(json.toString());
		        	if (respJSON.containsKey(PLAIN) && StringUtils.equals(respJSON.getJSONObject(PLAIN).getString(RESP_CODE), SUCCESS)) {
		        		balance = respJSON.getJSONObject(PLAIN).getJSONObject("results").getJSONObject("result").getLongValue("ca_balance");
		        	}
		        }
			} catch (Exception e) {
				balance = 0;
			}
		}
		return balance;
	}
	
	public void send(String cellphone, String message) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String signature = properties.getHuaxingSignature();
		nvps.add(new BasicNameValuePair("content", signature + message));
		nvps.add(new BasicNameValuePair("mobile", cellphone));
		String response = invokeHttp(properties.getRemotingUrl()+properties.getHuaxingSmsUri(), nvps);
		
		if(StringUtils.isNotBlank(response)) { 
			JSONObject json = com.alibaba.fastjson.JSON.parseObject(response);
			if(StringUtils.equals(json.getString("returnstatus"), "Success")) {
				return;
			}
        } 
		List<NameValuePair> flowNvps = new ArrayList<NameValuePair>();
		try {
			flowNvps.add(new BasicNameValuePair("msg", URLEncoder.encode(message, UTF_8)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		flowNvps.add(new BasicNameValuePair("mobile", cellphone));
		String flowResponse = invokeHttp(properties.getRemotingUrl()+properties.getFlowSmsUrl(), nvps);
		if (StringUtils.isNotBlank(flowResponse)) {
			String[] infos = StringUtils.split(flowResponse, COMMA);
			if (infos != null && infos.length == 2 && StringUtils.startsWith(infos[1], "0")) {
				//success
			}
		}
	}
	
	protected static String invokeHttp(String url, List<NameValuePair> nvps) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			if(nvps != null && nvps.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
			}
			CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity(), UTF_8);
			} 
		} catch (Exception e) {
			LOGGER.error(url +" invoke error in BaseController invokeHttp."+e.getMessage());
		} finally {
			httpPost.releaseConnection();
		}
		return null;
	}
}
