package com.canary.finance.domain;

import java.io.Serializable;

public class PayBank implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String bankName;
	private String bankNO;
	private int singleLimit;
	private int dayLimit;
	private int monthLimit;
	private int category;
	private int status;
	
	public PayBank() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNO() {
		return bankNO;
	}

	public void setBankNO(String bankNO) {
		this.bankNO = bankNO;
	}

	public int getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(int singleLimit) {
		this.singleLimit = singleLimit;
	}

	public int getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(int dayLimit) {
		this.dayLimit = dayLimit;
	}

	public int getMonthLimit() {
		return monthLimit;
	}

	public void setMonthLimit(int monthLimit) {
		this.monthLimit = monthLimit;
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