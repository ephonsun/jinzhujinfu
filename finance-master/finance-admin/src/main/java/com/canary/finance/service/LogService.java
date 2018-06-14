package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.LogLogin;
import com.canary.finance.domain.LogOperation;

public interface LogService {
	List<LogLogin> getLoginLogList(String name, String beginTime, String endTime, int offset, int size);
	int getLoginLogCount(String name, String beginTime, String endTime);
	boolean saveLoginLog(LogLogin loginLog);
	
	List<LogOperation> getOperationLogList(String name, String beginTime, String endTime, int offset, int size);
	int getOperationLogCount(String adminName, String beginTime, String endTime);
	boolean saveOperationLog(LogOperation operationLog);
}