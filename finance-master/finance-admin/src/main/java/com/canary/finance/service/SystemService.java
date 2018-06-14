package com.canary.finance.service;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Admin;
import com.canary.finance.domain.Resource;
import com.canary.finance.domain.Role;

public interface SystemService {
	boolean saveAdmin(Admin admin);
	Admin getAdmin(String name);
	Admin getAdmin(int adminId);
	List<Admin> getAdminList(int offset, int size);
	public int getAdminCount();
	
	boolean saveRole(Role role);
	Role getRole(int roleId);
	Role getRole(String name);
	List<Role> getRoleList(int status);
	
	Map<Resource, Map<Resource, List<Resource>>> getMenu(int roleId);
	List<Resource> getResourceList();
	List<Resource> getRoleResource(int roleId);
	List<Resource> getRoleResource(int roleId, int parentId);
	boolean saveRoleResources(int roleId, String resourceIds);
}
