package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable{
	private static final long serialVersionUID = 1L;
	private String orderNO;
	private String payDetail;
	private String sign;
	private String payDate;
	private int productId;
	private String account;
	private int userId;
	private String cardNO;
	private Date createTime;
	
	public String getOrderNO() {
		return orderNO;
	}
	
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPayDetail() {
		return payDetail;
	}

	public void setPayDetail(String payDetail) {
		this.payDetail = payDetail;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCardNO() {
		return cardNO;
	}

	public void setCardNO(String cardNO) {
		this.cardNO = cardNO;
	}
}