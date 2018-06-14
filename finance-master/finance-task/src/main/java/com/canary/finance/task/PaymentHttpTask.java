package com.canary.finance.task;

import static com.canary.finance.util.ConstantUtil.CODE;
import static com.canary.finance.util.ConstantUtil.DOMAIN;
import static com.canary.finance.util.ConstantUtil.MSG;
import static com.canary.finance.util.ConstantUtil.NUMBER_DATETIME_FORMAT;
import static com.canary.finance.util.ConstantUtil.DEFAULT_DECIMAL_FORMAT;
import static com.canary.finance.util.ConstantUtil.SEMICOLON;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.domain.Payment;
import com.canary.finance.orm.PaymentDao;
import com.canary.finance.pojo.PaymentDTO;
import com.canary.finance.util.DigestSignUtil;
import com.lianlianpay.security.utils.LianLianPaySecurity;

public class PaymentHttpTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentHttpTask.class);
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
				LOGGER.info("the payment task service cleaning resources ......");
				if(EXECUTOR != null && !EXECUTOR.isShutdown()) {
					EXECUTOR.shutdown();
				}
			}
		});
	}
	private final PaymentDao paymentDao;
	private final CanaryFinanceProperties properties;
	
	public PaymentHttpTask(PaymentDao paymentDao, CanaryFinanceProperties properties) {
		this.paymentDao = paymentDao;
		this.properties = properties;
	}
	
	public void execute() {
		List<Payment> paymentList = this.paymentDao.selectByDate(this.properties.getInstantpayDate());
		if(paymentList != null && paymentList.size() > 0) {
			LOGGER.info("the payment task service for {}, running for {} sub tasks ......", this.properties.getInstantpayDate(), paymentList.size());
			CountDownLatch latch = new CountDownLatch(paymentList.size());
			for(Payment payment : paymentList) {
				EXECUTOR.submit(new Task(latch, payment));
			}
			
			//waiting for all task done.
			try {
				latch.await();
			} catch(InterruptedException e) {
				LOGGER.error("the payment task service's CountDownLatch timeout[{}].", e.getMessage());
			} finally {
				LOGGER.info("the payment task service completed, excuted {} sub task.",  paymentList.size());
			}
		} else {
			LOGGER.info("the payment task service completed, do nothing.");
		}
	}
	
	public class Task implements Callable<String>{
		private final CountDownLatch latch;
		private final Payment payment;
		
		public Task(CountDownLatch latch, Payment payment){
			this.latch = latch;
			this.payment = payment;
		}

		@Override
		public String call() {
			JSONObject result = new JSONObject();
			try {
				if(this.payment != null && this.payment.getPayDetail() != null 
						&& StringUtils.equals(this.payment.getSign(), DigestUtils.md5Hex(DOMAIN+payment.getPayDetail()))) {
					PaymentDTO paymentDTO =  JSONObject.parseObject(this.payment.getPayDetail(), PaymentDTO.class);
					if(paymentDTO != null && paymentDTO.getOrderNo() != null && paymentDTO.getCustomerName() != null 
							&& paymentDTO.getBankCard() != null && paymentDTO.getBankCode() != null) {
						try {
							result = this.invokeHttp(properties.getInstantpay(), this.getPaymentParameter(paymentDTO));
							
						} catch (Exception e) {
							LOGGER.error("the payment task occured error: {}", e.getMessage());
						}
						
					} else {
						LOGGER.error("payment[{}] has not necessary parameter[order number or bank information] .", this.payment.getOrderNO());
					}
				} else {
					LOGGER.error("payment[{}] sign[{}] invalid .", this.payment.getOrderNO(), this.payment.getSign());
				}
			} finally {
				latch.countDown();
			}
			return result.toJSONString();
		}
		
		private JSONObject getPaymentParameter(PaymentDTO payment) throws Exception {
			JSONObject parameter = new JSONObject();
			DecimalFormat df = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);
			String infoOrder = payment.getInfoOrder();
			if(StringUtils.isNotBlank(infoOrder)) {
				infoOrder = "\\u5230\\u671f\\u56de\\u6b3e"+SEMICOLON+infoOrder;
			} else {
				infoOrder = payment.getProductName()+"\\u5230\\u671f\\u56de\\u6b3e";
			}

			parameter.put("oid_partner", "201507071000400502");
			parameter.put("platform", "silverfox-cn.com");
			parameter.put("api_version", "1.0");
			parameter.put("sign_type", "RSA");
			
			parameter.put("no_order", payment.getOrderNo());
			parameter.put("dt_order", DateFormatUtils.format(Calendar.getInstance(), NUMBER_DATETIME_FORMAT));
			parameter.put("money_order", df.format(payment.getAmount()));
			parameter.put("card_no", payment.getBankCard());
			parameter.put("acct_name", payment.getCustomerName());
			parameter.put("bank_name", payment.getBankName());
			parameter.put("info_order", payment.getInfoOrder());
			parameter.put("flag_card", "0");
			parameter.put("notify_url", "https://www.silverfox-cn.com/w/pri/payback/result/async");
			
			parameter.put("sign", DigestSignUtil.sign(parameter, properties.getOurPrivateKey() , StringUtils.reverse("9af8d5221814e5d8dc31f37241007b76")));
			
			return parameter;
		}
		
		private JSONObject invokeHttp(String url, JSONObject parameter) throws Exception {
			JSONObject result = new JSONObject();
			CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			try {
				JSONObject param = new JSONObject();
				param.put("oid_partner", "201507071000400502");
				param.put("pay_load", LianLianPaySecurity.encrypt(parameter.toJSONString(), properties.getLianlianPublicKey()));
				httpPost.setEntity(new StringEntity(param.toJSONString(), UTF_8));
				LOGGER.info("http invoker request orignal parameter: {}, encrypted parameter {}.", parameter.toJSONString(), param.toJSONString());
				CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
				if(httpResponse != null && httpResponse.getStatusLine() != null) {
					if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						result = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity(), UTF_8));
						LOGGER.info("http invoker response result: {}.", result.toJSONString());
					} else {
						LOGGER.error("the invoker request for {}, status code[{}], and response result: {}.", url, httpResponse.getStatusLine().getStatusCode(), httpResponse);
						result.put(CODE, httpResponse.getStatusLine().getStatusCode());
						result.put(MSG, httpResponse.getStatusLine());
					}
				} else {
					LOGGER.error("the invoker request for {}, and response result: Service Unavailable.", url);
					result.put(CODE, HttpStatus.SC_SERVICE_UNAVAILABLE);
					result.put(MSG, "Service Unavailable");
				}
			} finally {
				httpPost.releaseConnection();
			}
			
			return result;
		}
	}
}