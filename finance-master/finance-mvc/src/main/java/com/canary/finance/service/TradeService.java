package com.canary.finance.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.MerchantOrder;
import com.canary.finance.domain.Product;
import com.canary.finance.pojo.CustomerBalanceVO;
import com.canary.finance.pojo.CustomerOrderVO;
import com.canary.finance.pojo.OrderDTO;
import com.canary.finance.pojo.ProductOrderVO;

public interface TradeService {
	List<CustomerOrder> getCustomerOrderList(OrderDTO orderDTO, int offset, int size);
	int getCustomerOrderCount(OrderDTO orderDTO);
	int getCustomerOrderCount(int productId, int customerId);
	List<ProductOrderVO> getCustomerOrderList(int productId, int offset, int size);
	int getCustomerOrderCount(int productId);
	CustomerOrder getCustomerOrder(int orderId);
	CustomerOrder getCustomerOrder(String orderNO);
	boolean saveCustomerOrder(CustomerOrder order);
	void saveCustomerOrderBatch(@Param("orders")List<CustomerOrder> orders);
	
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
	
	List<CustomerOrderVO> getOrderForList(int customerId, int isPayback, int offset, int size);
	CustomerOrderVO getOrderDetail(String orderNO);
	int getOrderForCount(int customerId, int isPayback);
	List<CustomerBalanceVO> getCustomerBalanceList(int customerId, int type, int offset, int size);
	int  getCustomerBalanceCount(int customerId, int type);
	
	boolean saveOrder(Product product, Customer customer, String orderNO, int couponId, 
			int portion, int payType, String paybackNO, String contractNO);
}
