package com.canary.finance.pojo;

import java.io.Serializable;

public class PaybackVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String paybackNO;
	private String inAccount;
	private String outAccount;
	private double principal;
	private double profit;
	private double serviceCharge;
	private int customerId;
	private double paybackAmount;
	private String productName;
	
	public PaybackVO() {
		
	}

	public String getPaybackNO() {
		return paybackNO;
	}

	public void setPaybackNO(String paybackNO) {
		this.paybackNO = paybackNO;
	}

	public String getInAccount() {
		return inAccount;
	}

	public void setInAccount(String inAccount) {
		this.inAccount = inAccount;
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public double getPaybackAmount() {
		return paybackAmount;
	}

	public void setPaybackAmount(double paybackAmount) {
		this.paybackAmount = paybackAmount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOutAccount() {
		return outAccount;
	}

	public void setOutAccount(String outAccount) {
		this.outAccount = outAccount;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
}