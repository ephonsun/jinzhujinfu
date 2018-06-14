package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class CustomerCoupon implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int customerId;
	private int invitorId;
	private Coupon coupon;
	private double amount;
	private Date createTime;
	private Date usedTime;
	private String orderNO;
	
	public CustomerCoupon() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getInvitorId() {
		return invitorId;
	}

	public void setInvitorId(int invitorId) {
		this.invitorId = invitorId;
	}
}