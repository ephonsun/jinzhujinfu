package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.CustomerBalance;

public interface CustomerBalanceDao {
	List<CustomerBalance> queryForList(Map<String,Object> params);
	int queryForCount(Map<String,Object> params);
	CustomerBalance selectById(int id);
	CustomerBalance selectBySerialNO(String serialNO);
	int insert(CustomerBalance balance);
	int update(CustomerBalance balance);
}