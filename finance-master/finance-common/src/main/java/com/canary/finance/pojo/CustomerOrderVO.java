package com.canary.finance.pojo;

import java.io.Serializable;

public class CustomerOrderVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderNO;
	private double profit;
	private int principal;
	private double payBackAmount;
	private int productId;
	private String productName;
	private int remainingDays;
	private String paybackDate;
	private String orderTime;
	private String interestDate;
	private int couponAmount;
	private int financePeriod;
	
	public CustomerOrderVO() {
		
	}
	
	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	
	public int getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public int getFinancePeriod() {
		return financePeriod;
	}

	public void setFinancePeriod(int financePeriod) {
		this.financePeriod = financePeriod;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public int getPrincipal() {
		return principal;
	}

	public void setPrincipal(int principal) {
		this.principal = principal;
	}

	public double getPayBackAmount() {
		return payBackAmount;
	}

	public void setPayBackAmount(double payBackAmount) {
		this.payBackAmount = payBackAmount;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(int remainingDays) {
		this.remainingDays = remainingDays;
	}

	public String getPaybackDate() {
		return paybackDate;
	}

	public void setPaybackDate(String paybackDate) {
		this.paybackDate = paybackDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
	}
}