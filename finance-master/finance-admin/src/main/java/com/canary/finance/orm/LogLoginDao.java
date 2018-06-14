package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.LogLogin;

public interface LogLoginDao {
	public List<LogLogin> queryForList(Map<String, Object> params);
	public int queryForCount(Map<String, Object> params);
	public int insert(LogLogin loginLog);
	public int update(LogLogin loginLog);
}