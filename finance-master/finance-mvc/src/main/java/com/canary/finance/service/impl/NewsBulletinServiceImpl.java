package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.orm.NewsBulletinDao;
import com.canary.finance.orm.NewsMaterialDao;
import com.canary.finance.service.NewsBulletinService;

@Service
public class NewsBulletinServiceImpl implements NewsBulletinService {
	@Autowired
	private NewsBulletinDao newsBulletinDao;
	@Autowired
	private NewsMaterialDao newsMaterialDao;
	
	@Override
	public NewsMaterial getMaterial(int materialId) {
		return newsMaterialDao.selectById(materialId);
	}
	
	@Override
	public List<NewsBulletin> getNewsBulletinList(int type, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("offset", offset);
		params.put("size", size);
		return this.newsBulletinDao.queryForList(params);
	}

	@Override
	public int getNewsBulletinCount(int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return this.newsBulletinDao.queryForCount(params);
	}

	@Override
	public NewsBulletin getNewsBulletin(int newsId) {
		return this.newsBulletinDao.selectById(newsId);
	}

	@Override
	public List<NewsBulletin> getTopNotice() {
		return this.newsBulletinDao.selectTopN(1, 4);
	}

	@Override
	public List<NewsBulletin> getTopNews() {
		return this.newsBulletinDao.selectTopN(2, 4);
	}

}
