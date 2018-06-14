package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.NewsMaterial;

public interface NewsMaterialDao {
	List<NewsMaterial> queryForList(Map<String, Object> params);
	int queryForCount();
	NewsMaterial selectById(int id);
	NewsMaterial selectByTitle(String title);
	int insert(NewsMaterial news);
	int update(NewsMaterial news);
}