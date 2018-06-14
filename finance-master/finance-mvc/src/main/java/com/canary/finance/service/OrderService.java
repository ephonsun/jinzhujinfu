package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.CustomerOrder;

public interface OrderService {
	List<CustomerOrder> getPorductOrderList(int productId, int offset, int size);
	List<CustomerOrder> getPorductOrderList(int productId);
	int getProductOrderCount(int productId);
	List<CustomerOrder> getCustomerOrderList(int customerId, int offset, int size);
	List<CustomerOrder> getCustomerProfitOrderList(int customerId, int offset, int size);
	int getCustomerProfitOrderCount(int customerId);
	List<CustomerOrder> getCustomerRefundOrderList(int customerId, int offset, int size);
	int getCustomerRefundOrderCount(int customerId);
	int getCustomerOrderCount(int customerId);
	int getCustomerSumPrincipal(int customerId);
	double getCustomerAccumulatedIncome(int customerId);
	double getCustomerSumAsset(int customerId);
	double getCustomerYestodayIncome(int customerId);
}
