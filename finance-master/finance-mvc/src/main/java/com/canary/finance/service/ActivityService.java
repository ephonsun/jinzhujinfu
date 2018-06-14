package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Activity;

public interface ActivityService {
	List<Activity> getActivityList(int offset, int size);
	int getActivityCount();
	Activity getActivity(int activityId);
}
