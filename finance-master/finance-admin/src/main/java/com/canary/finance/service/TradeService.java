package com.canary.finance.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.MerchantOrder;
import com.canary.finance.domain.Payment;
import com.canary.finance.pojo.ExportDataVO;
import com.canary.finance.pojo.OrderDTO;
import com.canary.finance.pojo.PaybackVO;

public interface TradeService {
	List<CustomerOrder> getCustomerOrderList(OrderDTO orderDTO, int offset, int size);
	int getCustomerOrderCount(OrderDTO orderDTO);
	CustomerOrder getCustomerOrder(int orderId);
	CustomerOrder getCustomerOrder(String orderNO);
	boolean saveCustomerOrder(CustomerOrder order);
	void saveCustomerOrderBatch(@Param("orders")List<CustomerOrder> orders);
	int getCustomerOrderTotalTradeAmount(OrderDTO orderDTO);
	double getCustomerOrderTotalPaybackAmount(OrderDTO orderDTO);
	
	List<CustomerBalance> getCustomerBalanceList(OrderDTO orderDTO, int offset, int size);
	int  getCustomerBalanceCount(OrderDTO orderDTO);
	CustomerBalance getCustomerBalance(int balanceId);
	CustomerBalance getCustomerBalance(String serialNO);
	boolean saveCustomerBalance(CustomerBalance balance);
	
	List<MerchantOrder> getMerchantOrderList(OrderDTO orderDTO, int offset, int size);
	int getMerchantOrderCount(OrderDTO orderDTO);
	MerchantOrder getMerantOrder(int orderId);
	MerchantOrder getMerantOrder(String orderNO);
	boolean saveMerantOrder(MerchantOrder order);
	int getMerchantOrderTotalTradeAmount();
	Double getMerchantOrderTotalPaybackAmount();
	
	List<ExportDataVO> listOrders(int productId);
	
	List<Payment> listPayments(int productId);
	Payment getPayment(String orderNO);
	boolean delete(String orderNO);
	boolean deleteBatch(List<Payment> payments);
	boolean paybackInBatch(List<CustomerOrder> orders);
	boolean saveBalanceLogInBatch(List<CustomerBalance> logs);
	boolean saveMessagesInBatch(List<CustomerMessage> messages);
	
	List<PaybackVO> listPaybackData(int productId);
	List<PaybackVO> listCouponData(int productId);
	List<PaybackVO> listIncreaseInterestData(int productId);
	boolean updateCouponStatus(String paybackNO, int couponStatus);
	boolean updateIncreaseInterestStatus(String paybackNO, int increaseInterestStatus);

}
