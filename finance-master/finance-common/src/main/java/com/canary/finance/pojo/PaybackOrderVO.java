package com.canary.finance.pojo;

import java.io.Serializable;

public class PaybackOrderVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int productId;
	private String productName;
	private String merchantName;
	private double paybackAmount;
	private String paybackDate;
	private double couponAmount;
	private Double interestAmount;
	private int payback;
	
	public PaybackOrderVO() {
		
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}
	
	public Double getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(Double interestAmount) {
		this.interestAmount = interestAmount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public double getPaybackAmount() {
		return paybackAmount;
	}

	public void setPaybackAmount(double paybackAmount) {
		this.paybackAmount = paybackAmount;
	}

	public String getPaybackDate() {
		return paybackDate;
	}

	public void setPaybackDate(String paybackDate) {
		this.paybackDate = paybackDate;
	}

	public int getPayback() {
		return payback;
	}

	public void setPayback(int payback) {
		this.payback = payback;
	}
}