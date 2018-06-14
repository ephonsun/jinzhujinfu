package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.LogOperation;

public interface LogOperationDao {
	public List<LogOperation> queryForList(Map<String, Object> params);
	public int queryForCount(Map<String, Object> params);
	public int insert(LogOperation log);
}
