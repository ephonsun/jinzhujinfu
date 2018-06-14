package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.LogLogin;
import com.canary.finance.domain.LogOperation;
import com.canary.finance.orm.LogLoginDao;
import com.canary.finance.orm.LogOperationDao;
import com.canary.finance.service.LogService;

@Service
public class LogServiceImpl implements LogService{
	@Autowired
	private LogLoginDao logLoginDao;
	@Autowired
	private LogOperationDao logOperationDao;
	
	@Override
	public List<LogLogin> getLoginLogList(String name, String beginTime, String endTime, int offset, int size) {
		Map<String, Object> params = this.getParams(name, beginTime, endTime);
		params.put("offset", offset);
		params.put("size", size);
		return this.logLoginDao.queryForList(params);
	}

	@Override
	public int getLoginLogCount(String name, String beginTime, String endTime) {
		return this.logLoginDao.queryForCount(this.getParams(name, beginTime, endTime));
	}
	
	@Override
	public boolean saveLoginLog(LogLogin loginLog) {
		return this.logLoginDao.insert(loginLog)>0 ? true : false;
	}
	
	@Override
	public List<LogOperation> getOperationLogList(String name, String beginTime, String endTime, int offset, int size) {
		Map<String, Object> params = this.getParams(name, beginTime, endTime);
		params.put("offset", offset);
		params.put("size", size);
		return this.logOperationDao.queryForList(params);
	}
	
	@Override
	public int getOperationLogCount(String name, String beginTime, String endTime) {
		return this.logOperationDao.queryForCount(this.getParams(name, beginTime, endTime));
	}

	@Override
	public boolean saveOperationLog(LogOperation operationLog) {
		return this.logOperationDao.insert(operationLog)>0 ? true : false;
	}
	
	private Map<String, Object> getParams(String name, String beginTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
		}
		return params;
	}
}
