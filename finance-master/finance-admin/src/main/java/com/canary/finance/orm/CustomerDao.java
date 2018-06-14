package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Customer;

public interface CustomerDao {
	List<Customer> queryForList(Map<String,Object> params);
	int queryForCount(Map<String,Object> params);
	Customer selectById(int id);
	Customer selectByCellphone(String cellphone);
	Customer selectByIdcard(String idcard);
	int insert(Customer customer);
	int update(Customer customer);
}