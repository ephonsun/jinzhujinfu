package com.canary.finance.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Admin;
import com.canary.finance.domain.Resource;
import com.canary.finance.domain.Role;

public class SessionVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Admin admin;
	private Role role;
	private Map<Resource, Map<Resource, List<Resource>>> menu;
	private Object object;
	
	public SessionVO() {
		
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Map<Resource, Map<Resource, List<Resource>>> getMenu() {
		return menu;
	}

	public void setMenu(Map<Resource, Map<Resource, List<Resource>>> menu) {
		this.menu = menu;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}