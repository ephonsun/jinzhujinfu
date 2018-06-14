package com.canary.finance.domain;

import java.io.Serializable;

public class RoleResource implements Serializable{
	private static final long serialVersionUID = 1L;
	private int roleId;
	private int resourceId;
	
	public RoleResource(){
		
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
}