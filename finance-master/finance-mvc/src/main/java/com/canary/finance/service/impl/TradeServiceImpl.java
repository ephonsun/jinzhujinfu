package com.canary.finance.service.impl;

import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.SUCCESS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.MerchantOrder;
import com.canary.finance.domain.Product;
import com.canary.finance.enumeration.MessageSceneEnum;
import com.canary.finance.enumeration.PurposeEnum;
import com.canary.finance.enumeration.SmsPatternEnum;
import com.canary.finance.orm.CustomerBalanceDao;
import com.canary.finance.orm.CustomerCouponDao;
import com.canary.finance.orm.CustomerDao;
import com.canary.finance.orm.CustomerMessageDao;
import com.canary.finance.orm.CustomerOrderDao;
import com.canary.finance.orm.MerchantOrderDao;
import com.canary.finance.orm.PaymentDao;
import com.canary.finance.orm.ProductDao;
import com.canary.finance.pojo.CustomerBalanceVO;
import com.canary.finance.pojo.CustomerOrderVO;
import com.canary.finance.pojo.OrderDTO;
import com.canary.finance.pojo.ProductOrderVO;
import com.canary.finance.service.TradeService;

@Service
public class TradeServiceImpl implements TradeService {
	private static final long TIMESTAMP = 1483200000;
	private static final int DEFAULT_SORTING = 999;
	@Autowired
	private CustomerOrderDao customerOrderDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CustomerMessageDao customerMessageDao;
	@Autowired
	private MerchantOrderDao merchantOrderDao;
	@Autowired
	private CustomerBalanceDao customerBalanceDao;
	@Autowired
	private CustomerCouponDao customerCouponDao;
	
	@Override
	public boolean saveOrder(Product product, Customer customer, String orderNO, int couponId, 
			int portion, int payType, String paybackNO, String contractNO) {
		if (product == null || customer == null || portion <= 0) {
			return false;
		}
		CustomerCoupon customerCoupon = null;
		if (couponId > 0) {
			customerCoupon = customerCouponDao.selectById(couponId);
		}
		if (customerCoupon != null && customerCoupon.getId() > 0 && StringUtils.isBlank(customerCoupon.getOrderNO())
			&& customerCoupon.getCoupon() != null && customerCoupon.getCoupon().getInvestAmount() <= product.getLowestMoney()*portion) {
		} else {
			customerCoupon = null;
		}
		CustomerOrder order = new CustomerOrder();
		if (customerCoupon != null && customerCoupon.getAmount() > 0) {
			order.setCouponAmount(customerCoupon.getAmount());
		}
		order.setCustomer(customer);
		order.setOrderNO(orderNO);
		order.setOrderTime(Calendar.getInstance().getTime());
		order.setProduct(product);
		order.setPrincipal(product.getLowestMoney()*portion);
		order.setPayType(payType);
		order.setPaybackNO(paybackNO);
		order.setContractNO(contractNO);
		if (customerOrderDao.insert(order) > 0 ? true : false) {
			if (customer.getTradeTime() == null || customer.getTradeTime().getTime() < TIMESTAMP) {
				customer.setTradeTime(Calendar.getInstance().getTime());
				customerDao.update(customer);
			}
			if (customerCoupon != null && customerCoupon.getId() > 0) {
				customerCoupon.setOrderNO(order.getOrderNO());
				customerCoupon.setUsedTime(Calendar.getInstance().getTime());
				customerCouponDao.update(customerCoupon);
			}
			addMessage(customer.getId(), product.getName(), order.getPrincipal());
			addCustomerBalance(customer, order.getOrderNO(), product.getName(), order.getPrincipal());
			product = productDao.selectById(product.getId());
			product.setActualAmount(product.getActualAmount() + order.getPrincipal());
			if (product.getActualAmount() >= product.getTotalAmount()) {
				Calendar interestDate = Calendar.getInstance();
				interestDate.add(Calendar.DAY_OF_YEAR, 1);
				product.setSorting(DEFAULT_SORTING);
				product.setInterestDate(interestDate.getTime());
				if (productDao.update(product) > 0) {
					List<CustomerOrder> orders = customerOrderDao.selectByProductId(product.getId());
					if (orders != null && orders.size() > 0) {
						List<CustomerMessage> messages = new ArrayList<CustomerMessage>();
 						for (CustomerOrder customerOrder : orders) {
 							if (customerOrder != null && customerOrder.getProduct() != null && customerOrder.getCustomer() != null) {
 								CustomerMessage message = new CustomerMessage();
 								message.setCustomerId(customerOrder.getCustomer().getId());
 								Calendar calendar = Calendar.getInstance();
 								calendar.set(Calendar.HOUR_OF_DAY, 1);
 								calendar.add(Calendar.DAY_OF_YEAR, 1);
 								message.setEffectTime(calendar.getTime());
 								message.setScene(MessageSceneEnum.PAY_SUCCESS.getScene());
 								message.setStatus(0);
 								String msg = String.format(SmsPatternEnum.PRODUCT_INTEREST.getPattern(), customerOrder.getProduct().getName());
 								message.setMessage(msg);
 								messages.add(message);
 							}
						}
 						if (messages != null && messages.size() > 0) {
 							customerMessageDao.batchInsert(messages);
 						}
 						/*List<Payment> payments = new ArrayList<Payment>();
 						SimpleDateFormat dateFormat = new SimpleDateFormat(NUMBER_DATE_FORMAT);
 						for (CustomerOrder customerOrder : orders) {
 							if (customerOrder != null && customerOrder.getProduct() != null && customerOrder.getCustomer() != null) {
 								Calendar calendar = Calendar.getInstance();
 								calendar.add(Calendar.DAY_OF_YEAR, 1);
 								calendar.add(Calendar.DAY_OF_YEAR, customerOrder.getProduct().getFinancePeriod());
 								Payment payment = new Payment();
 								payment.setCreateTime(Calendar.getInstance().getTime());
 								payment.setOrderNO(customerOrder.getPaybackNO());
 								JSONObject data = new JSONObject();
 								double paybackAmount = customerOrder.getPrincipal()+customerOrder.getCouponAmount()+
 										Math.round(customerOrder.getPrincipal()*customerOrder.getProduct().getFinancePeriod()*customerOrder.getProduct().getYearIncome()/365.0)/100.0;
 								data.put("paybackAmount", paybackAmount);
 								data.put("customerId", customer.getId());
 								data.put("productName", product.getName());
 								data.put("cellphone", customerOrder.getCustomer().getCellphone());
 								payment.setPayDetail(data.toJSONString());
 								if (product.getMerchant() != null) {
 									payment.setAccount(product.getMerchant().getCellphone());
 								}
 								payment.setProductId(product.getId());
 								payment.setSign(DigestUtils.md5Hex(DOMAIN+data.toJSONString()));
 								payment.setPayDate(dateFormat.format(calendar.getTime()));
 								payments.add(payment);
 							}
						}
 						if (payments != null && payments.size() > 0) {
 							paymentDao.batchInsert(payments);
 						}*/
					}
				}
			} else {
				productDao.update(product);
			}
			return true;
		}
		return false;
	}
	
