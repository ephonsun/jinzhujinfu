package com.canary.finance.orm;

import java.util.List;

import com.canary.finance.domain.CouponRule;

public interface CouponRuleDao {
	List<CouponRule> queryForList();
	CouponRule selectById(int id);
	int insert(CouponRule rule);
	int update(CouponRule rule);
	CouponRule selectByCategory(int category);
}