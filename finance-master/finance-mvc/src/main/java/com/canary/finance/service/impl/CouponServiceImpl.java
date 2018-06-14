package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Coupon;
import com.canary.finance.domain.CouponRule;
import com.canary.finance.orm.CouponDao;
import com.canary.finance.orm.CouponRuleDao;
import com.canary.finance.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponRuleDao couponRuleDao;
	
	@Override
	public List<Coupon> getCouponList(String name, int category, int status, int offset, int size) {
		Map<String, Object> params = this.getParams(name, category, status);
		params.put("offset", offset);
		params.put("size", size);
		return this.couponDao.queryForList(params);
	}

	@Override
	public int getCouponCount(String name, int category, int status) {
		return this.couponDao.queryForCount(this.getParams(name, category, status));
	}

	@Override
	public List<Coupon> getCouponList(int status) {
		return this.couponDao.selectByStatus(status);
	}

	@Override
	public Coupon getCoupon(int couponId) {
		return this.couponDao.selectById(couponId);
	}

	@Override
	public Coupon getCoupon(String couponName) {
		return this.couponDao.selectByName(couponName);
	}

	@Override
	public boolean saveCoupon(Coupon coupon) {
		if(coupon != null && coupon.getId() > 0) {
			return this.couponDao.update(coupon)> 0 ? true : false;
		} else {
			return this.couponDao.insert(coupon)>0 ? true : false;
		}
	}

	@Override
	public List<CouponRule> getCouponRuleList() {
		return this.couponRuleDao.queryForList();
	}

	@Override
	public CouponRule getCouponRule(int ruleId) {
		return this.couponRuleDao.selectById(ruleId);
	}

	@Override
	public boolean saveCouponRule(CouponRule rule) {
		if(rule != null && rule.getId() > 0) {
			return this.couponRuleDao.update(rule)>0 ? true : false;
		} else {
			return this.couponRuleDao.insert(rule)>0 ? true : false;
		}
	}
	
	private Map<String, Object> getParams(String name, int category, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("category", category);
		params.put("status", status);
		return params;
	}
}
