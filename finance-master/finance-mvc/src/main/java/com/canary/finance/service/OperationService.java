package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Activity;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.pojo.MaterialVO;

public interface OperationService {
	List<Channel> getChannelList(String name, int offset, int size);
	int getChannelCount(String name);
	List<Channel> getChannelList(int status);
	Channel getChannel(int channelId);
	Channel getChannel(String channelName);
	boolean saveChannel(Channel channel);
	
	AppVersion getLatestAppVersion(String appExtension);
	
	List<PictureLibrary> getAppPictureList();
	PictureLibrary getPicture(int pictureId);
	
	List<Activity> getActivityList(int offset, int size);
	int getActivityCount();
	Activity getActivity(int activityId);
	
	List<MaterialVO> getNewsBulletinList(int type, int offset, int size);
	int getNewsBulletinCount(int type);
	MaterialVO getNewsBulletin(int newsId);
	
	List<NewsMaterial> getNewsMaterialList(int offset, int size);
	int getNewsMaterialCount();
	NewsMaterial getNewsMaterial(int newsId);
	NewsMaterial getNewsMaterial(String title);
	boolean saveNewsMaterial(NewsMaterial news);
}
