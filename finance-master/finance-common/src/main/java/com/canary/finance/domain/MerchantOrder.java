package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class MerchantOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String orderNO;
	private double principal;
	private Date orderTime;
	private int status;
	private double paybackAmount;
	private Date paybackTime;
	private String paybackNO;
	private Product product;
	private Merchant merchant;
	
	public MerchantOrder() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getPaybackAmount() {
		return paybackAmount;
	}

	public void setPaybackAmount(double paybackAmount) {
		this.paybackAmount = paybackAmount;
	}

	public Date getPaybackTime() {
		return paybackTime;
	}

	public void setPaybackTime(Date paybackTime) {
		this.paybackTime = paybackTime;
	}

	public String getPaybackNO() {
		return paybackNO;
	}

	public void setPaybackNO(String paybackNO) {
		this.paybackNO = paybackNO;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
}