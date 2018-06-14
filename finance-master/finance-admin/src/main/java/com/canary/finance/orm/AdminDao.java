package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Admin;

public interface AdminDao {
	List<Admin> queryForList(@Param("offset")int offset, @Param("size")int size);
	int queryForCount();
	Admin selectByName(String name);
	Admin selectById(int id);
	int insert(Admin admin);
	int update(Admin admin);
}