package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.orm.CustomerDao;
import com.canary.finance.orm.CustomerOrderDao;
import com.canary.finance.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private CustomerOrderDao customerOrderDao;
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public List<CustomerOrder> getPorductOrderList(int productId, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerOrderDao.queryForList(params);
	}
	
	@Override
	public List<CustomerOrder> getPorductOrderList(int productId) {
		return this.customerOrderDao.selectByProductId(productId);
	}

	@Override
	public int getProductOrderCount(int productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		return this.customerOrderDao.queryForCount(params);
	}

	@Override
	public List<CustomerOrder> getCustomerOrderList(int customerId, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerOrderDao.queryForList(params);
	}

	@Override
	public int getCustomerOrderCount(int customerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		return this.customerOrderDao.queryForCount(params);
	}

	@Override
	public int getCustomerSumPrincipal(int customerId) {
		Integer sumPrincipal = this.customerOrderDao.selectSumPrincipalByCustomerId(customerId);
		return sumPrincipal == null ? 0 : sumPrincipal;
	}

	@Override
	public List<CustomerOrder> getCustomerProfitOrderList(int customerId, int offset, int size) {
		return this.customerOrderDao.selectProfitListByCustomerId(customerId, offset, size);
	}

	@Override
	public int getCustomerProfitOrderCount(int customerId) {
		return this.customerOrderDao.selectProfitCountByCustomerId(customerId);
	}

	@Override
	public List<CustomerOrder> getCustomerRefundOrderList(int customerId, int offset, int size) {
		return this.customerOrderDao.selectRefundListByCustomerId(customerId, offset, size);
	}

	@Override
	public int getCustomerRefundOrderCount(int customerId) {
		return this.customerOrderDao.selectRefundCountByCustomerId(customerId);
	}

	@Override
	public double getCustomerAccumulatedIncome(int customerId) {
		Double accumulatedIncome = this.customerOrderDao.selectAccumulatedIncomeByCustomerId(customerId);
		return accumulatedIncome == null ? 0.00d : accumulatedIncome;
	}

	@Override
	public double getCustomerSumAsset(int customerId) {
		double balance = 0.00d;
		Customer customer = this.customerDao.selectById(customerId);
		if(customer != null) {
			balance = customer.getBalance();
		}
		Double sumAsset = this.customerOrderDao.selectSumAssetByCustomerId(customerId);
		return sumAsset == null ? balance : (balance+sumAsset);
	}

	@Override
	public double getCustomerYestodayIncome(int customerId) {
		Double yestodayIncome = this.customerOrderDao.selectYestodayIncomeByCustomerId(customerId);
		return yestodayIncome == null ? 0.00d : yestodayIncome;
	}

}