	private boolean addMessage(int customerId, String productName, int principal) {
		CustomerMessage message = new CustomerMessage();
		message.setCustomerId(customerId);
		message.setEffectTime(Calendar.getInstance().getTime());
		message.setScene(MessageSceneEnum.PAY_SUCCESS.getScene());
		message.setStatus(0);
		String msg = String.format(SmsPatternEnum.PAY_SUCCESS.getPattern(), productName, principal);
		message.setMessage(msg);
		return customerMessageDao.insert(message) > 0 ? true : false;
	}
	
	private boolean addCustomerBalance(Customer customer, String orderNO, String productName, int principal) {
		CustomerBalance customerBalance = new CustomerBalance();
		customerBalance.setAmount(principal*100);
		customerBalance.setCategory(PurposeEnum.PAY_SUCCESS.getType());
		customerBalance.setResponseCode(SUCCESS);
		customerBalance.setResponseDesc(PurposeEnum.PAY_SUCCESS.getPurpose()+MINUS+productName);
		customerBalance.setCustomer(customer);
		customerBalance.setCreateTime(Calendar.getInstance().getTime());
		customerBalance.setSerialNO(orderNO);
		return customerBalanceDao.insert(customerBalance) > 0 ? true : false;
	}
	
	@Override
	public List<ProductOrderVO> getCustomerOrderList(int productId, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerOrderDao.queryVOForList(params);
	}

	@Override
	public int getCustomerOrderCount(int productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		return this.customerOrderDao.queryVOForCount(params);
	}
	
	@Override
	public List<CustomerBalanceVO> getCustomerBalanceList(int customerId, int type, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("type", type);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerBalanceDao.queryVOForList(params);
	}

	@Override
	public int getCustomerBalanceCount(int customerId, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("type", type);
		return this.customerBalanceDao.queryVOForCount(params);
	}
	
	@Override
	public List<CustomerOrderVO> getOrderForList(int customerId, int isPayback, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("isPayback", isPayback);
		params.put("offset", offset);
		params.put("size", size);
		return customerOrderDao.queryOrderForList(params);
	}
	
	@Override
	public CustomerOrderVO getOrderDetail(String orderNO) {
		return customerOrderDao.selectDetailByOrderNO(orderNO);
	}
	
	@Override
	public int getOrderForCount(int customerId, int isPayback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("isPayback", isPayback);
		return customerOrderDao.queryOrderForCount(params);
	}
	
	@Override
	public List<CustomerOrder> getCustomerOrderList(OrderDTO orderDTO, int offset, int size) {
		Map<String, Object> params = this.getParams(orderDTO, true);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerOrderDao.queryForList(params);
	}

	@Override
	public int getCustomerOrderCount(OrderDTO orderDTO) {
		return this.customerOrderDao.queryForCount(this.getParams(orderDTO, true));
	}
	
