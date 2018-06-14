package com.canary.finance.orm;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.AppVersion;

public interface AppVersionDao {
	public AppVersion selectLatestAppVersion(@Param("appExtension")String appExtension);
}