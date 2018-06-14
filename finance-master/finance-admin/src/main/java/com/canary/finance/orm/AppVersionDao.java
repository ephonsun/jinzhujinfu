package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.AppVersion;

public interface AppVersionDao {
	public List<AppVersion> queryForList(Map<String, Object> params);
	public int queryForCount(Map<String, Object> params);
	public AppVersion selectById(int id);
	public AppVersion selectByVersion(@Param("version")String version);
	public int insert(AppVersion appVersion);
	public int update(AppVersion appVersion);
}