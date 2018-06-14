package com.canary.finance.pojo;

import java.io.Serializable;
import java.util.Date;

public class PaymentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderNo;
	private String oldOrderNO;
	private String customerName;
	private Date orderTime;
	private Date oldOrderTime;
	private double amount;
	private int productId;
	private String productName;
	private String bankCode;
	private String bankName;
	private String bankCard;
	private int flagCard;
	private String infoOrder;
	private String voucher;
	
	public PaymentDTO() {
		
	} 
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public int getFlagCard() {
		return flagCard;
	}

	public void setFlagCard(int flagCard) {
		this.flagCard = flagCard;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public String getInfoOrder() {
		return infoOrder;
	}

	public void setInfoOrder(String infoOrder) {
		this.infoOrder = infoOrder;
	}

	public String getOldOrderNO() {
		return oldOrderNO;
	}

	public void setOldOrderNO(String oldOrderNO) {
		this.oldOrderNO = oldOrderNO;
	}

	public Date getOldOrderTime() {
		return oldOrderTime;
	}

	public void setOldOrderTime(Date oldOrderTime) {
		this.oldOrderTime = oldOrderTime;
	}
}
