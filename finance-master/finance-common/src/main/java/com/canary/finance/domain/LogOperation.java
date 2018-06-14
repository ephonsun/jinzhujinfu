package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class LogOperation implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String respone;
	private String request;
	private Date createTime;
	
	public LogOperation() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRespone() {
		return respone;
	}

	public void setRespone(String respone) {
		this.respone = respone;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
