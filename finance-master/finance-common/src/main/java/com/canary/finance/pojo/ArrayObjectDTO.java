package com.canary.finance.pojo;

import java.io.Serializable;

public class ArrayObjectDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object object;
	private boolean success;
	private String message;
	
	public ArrayObjectDTO() {
		
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "[\"" + object.toString() +"\"," + success + ",\"" + message + "\"]";
	}
}  