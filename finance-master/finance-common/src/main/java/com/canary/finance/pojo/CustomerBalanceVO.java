package com.canary.finance.pojo;

import java.io.Serializable;

public class CustomerBalanceVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private double amount;
	private int category;
	private String remark;
	private String tradeTime;
	
	public CustomerBalanceVO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
}