package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Coupon;
import com.canary.finance.domain.CouponRule;

public interface CouponService {
	List<Coupon> getCouponList(String name, int category, int status, int offset, int size);
	int getCouponCount(String name, int category, int status);
	List<Coupon> getCouponList(int status);
	Coupon getCoupon(int couponId);
	Coupon getCoupon(String couponName);
	boolean saveCoupon(Coupon coupon);
	
	List<CouponRule> getCouponRuleList();
	CouponRule getCouponRule(int ruleId);
	boolean saveCouponRule(CouponRule rule);
}