	@Override
	public int getCustomerOrderCount(int productId, int customerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("customerId", customerId);
		return this.customerOrderDao.queryForCount(params);
	}

	@Override
	public CustomerOrder getCustomerOrder(int orderId) {
		return this.customerOrderDao.selectById(orderId);
	}

	@Override
	public CustomerOrder getCustomerOrder(String orderNO) {
		return this.customerOrderDao.selectByOrderNO(orderNO);
	}

	@Override
	public boolean saveCustomerOrder(CustomerOrder order) {
		if(order != null && order.getId() > 0) {
			return this.customerOrderDao.update(order)>0 ? true : false;
		} else {
			return this.customerOrderDao.insert(order)>0 ? true : false;
		}
	}

	@Override
	public void saveCustomerOrderBatch(List<CustomerOrder> orders) {
		if(orders != null && orders.size() > 0) {
			this.customerOrderDao.updateBatch(orders);
		}
	}

	@Override
	public List<CustomerBalance> getCustomerBalanceList(OrderDTO orderDTO, int offset, int size) {
		Map<String, Object> params = this.getParams(orderDTO, true);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerBalanceDao.queryForList(params);
	}

	@Override
	public int getCustomerBalanceCount(OrderDTO orderDTO) {
		return this.customerBalanceDao.queryForCount(this.getParams(orderDTO, true));
	}

	@Override
	public CustomerBalance getCustomerBalance(int balanceId) {
		return this.customerBalanceDao.selectById(balanceId);
	}

	@Override
	public CustomerBalance getCustomerBalance(String serialNO) {
		return this.customerBalanceDao.selectBySerialNO(serialNO);
	}

	@Override
	public boolean saveCustomerBalance(CustomerBalance balance) {
		if(balance != null && balance.getId() > 0) {
			return this.customerBalanceDao.update(balance)>0 ? true : false;
		} else {
			return this.customerBalanceDao.insert(balance)>0 ? true : false;
		}
	}

	@Override
	public List<MerchantOrder> getMerchantOrderList(OrderDTO orderDTO, int offset, int size) {
		Map<String, Object> params = this.getParams(orderDTO, false);
		params.put("offset", offset);
		params.put("size", size);
		return this.merchantOrderDao.queryForList(params);
	}

	@Override
	public int getMerchantOrderCount(OrderDTO orderDTO) {
		return this.merchantOrderDao.queryForCount(this.getParams(orderDTO, false));
	}

	@Override
	public MerchantOrder getMerantOrder(int orderId) {
		return this.merchantOrderDao.selectById(orderId);
	}

	@Override
	public MerchantOrder getMerantOrder(String orderNO) {
		return this.merchantOrderDao.selectByOrderNO(orderNO);
	}

	@Override
	public boolean saveMerantOrder(MerchantOrder order) {
		if(order != null && order.getId() > 0) {
			return this.merchantOrderDao.update(order)>0 ? true : false;
		} else {
			return this.merchantOrderDao.insert(order)>0 ? true : false;
		}
	}

	@Override
	public int getMerchantOrderTotalTradeAmount() {
		Integer tradeAmount = this.merchantOrderDao.sumPrincipal();
		return tradeAmount == null ? 0 : tradeAmount;
	}

	@Override
	public Double getMerchantOrderTotalPaybackAmount() {
		Double paybackAmount = this.merchantOrderDao.sumPaybackAmount();
		return paybackAmount == null ? 0 : paybackAmount;
	}

	private Map<String, Object> getParams(OrderDTO orderDTO, boolean isCustomer) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(orderDTO != null) {
			params.put("orderType", orderDTO.getOrderType());
			if(StringUtils.isNotBlank(orderDTO.getBeginTime()) && StringUtils.isNotBlank(orderDTO.getEndTime())) {
				params.put("beginTime", orderDTO.getBeginTime());
				params.put("endTime", orderDTO.getEndTime());
			}
			if(StringUtils.isNotBlank(orderDTO.getOrderNO())) {
				params.put("orderNO", orderDTO.getOrderNO());
			}
			if(StringUtils.isNotBlank(orderDTO.getProductName())) {
				params.put("productName", orderDTO.getProductName());
			}
			params.put("status", orderDTO.getStatus());
			if(isCustomer) {
				params.put("payType", orderDTO.getPayType());
				if(orderDTO.getAmountFrom() != null && orderDTO.getAmountFrom() >= 0 && orderDTO.getAmountTo() > orderDTO.getAmountFrom()) {
					params.put("amountFrom", orderDTO.getAmountFrom());
					params.put("amountTo", orderDTO.getAmountTo());
				}
				params.put("channelId", orderDTO.getChannelId());
				if(StringUtils.isNotBlank(orderDTO.getName())) {
					params.put("name", orderDTO.getName());
				}
				if(StringUtils.isNotBlank(orderDTO.getCellphone())) {
					params.put("cellphone", orderDTO.getCellphone());
				}
			} else {
				params.put("merchantId", orderDTO.getMerchantId());
			}
		}
		return params;
	}
}
