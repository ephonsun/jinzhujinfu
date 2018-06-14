package com.canary.finance.service.impl;

import static com.canary.finance.util.ConstantUtil.MAX_TIME;
import static com.canary.finance.util.ConstantUtil.MIN_TIME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Activity;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.domain.PushMessage;
import com.canary.finance.orm.ActivityDao;
import com.canary.finance.orm.AppVersionDao;
import com.canary.finance.orm.ChannelDao;
import com.canary.finance.orm.NewsBulletinDao;
import com.canary.finance.orm.NewsMaterialDao;
import com.canary.finance.orm.PictureLibraryDao;
import com.canary.finance.orm.PushMessageDao;
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
	@Autowired
	private PushMessageDao pushMessageDao;
	
	@Override
	public List<PushMessage> listMessage(String beginTime, String endTime,int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
			params.put("beginTime", beginTime+MIN_TIME);
			params.put("endTime", endTime+MAX_TIME);
		}
		params.put("offset", offset);
		params.put("size", size);
		return pushMessageDao.queryForList(params);
	}
	
	@Override
	public PushMessage getPushMessage(int id) {
		return pushMessageDao.selectById(id);
	}
	
	@Override
	public boolean save(PushMessage pushMessage) {
		if (pushMessage != null) {
			if (pushMessage.getId() > 0) {
				return pushMessageDao.update(pushMessage) > 0 ? true : false;
			} else {
				return pushMessageDao.insert(pushMessage) > 0 ? true : false;
			}
			
		}
		return false;
	}
	
	@Override
	public int countMessage(String beginTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
			params.put("beginTime", beginTime+MIN_TIME);
			params.put("endTime", endTime+MAX_TIME);
		}
		return pushMessageDao.queryForCount(params);
	}
	
	@Override
	public boolean auditMessage(int id) {
		return pushMessageDao.updateStatus(id) > 0 ? true : false;
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
	public List<AppVersion> getAppVersionList(String version, String content, int offset, int size) {
		Map<String, Object> params = this.getParams(version, content);
		params.put("offset", offset);
		params.put("size", size);
		return this.versionDao.queryForList(params);
	}

	@Override
	public int getAppVersionCount(String version, String content) {
		return this.versionDao.queryForCount(this.getParams(version, content));
	}

	@Override
	public AppVersion getAppVersion(int versionId) {
		return this.versionDao.selectById(versionId);
	}

	@Override
	public AppVersion getAppVersion(String versionName) {
		return this.versionDao.selectByVersion(versionName);
	}

	@Override
	public boolean saveAppVersion(AppVersion version) {
		if(version != null && version.getId() > 0) {
			return this.versionDao.update(version)>0 ? true : false;
		} else {
			return this.versionDao.insert(version)>0 ? true : false;
		}
	}

	@Override
	public List<PictureLibrary> getPictureList(int platform, int status, int offset, int size) {
		Map<String, Object> params = this.getParams(platform, status);
		params.put("offset", offset);
		params.put("size", size);
		return this.pictureLibraryDao.queryForList(params);
	}

	@Override
	public int getPictureCount(int platform, int status) {
		return this.pictureLibraryDao.queryForCount(this.getParams( platform, status));
	}

	@Override
	public PictureLibrary getPicture(int pictureId) {
		return this.pictureLibraryDao.selectById(pictureId);
	}

	@Override
	public PictureLibrary getPicture(String name, int platform) {
		return this.pictureLibraryDao.selectByNameAndPlatform(name, platform);
	}

	@Override
	public boolean savePicture(PictureLibrary picture) {
		if(picture != null && picture.getId() > 0) {
			if (StringUtils.isBlank(picture.getLink()) || StringUtils.length(picture.getLink()) < 10) {
				String link = "https://www.jinzhujinfu.com/banner/detail/"+picture.getId();
				picture.setLink(link);
			}
			return this.pictureLibraryDao.update(picture)>0 ? true : false;
		} else {
			if (StringUtils.isBlank(picture.getLink()) || StringUtils.length(picture.getLink()) < 10) {
				if (this.pictureLibraryDao.insert(picture)>0) {
					String link = "https://www.jinzhujinfu.com/banner/detail/"+picture.getId();
					picture.setLink(link);
					return this.pictureLibraryDao.update(picture)>0 ? true : false;
				}
				return false;
			} else {
				return this.pictureLibraryDao.insert(picture)>0 ? true : false;
			}
		}
	}

	@Override
	public List<Activity> getActivityList(int status, int offset, int size) {
		Map<String, Object> params = this.getParams(status);
		params.put("offset", offset);
		params.put("size", size);
		return this.activityDao.queryForList(params);
	}

	@Override
	public int getActivityCount(int status) {
		return this.activityDao.queryForCount(this.getParams(status));
	}

	@Override
	public Activity getActivity(int activityId) {
		return this.activityDao.selectById(activityId);
	}

	@Override
	public Activity getActivity(String title) {
		return this.activityDao.selectByTitle(title);
	}

	@Override
	public boolean saveActivity(Activity activity) {
		if(activity != null && activity.getId() > 0) {
			return this.activityDao.update(activity)>0 ? true : false;
		} else {
			return this.activityDao.insert(activity)>0 ? true : false;
		}
	}

	@Override
	public List<NewsBulletin> getNewsBulletinList(String title, int status, int type, int offset, int size) {
		Map<String, Object> params = this.getParams(title, status, type);
		params.put("offset", offset);
		params.put("size", size);
		return this.newsBulletinDao.queryForList(params);
	}

	@Override
	public int getNewsBulletinCount(String title, int status, int type) {
		return this.newsBulletinDao.queryForCount(this.getParams(title, status, type));
	}

	@Override
	public NewsBulletin getNewsBulletin(int newsId) {
		return this.newsBulletinDao.selectById(newsId);
	}

	@Override
	public boolean saveNewsBulletin(NewsBulletin news) {
		if(news != null && news.getId() > 0) {
			return this.newsBulletinDao.update(news)>0 ? true : false;
		} else {
			return this.newsBulletinDao.insert(news)>0 ? true : false;
		}
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
			if (StringUtils.isBlank(news.getUrl())) {
				if (this.newsMaterialDao.insert(news)>0 ? true : false) {
					String url = "https://www.jinzhujinfu.com/news/detail/"+news.getId();
					news.setUrl(url);
					return this.newsMaterialDao.update(news)>0 ? true : false;
				}
				return false;
			} else {
				return this.newsMaterialDao.insert(news)>0 ? true : false;
			}
		}
	}
	
	private Map<String, Object> getParams(int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		return params;
	}
	
	private Map<String, Object> getParams(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		return params;
	}
	
	private Map<String, Object> getParams(String version, String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(version)) {
			params.put("version", version);
		}
		if(StringUtils.isNotBlank(content)) {
			params.put("content", content);
		}
		return params;
	}
	
	private Map<String, Object> getParams(int platform, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("platform", platform);
		params.put("status", status);
		return params;
	}
	
	private Map<String, Object> getParams(String title, int status, int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(title)) {
			params.put("title", title);
		}
		params.put("status", status);
		params.put("type", type);
		return params;
	}
}
