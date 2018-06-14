package com.canary.finance.task;

import static com.canary.finance.util.ConstantUtil.COMMA;
import static com.canary.finance.util.ConstantUtil.DOMAIN;
import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.COLON;
import static com.canary.finance.util.ConstantUtil.PLAIN;
import static com.canary.finance.util.ConstantUtil.RESP_CODE;
import static com.canary.finance.util.ConstantUtil.SUCCESS;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.Payment;
import com.canary.finance.enumeration.MessageSceneEnum;
import com.canary.finance.enumeration.OrderNOPrefixEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.orm.PaymentDao;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

public class PaymentHttpsTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentHttpsTask.class);
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
	
	public PaymentHttpsTask(PaymentDao paymentDao, CanaryFinanceProperties properties) {
		this.paymentDao = paymentDao;
		this.properties = properties;
	}
	
	public void execute() {
		List<Payment> paymentList = this.paymentDao.selectByDate(this.properties.getInstantpayDate());
		
		if(paymentList != null && paymentList.size() > 0) {
			LOGGER.info("the payment task service for {}, running for {} sub tasks ......", this.properties.getInstantpayDate(), paymentList.size());
			Map<String, Long> data = new HashMap<String, Long>();
			for (Payment payment : paymentList) {
				if(StringUtils.isNotBlank(payment.getOrderNO()) && payment.getPayDetail() != null 
						&& StringUtils.equals(payment.getSign(), DigestUtils.md5Hex(DOMAIN+payment.getPayDetail()))) {
					JSONObject payDetail =  JSONObject.parseObject(payment.getPayDetail(), JSONObject.class);
					Long amount = data.get(payment.getProductId()+COLON+payment.getCardNO());
					if (amount == null) {
						data.put(payment.getProductId()+COLON+payment.getAccount(), Math.round(payDetail.getDoubleValue("paybackAmount")*100));
					} else {
						data.put(payment.getProductId()+COLON+payment.getAccount(), Math.round(payDetail.getDoubleValue("paybackAmount")*100)+amount);
					}
				}
			}
			CountDownLatch latch = new CountDownLatch(data.size());
			for (String key : data.keySet()) {  
			    String[] info = StringUtils.split(key, COLON);
			    if (info != null && info.length == 2) {
			    	Long value = data.get(key);  
			    	int productId = Integer.parseInt(info[0]);
			    	String merchantAccount = info[1];
			    	long balance = getBalance(merchantAccount);
			    	if (balance >= value) {
			    		EXECUTOR.submit(new Task(latch, productId, this.properties.getInstantpayDate(), paymentDao, merchantAccount));
						try {
							TimeUnit.MINUTES.sleep(3);
						} catch(InterruptedException e) {
							//do nothing.
						}
			    	} else {
			    		latch.countDown();
			    	}
			    }
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
		private int productId;
		private String payDate;
		private PaymentDao paymentDao;
		private String merchantAccount;
		
		public Task(CountDownLatch latch, int productId, String payDate, PaymentDao paymentDao, String merchantAccount){
			this.latch = latch;
			this.productId = productId;
			this.payDate = payDate;
			this.paymentDao = paymentDao;
			this.merchantAccount = merchantAccount;
		}

		@Override
		public String call() {
			JSONObject result = new JSONObject();
			ConcurrentSkipListSet<Payment> payments = new ConcurrentSkipListSet<Payment>(new Comparator<Payment>() {
				@Override
				public int compare(Payment paymentBase, Payment paymentTo) {
					if (paymentBase != null && StringUtils.isNotBlank(paymentBase.getOrderNO()) 
							&& paymentTo != null && StringUtils.isNotBlank(paymentTo.getOrderNO())) {
						return (paymentBase.getOrderNO().compareToIgnoreCase(paymentTo.getOrderNO()));
					}
					return 1;
				}
			});
			ConcurrentSkipListSet<CustomerOrder> orders = new ConcurrentSkipListSet<CustomerOrder>(new Comparator<CustomerOrder>() {
				@Override
				public int compare(CustomerOrder orderBase, CustomerOrder orderTo) {
					return 1;
				}
			});
			ConcurrentSkipListSet<CustomerBalance> balanceLogs = new ConcurrentSkipListSet<CustomerBalance>(new Comparator<CustomerBalance>() {
				@Override
				public int compare(CustomerBalance balanceBase, CustomerBalance balanceTo) {
					return 1;
				}
			});
			ConcurrentSkipListSet<CustomerMessage> messages = new ConcurrentSkipListSet<CustomerMessage>(new Comparator<CustomerMessage>() {
				@Override
				public int compare(CustomerMessage messageBase, CustomerMessage messageTo) {
					return 1;
				}
			});
			try {
				List<Payment> paymentList = this.paymentDao.selectByDateAndProductId(payDate, productId);
				for (Payment payment : paymentList) {
					if(payment != null && StringUtils.isNotBlank(payment.getOrderNO()) && payment.getPayDetail() != null 
							&& StringUtils.equals(payment.getSign(), DigestUtils.md5Hex(DOMAIN+payment.getPayDetail()))) {
						JSONObject data =  JSONObject.parseObject(payment.getPayDetail(), JSONObject.class);
						if(data != null && data.containsKey("paybackAmount") && data.containsKey("cellphone")) {
							try {
								String payOrderNO = payment.getOrderNO();
								List<NameValuePair> payNvps = new ArrayList<NameValuePair>();
								payNvps.add(new BasicNameValuePair("outAccount", merchantAccount));
								payNvps.add(new BasicNameValuePair("inAccount", data.getString("cellphone")));
								payNvps.add(new BasicNameValuePair("mchnt_txn_ssn", payOrderNO));
								payNvps.add(new BasicNameValuePair("amt", String.valueOf(Math.round(data.getDoubleValue("paybackAmount")*100))));
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
														payments.add(payment);
														CustomerOrder order = new CustomerOrder();
														order.setPaybackNO(payOrderNO);
														order.setPaybackAmount(data.getDoubleValue("paybackAmount"));
														order.setPaybackTime(Calendar.getInstance().getTime());
														orders.add(order);
														//{"paybackAmount":201.92,"customerId":2,"cellphone":"13094818226","productName":"体验专享1"}
														CustomerBalance balanceLog = new CustomerBalance();
														balanceLog.setAmount(Integer.parseInt(String.valueOf(Math.round(data.getDoubleValue("paybackAmount")*100))));
														balanceLog.setCategory(PurposeEnum.PAYBACK.getType());
														balanceLog.setResponseCode(SUCCESS);
														balanceLog.setResponseDesc(PurposeEnum.PAYBACK.getPurpose()+MINUS+data.getString("productName"));
														Customer customer = new Customer();
														customer.setId(data.getInteger("customerId"));
														balanceLog.setCustomer(customer);
														balanceLog.setCreateTime(Calendar.getInstance().getTime());
														balanceLog.setSerialNO(payOrderNO);
														balanceLogs.add(balanceLog);
													
														CustomerMessage message = new CustomerMessage();
														message.setCustomerId(data.getInteger("customerId"));
														message.setEffectTime(Calendar.getInstance().getTime());
														message.setScene(MessageSceneEnum.PAYBACK_SUCCESS.getScene());
														message.setStatus(0);
														String msg = String.format(SmsPatternEnum.PAYBACK_SUCCESS.getPattern(), data.getString("productName"), data.getDoubleValue("paybackAmount"));
														message.setMessage(msg);
														messages.add(message);
														send(data.getString("cellphone"), msg);
														TimeUnit.SECONDS.sleep(1);
														
														break;
													} catch(InterruptedException e) {
														//do nothing.
													}
												} else {
													try {
														TimeUnit.SECONDS.sleep(1);
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
						} else {
							LOGGER.error("payment[{}] has not necessary parameter[order number or bank information] .", payment.getOrderNO());
						}
					} else {
						LOGGER.error("payment[{}] sign[{}] invalid .", payment.getOrderNO(), payment.getSign());
					}
				}
			} finally {
				if (payments != null && payments.size() > 0) {
					List<Payment> logs = new ArrayList<Payment>();
					for (Payment log : payments) {
						logs.add(log);
					}
					paymentDao.deleteBatch(logs);
				}
				if (orders != null && orders.size() > 0) {
					List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
					for (CustomerOrder order : orders) {
						customerOrders.add(order);
					}
					paymentDao.paybackInBatch(customerOrders);
				}
				if (balanceLogs != null && balanceLogs.size() > 0) {
					List<CustomerBalance> logs = new ArrayList<CustomerBalance>();
					for (CustomerBalance log : balanceLogs) {
						logs.add(log);
					}
					paymentDao.insertBalanceLogInBatch(logs);
				}
				if (messages != null && messages.size() > 0) {
					List<CustomerMessage> customerMessages = new ArrayList<CustomerMessage>();
					for (CustomerMessage message : messages) {
						customerMessages.add(message);
					}
					paymentDao.batchInsertMessages(customerMessages);
				}
				latch.countDown();
			}
			return result.toJSONString();
		}
	}
	
	public long getBalance(String accountId) {
		long balance = 0; 
		String orderNO;
		try {
			orderNO = OrderNOPrefixEnum.JZM.name()+new SnowflakeDistributedIdRepository().nextValue();
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
	
	public class SnowflakeDistributedIdRepository {
		private final static long TWEPOCH = 1288834974657L;
		private final static long DATA_CENTER_ID_BITS = 5L;
	    private final static long WORKER_ID_BITS = 5L;
	    private final static long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
	    private final static long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
	    private final static long SEQUENCE_BITS = 12L;
	    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
	    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
	    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
	    private final static long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);  
	    private final long workerId;
	    private final long dataCenterId;
	    private long lastTimestamp = -1L; 
	    private long sequence = 0L;
	    
	    public SnowflakeDistributedIdRepository() {
	    	this(1, 1);
	    }
	    
	    public SnowflakeDistributedIdRepository(long workerId, long dataCenterId) {
	    	if (workerId > MAX_WORKER_ID || workerId < 0) { 
	            throw new IllegalArgumentException(String.format("%s must range from %d to %d", workerId, 0, MAX_WORKER_ID));  
	        }
	        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
	            throw new IllegalArgumentException(String.format("%s must range from %d to %d", dataCenterId, 0, MAX_DATA_CENTER_ID));  
	        }

	        this.workerId = workerId;
	        this.dataCenterId = dataCenterId;
	    }
	    
	    public synchronized long nextValue() throws Exception {
	    	long timestamp = currentTimeMillis();
	        if(timestamp < this.lastTimestamp) {
	        	LOGGER.error(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp-timestamp)));
	            throw new Exception(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", (this.lastTimestamp-timestamp)));
	        }  

	        if(this.lastTimestamp == timestamp) {
	            this.sequence = (this.sequence+1) & SEQUENCE_MASK;
	            if (sequence == 0) {
	                timestamp = tilNextMillis(this.lastTimestamp);
	            }
	        } else {
	            this.sequence = 0;
	        }
	        this.lastTimestamp = timestamp;
	          
	        // 64 Bit ID (42(Millis)+5(Data Center ID)+5(Machine ID)+12(Repeat Sequence Summation))
	        long nextId = ((timestamp-TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
	                | (this.dataCenterId << DATA_CENTER_ID_SHIFT)
	                | (this.workerId << WORKER_ID_SHIFT) | this.sequence;

	        return nextId;  
	    }
	 
	    private long tilNextMillis(long lastTimestamp) {
	        long timestamp = this.currentTimeMillis();
	        while (timestamp <= lastTimestamp) {
	            timestamp = this.currentTimeMillis();
	        }
	        return timestamp;
	    }
	 
	    private long currentTimeMillis() {
	        return System.currentTimeMillis();
	    }
	}
}