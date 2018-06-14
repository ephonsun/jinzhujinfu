package com.canary.finance.pojo;

import java.io.Serializable;

public class TreeNodeVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String pId;
	private boolean checked;
	
	public TreeNodeVO(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}