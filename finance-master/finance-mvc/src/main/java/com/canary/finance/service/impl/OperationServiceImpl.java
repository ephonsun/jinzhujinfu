package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Activity;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.orm.ActivityDao;
import com.canary.finance.orm.AppVersionDao;
import com.canary.finance.orm.ChannelDao;
import com.canary.finance.orm.NewsBulletinDao;
import com.canary.finance.orm.NewsMaterialDao;
import com.canary.finance.orm.PictureLibraryDao;
import com.canary.finance.pojo.MaterialVO;
import com.canary.finance.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService {
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private AppVersionDao versionDao;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private PictureLibraryDao pictureLibraryDao;
	@Autowired
	private NewsBulletinDao newsBulletinDao;
	@Autowired
	private NewsMaterialDao newsMaterialDao;
	
	@Override
	public AppVersion getLatestAppVersion(String appExtension) {
		return versionDao.selectLatestAppVersion(appExtension);
	}
	
	@Override
	public List<Channel> getChannelList(String name, int offset, int size) {
		Map<String, Object> params = this.getParams(name);
		params.put("offset", offset);
		params.put("size", size);
		return this.channelDao.queryForList(params);
	}
	
	@Override
	public List<Channel> getChannelList(int status) {
		return this.channelDao.selectByStatus(status);
	}

	@Override
	public int getChannelCount(String name) {
		return this.channelDao.queryForCount(this.getParams(name));
	}

	@Override
	public Channel getChannel(int channelId) {
		return this.channelDao.selectById(channelId);
	}

	@Override
	public Channel getChannel(String channelName) {
		return this.channelDao.selectByName(channelName);
	}

	@Override
	public boolean saveChannel(Channel channel) {
		if(channel != null && channel.getId() > 0) {
			return this.channelDao.update(channel)>0 ? true : false;
		} else { 
			return this.channelDao.insert(channel)>0 ? true : false;
		}
	}

	@Override
	public List<PictureLibrary> getAppPictureList() {
		return this.pictureLibraryDao.selectForMobile();
	}

	@Override
	public PictureLibrary getPicture(int pictureId) {
		return this.pictureLibraryDao.selectById(pictureId);
	}

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

	@Override
	public List<MaterialVO> getNewsBulletinList(int type, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("offset", offset);
		params.put("size", size);
		return this.newsBulletinDao.queryVOForList(params);
	}

	@Override
	public int getNewsBulletinCount(int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return this.newsBulletinDao.queryVOForCount(params);
	}

	@Override
	public MaterialVO getNewsBulletin(int newsId) {
		return this.newsBulletinDao.selectVOById(newsId);
	}

	@Override
	public List<NewsMaterial> getNewsMaterialList(int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("size", size);
		return this.newsMaterialDao.queryForList(params);
	}

	@Override
	public int getNewsMaterialCount() {
		return this.newsMaterialDao.queryForCount();
	}

	@Override
	public NewsMaterial getNewsMaterial(int newsId) {
		return this.newsMaterialDao.selectById(newsId);
	}

	@Override
	public NewsMaterial getNewsMaterial(String title) {
		return this.newsMaterialDao.selectByTitle(title);
	}

	@Override
	public boolean saveNewsMaterial(NewsMaterial news) {
		if(news != null && news.getId() > 0) {
			return this.newsMaterialDao.update(news)>0 ? true : false;
		} else {
			return this.newsMaterialDao.insert(news)>0 ? true : false;
		}
	}
	
	private Map<String, Object> getParams(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		return params;
	}
}
