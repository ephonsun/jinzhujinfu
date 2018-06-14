package com.canary.finance.pojo;

import java.io.Serializable;

public class InvitorCouponVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int amount;
	private String createTime;
	private String cellphone;
	
	public InvitorCouponVO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
}