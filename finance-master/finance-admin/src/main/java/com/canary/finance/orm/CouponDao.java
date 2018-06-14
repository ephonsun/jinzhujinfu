package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Coupon;

public interface CouponDao {
	List<Coupon> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	List<Coupon> selectByStatus(int status);
	Coupon selectById(int id);
	Coupon selectByName(String name);
	int insert(Coupon coupon);
	int update(Coupon coupon);
}