package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.MerchantOrder;

public interface MerchantOrderDao {
	List<MerchantOrder> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	MerchantOrder selectById(int id);
	MerchantOrder selectByOrderNO(String orderNO);
	int insert(MerchantOrder order);
	int update(MerchantOrder order);
	Integer sumPrincipal();
	Double sumPaybackAmount();
}