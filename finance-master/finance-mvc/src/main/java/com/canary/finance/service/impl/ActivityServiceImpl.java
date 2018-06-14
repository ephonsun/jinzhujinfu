package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Activity;
import com.canary.finance.orm.ActivityDao;
import com.canary.finance.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	private ActivityDao activityDao;
	
	@Override
	public List<Activity> getActivityList(int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("size", size);
		return this.activityDao.queryForList(params);
	}

	@Override
	public int getActivityCount() {
		return this.activityDao.queryForCount();
	}

	@Override
	public Activity getActivity(int activityId) {
		return this.activityDao.selectById(activityId);
	}

}
