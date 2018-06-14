package com.canary.finance.pojo;

import java.io.Serializable;

public class CustomerMessageVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String content;
	private int status;
	private String time;
	
	public CustomerMessageVO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}