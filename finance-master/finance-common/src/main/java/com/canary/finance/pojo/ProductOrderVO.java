package com.canary.finance.pojo;

import static com.canary.finance.util.ConstantUtil.NORMAL_DATETIME_FORMAT;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductOrderVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderNO;
	private String cellphone;
	private int principal;
	private String orderTime;
	
	public ProductOrderVO() {
		
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public int getPrincipal() {
		return principal;
	}

	public void setPrincipal(int principal) {
		this.principal = principal;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
		this.orderTime = dateFormat.format(orderTime);
	}
}