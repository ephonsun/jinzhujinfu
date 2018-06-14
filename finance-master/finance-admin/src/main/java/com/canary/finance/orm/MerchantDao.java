package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Merchant;

public interface MerchantDao {
	List<Merchant> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	List<Merchant> selectByStatus(int status);
	Merchant selectById(int id);
	Merchant selectByLicense(String license);
	int insert(Merchant merchant);
	int update(Merchant merchant);
}