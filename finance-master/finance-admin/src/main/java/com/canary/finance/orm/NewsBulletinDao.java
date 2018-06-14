package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.NewsBulletin;

public interface NewsBulletinDao {
	List<NewsBulletin> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	NewsBulletin selectById(int id);
	int insert(NewsBulletin newsBulletin);
	int update(NewsBulletin newsBulletin);
}