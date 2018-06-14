package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Role;

public interface RoleDao {
	List<Role> queryForList(@Param("status")int status);
	List<Role> selectAll();
	Role selectById(int id);
	Role selectByName(@Param("name")String name);
	int insert(Role role);
	int update(Role role);
}