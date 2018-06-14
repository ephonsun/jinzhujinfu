package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Activity;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.domain.PushMessage;

public interface OperationService {
	List<Channel> getChannelList(String name, int offset, int size);
	int getChannelCount(String name);
	List<Channel> getChannelList(int status);
	Channel getChannel(int channelId);
	Channel getChannel(String channelName);
	boolean saveChannel(Channel channel);
	
	List<AppVersion> getAppVersionList(String version, String content, int offset, int size);
	int getAppVersionCount(String version, String content);
	AppVersion getAppVersion(int versionId);
	AppVersion getAppVersion(String versionName);
	boolean saveAppVersion(AppVersion version);
	
	List<PictureLibrary> getPictureList(int platform, int status, int offset, int size);
	int getPictureCount(int platform, int status);
	PictureLibrary getPicture(int pictureId);
	PictureLibrary getPicture(String name, int category);
	boolean savePicture(PictureLibrary picture);
	
	List<Activity> getActivityList(int status, int offset, int size);
	int getActivityCount(int status);
	Activity getActivity(int activityId);
	Activity getActivity(String title);
	boolean saveActivity(Activity activity);
	
	List<NewsBulletin> getNewsBulletinList(String title, int status, int type, int offset, int size);
	int getNewsBulletinCount(String title, int status, int type);
	NewsBulletin getNewsBulletin(int newsId);
	boolean saveNewsBulletin(NewsBulletin news);
	
	List<NewsMaterial> getNewsMaterialList(int offset, int size);
	int getNewsMaterialCount();
	NewsMaterial getNewsMaterial(int newsId);
	NewsMaterial getNewsMaterial(String title);
	boolean saveNewsMaterial(NewsMaterial news);
	
	List<PushMessage> listMessage(String beginTime, String endTime,int offset, int size);
	PushMessage getPushMessage(int id);
	boolean save(PushMessage pushMessage);
	int countMessage(String beginTime, String endTime);
	boolean auditMessage(int id);
}
