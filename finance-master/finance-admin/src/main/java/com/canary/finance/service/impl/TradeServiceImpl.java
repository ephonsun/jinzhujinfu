package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.MerchantOrder;
import com.canary.finance.domain.Payment;
import com.canary.finance.orm.CustomerBalanceDao;
import com.canary.finance.orm.CustomerOrderDao;
import com.canary.finance.orm.MerchantOrderDao;
import com.canary.finance.orm.PaymentDao;
import com.canary.finance.pojo.ExportDataVO;
import com.canary.finance.pojo.OrderDTO;
import com.canary.finance.pojo.PaybackVO;
import com.canary.finance.service.TradeService;

@Service
public class TradeServiceImpl implements TradeService {
	@Autowired
	private CustomerOrderDao customerOrderDao;
	@Autowired
	private MerchantOrderDao merchantOrderDao;
	@Autowired
	private CustomerBalanceDao customerBalanceDao;
	@Autowired
	private PaymentDao paymentDao;
	
	@Override
	public List<PaybackVO> listPaybackData(int productId) {
		if (productId > 0) {
			return customerOrderDao.queryPaybackForList(productId);
		}
		return null;
	}
	
	@Override
	public List<PaybackVO> listCouponData(int productId) {
		if (productId > 0) {
			return customerOrderDao.queryCouponForList(productId);
		}
		return null;
	}
	
	@Override
	public List<PaybackVO> listIncreaseInterestData(int productId) {
		if (productId > 0) {
			return customerOrderDao.queryIncreaseInterestForList(productId);
		}
		return null;
	}
		
		
	@Override
	public boolean updateCouponStatus(String paybackNO, int couponStatus) {
		return customerOrderDao.updateCouponStatus(paybackNO, couponStatus) > 0 ? true : false;
	}
	
	@Override
	public boolean updateIncreaseInterestStatus(String paybackNO, int increaseInterestStatus) {
		return customerOrderDao.updateIncreaseInterestStatus(paybackNO, increaseInterestStatus) > 0 ? true : false;
	}
	
	@Override
	public List<ExportDataVO> listOrders(int productId) {
		return customerOrderDao.selectByProductId(productId);
	}
	
	@Override
	public List<Payment> listPayments(int productId) {
		return paymentDao.selectByProductId(productId);
	}
	
	@Override
	public Payment getPayment(String orderNO) {
		if (StringUtils.isBlank(orderNO)) {
			return null;
		}
		return paymentDao.selectByOrderNO(orderNO);
	}
	
	@Override
	public boolean delete(String orderNO) {
		if (StringUtils.isBlank(orderNO)) {
			return false;
		}
		return paymentDao.delete(orderNO) > 0 ? true : false;
	}
	
	@Override
	public boolean deleteBatch(List<Payment> payments) {
		if (payments == null || payments.size() == 0) {
			return false;
		}
		return paymentDao.deleteBatch(payments) > 0 ? true : false;
	}
	
	@Override
	public boolean paybackInBatch(List<CustomerOrder> orders) {
		if (orders == null || orders.size() == 0) {
			return false;
		}
		return paymentDao.paybackInBatch(orders) > 0 ? true : false;
	}
	
	@Override
	public boolean saveBalanceLogInBatch(List<CustomerBalance> logs) {
		if (logs == null || logs.size() == 0) {
			return false;
		}
		return paymentDao.insertBalanceLogInBatch(logs) > 0 ? true : false;
	}
	
	@Override
	public boolean saveMessagesInBatch(List<CustomerMessage> messages) {
		if (messages == null || messages.size() == 0) {
			return false;
		}
		return paymentDao.batchInsertMessages(messages) > 0 ? true : false;
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
	public int getCustomerOrderTotalTradeAmount(OrderDTO orderDTO) {
		Integer tradeAmount = this.customerOrderDao.sumPrincipal(this.getParams(orderDTO, true));
		return tradeAmount == null ? 0 : tradeAmount;
	}
	
	@Override
	public double getCustomerOrderTotalPaybackAmount(OrderDTO orderDTO) {
		Double paybackAmount = this.customerOrderDao.sumPaybackAmount(this.getParams(orderDTO, true));
		return paybackAmount == null ? 0 : paybackAmount;
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
				if(orderDTO.getAmountFrom() != null && orderDTO.getAmountFrom() >= 0 && orderDTO.getAmountTo() >= orderDTO.getAmountFrom()) {
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
