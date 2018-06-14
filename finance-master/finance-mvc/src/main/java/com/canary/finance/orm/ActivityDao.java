package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Activity;

public interface ActivityDao {
	List<Activity> queryForList(Map<String, Object> params);
	int queryForCount();
	Activity selectById(int id);
}