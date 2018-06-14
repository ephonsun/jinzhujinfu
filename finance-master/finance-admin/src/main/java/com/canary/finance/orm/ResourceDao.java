package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Resource;
import com.canary.finance.domain.RoleResource;

public interface ResourceDao {
	List<Resource> selectAll();
	List<Resource> selectByRoleId(@Param("roleId")int roleId);
	List<Resource> selectByParentIdAndRoleId(@Param("parentId")int parentId, @Param("roleId")int roleId);
	int deleteRoleResource(@Param("roleId")int roleId);
	int insertRoleResource(@Param("roleResources")List<RoleResource> roleResources);
}