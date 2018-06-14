package com.canary.finance.domain;

import java.io.Serializable;

public class CouponRule implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int category;
	private String couponIds;
	private String couponAmounts;
	private int status;
	
	public CouponRule(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}

	public String getCouponAmounts() {
		return couponAmounts;
	}

	public void setCouponAmounts(String couponAmounts) {
		this.couponAmounts = couponAmounts;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}