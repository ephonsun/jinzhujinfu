package com.canary.finance.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Admin;
import com.canary.finance.domain.Resource;
import com.canary.finance.domain.Role;
import com.canary.finance.domain.RoleResource;
import com.canary.finance.orm.AdminDao;
import com.canary.finance.orm.ResourceDao;
import com.canary.finance.orm.RoleDao;
import com.canary.finance.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public List<Admin> getAdminList(int offset, int size) {
		return this.adminDao.queryForList(offset, size);
	}

	@Override
	public int getAdminCount() {
		return this.adminDao.queryForCount();
	}
	
	@Override
	public boolean saveAdmin(Admin admin) {
		if(admin != null && admin.getId() > 0) {
			return this.adminDao.update(admin)>0 ? true : false;
		} else {
			return this.adminDao.insert(admin)>0 ? true : false;
		}
	}

	@Override
	public Admin getAdmin(String name) {
		return this.adminDao.selectByName(name);
	}

	@Override
	public Admin getAdmin(int adminId) {
		return this.adminDao.selectById(adminId);
	}
	
	@Override
	public Role getRole(String name) {
		return this.roleDao.selectByName(name);
	}

	@Override
	public List<Role> getRoleList(int status) {
		if(status != -1) {
			return this.roleDao.queryForList(status);
		} else {
			return this.roleDao.selectAll();
		}
	}

	@Override
	public boolean saveRole(Role role) {
		if(role != null && role.getId() > 0) {
			return this.roleDao.update(role)>0 ? true : false;
		} else {
			return this.roleDao.insert(role)>0 ? true : false;
		}
	}

	@Override
	public Role getRole(int roleId) {
		return this.roleDao.selectById(roleId);
	}

	@Override
	public Map<Resource, Map<Resource, List<Resource>>> getMenu(int roleId) {
		Map<Resource, Map<Resource, List<Resource>>> menusAndFunctions = new LinkedHashMap<Resource, Map<Resource, List<Resource>>>();
		List<Resource> tops = this.resourceDao.selectByParentIdAndRoleId(0, roleId);
		if(tops != null && tops.size() > 0){
			for(Resource top : tops){
				List<Resource> menu = this.resourceDao.selectByParentIdAndRoleId(top.getId(), roleId);
				Map<Resource, List<Resource>> menus = new LinkedHashMap<Resource, List<Resource>>();
				for (Resource me : menu) {
					List<Resource> functions = this.resourceDao.selectByParentIdAndRoleId(me.getId(), roleId);
					if(functions != null && functions.size()>0) {
						menus.put(me, functions);
					} else {
						menus.put(me, new ArrayList<Resource>(0));
					}
				}
				if (menus.size() > 0) {
					menusAndFunctions.put(top, menus);
				} 
			}
		}
		return menusAndFunctions;
	}

	@Override
	public boolean saveRoleResources(int roleId, String sourcesIds) {
		List<RoleResource> roleResources = new ArrayList<RoleResource>();
		if(roleId > 0){
			String[] resourceIds = StringUtils.split(sourcesIds, ",");
			if(resourceIds != null && resourceIds.length > 0) {
				for(String resourceId : resourceIds ){
					RoleResource roleResource = new RoleResource();
					roleResource.setRoleId(roleId);
					roleResource.setResourceId(Integer.parseInt(resourceId));
					roleResources.add(roleResource);
				}
				this.resourceDao.deleteRoleResource(roleId);
				return this.resourceDao.insertRoleResource(roleResources) > 0 ? true : false;
			} else {
				this.resourceDao.deleteRoleResource(roleId);
				return true;
			}
					
		}
		return false;
	}

	@Override
	public List<Resource> getResourceList() {
		return this.resourceDao.selectAll();
	}

	@Override
	public List<Resource> getRoleResource(int roleId) {
		return this.resourceDao.selectByRoleId(roleId);
	}

	@Override
	public List<Resource> getRoleResource(int roleId, int parentId) {
		return this.resourceDao.selectByParentIdAndRoleId(parentId, roleId);
	}
}
