package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.PushMessage;

public interface PushMessageDao {
	List<PushMessage> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	PushMessage selectById(int id);
	int insert(PushMessage pushMessage);
	int update(PushMessage pushMessage);
	int updateStatus(int id);
}