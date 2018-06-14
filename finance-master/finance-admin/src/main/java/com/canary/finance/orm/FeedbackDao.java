package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Feedback;

public interface FeedbackDao {
	public List<Feedback> queryForList(Map<String, Object> params);
	public int queryForCount(Map<String, Object> params);
	public int insert(Feedback feedback);
}