package com.canary.finance.remote.http;
import static com.canary.finance.remote.util.ConstantUtil.CODE;
import static com.canary.finance.remote.util.ConstantUtil.RESULT;
import static com.canary.finance.remote.util.ConstantUtil.SEMICOLON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;

public class JPushServiceTask implements Callable<String> {
	private static final Log log = LogFactory.getLog(JPushServiceTask.class);
	private static final int ALIAS_PUSH_MAX_LENGTH = 1000;
	private static final String PUSH_OS_TYPE = "osType";
	private static final String PUSH_MSG_TITLE = "title";
	private static final String PUSH_MSG_CONTENT = "content";
	private static final String PUSH_MSG_SEND_TYPE = "sendType";
	private static final String PUSH_MSG_SEND_TIME = "sendTime";
	private static final String PUSH_MSG_SEND_TARGET = "sendTarget";
	private static final String PUSH_MSG_EQUIPMENTS = "equipments";
	private static final int PUSH_ANDROID_DEVICE = 1;
	private static final int PUSH_IOS_DEVICE = 2;
	private static final String PUSH_PRODUCTION = "jpush.ios.production";
	private static final String EMPTY = "";
	private static final String MINUS = "-";
	private final String apikey;
	private final String secretkey;
	private final Map<String, String> map;
	
	public JPushServiceTask(String apikey, String secretkey, Map<String, String> map) {
		this.apikey = apikey;
		this.secretkey = secretkey;
		this.map = map;
		
	}

	@Override
	public String call() throws Exception {
		int deviceType = 0;	
		if(StringUtils.isNotBlank(map.get(PUSH_OS_TYPE))) {
			deviceType = Integer.parseInt(map.get(PUSH_OS_TYPE));
		}
		int sendTarget = 0;
		if(StringUtils.isNotBlank(map.get(PUSH_MSG_SEND_TARGET))) {
			sendTarget = Integer.parseInt(map.get(PUSH_MSG_SEND_TARGET));
		}
		int sendType = 0;
	    if(StringUtils.isNotBlank(map.get(PUSH_MSG_SEND_TYPE))){
	    	sendType = Integer.parseInt(map.get(PUSH_MSG_SEND_TYPE));
	    }
		String title = map.get(PUSH_MSG_TITLE);
		String time = map.get(PUSH_MSG_SEND_TIME);
		String content = map.get(PUSH_MSG_CONTENT);
		String equipments = map.get(PUSH_MSG_EQUIPMENTS);
		int category = 0;
		if (StringUtils.isNotBlank(content)) {
			return jPushMsg(deviceType, sendTarget, sendType, time, title, content, equipments, category);
		}
		return null;
	}
	
	private String jPushMsg(int deviceType, int sendTarget, int type,  String time, String title, String content, String equipments, int category) {
		try {
			JPushClient jpushClient = new JPushClient(secretkey, apikey);
			if(type == 1){
				return this.timingPush(jpushClient, deviceType, title, content, equipments, time);
			}else{
				return this.immediatelyPush(jpushClient, deviceType, title, content, equipments, category);
			}
		} catch (Exception e) {
			log.error("Connection error. Should retry later. ", e);
		}
		return null;
	}
	
	private String immediatelyPush(JPushClient jpushClient, int deviceType, String title, String content, String equipments, int category){
	    try {
	    	PushResult result = jpushClient.sendPush(new Builder()
	    	.setPlatform(this.getPlatform(deviceType))
	    	.setAudience(getAudience(equipments))
	    	.setNotification(this.getNotification(deviceType, title, content, category))
	    	.setOptions(this.getOption())
	    	.build());
            return "msgId: " + result.msg_id + ", request: " + result.isResultOK();
        } catch (APIConnectionException e) {
        	log.error("Connection error. Should retry later. ", e);
        	return e.getMessage();
        } catch (APIRequestException e) {
        	log.error("Error response from JPush server. Should review and fix it. ");
        	log.error("HTTP Status: " + e.getStatus());   
        	log.error("Error Code: " + e.getErrorCode());
        	log.error("Error Message: " + e.getErrorMessage());   
        	log.error("Msg ID: " + e.getMsgId());
        	return e.getMessage();
        }
	}
	
	private String timingPush(JPushClient jpushClient, int deviceType, String title, String content, String equipments, String time){
		try {
			ScheduleResult result = jpushClient.createSingleSchedule(String.valueOf(deviceType), time, new Builder()
            .setPlatform(this.getPlatform(deviceType))
            .setAudience(this.getAudience(equipments))
            .setNotification(this.getNotification(deviceType, title, content, 0))
            .setOptions(this.getOption())
            .build());
            log.info("schedule result is " + result);
            return "request is : " + result.getResponseCode();
        } catch (APIConnectionException e) {
        	log.error("Connection error. Should retry later. ", e);
        	return e.getMessage();
        } catch (APIRequestException e) {
        	log.error("Error response from JPush server. Should review and fix it. ", e);
        	log.error("HTTP Status: " + e.getStatus());
        	log.error("Error Code: " + e.getErrorCode());
        	log.error("Error Message: " + e.getErrorMessage());
        	return e.getMessage();
        }
	}
	
	private Audience getAudience(String equipments){
		if(StringUtils.isNotBlank(equipments)){
    		String[] channelIds = equipments.split(SEMICOLON);
    		Collection<String> aliases = new ArrayList<String>();
    		if(channelIds.length == 1){
    			log.info("the single push drivce ID is : "+ equipments);
    			return Audience.alias(equipments.replace(MINUS, EMPTY));
    		}else if(channelIds.length > 1 && channelIds.length <= ALIAS_PUSH_MAX_LENGTH){
    			StringBuffer sb = new StringBuffer();
    			for(int i = 0;i< channelIds.length;i++){
    				aliases.add(channelIds[i].replace(MINUS, EMPTY));
    				sb.append(channelIds[i]).append(" ");
    			} 
    			log.info("the batch push drivces ID is : "+sb);
    			return Audience.alias(aliases);
    		}
    	}
		return Audience.all();
	}
	
	private Platform getPlatform(int deviceType){
		if(deviceType == PUSH_ANDROID_DEVICE){
			return Platform.android();
		}else if(deviceType == PUSH_IOS_DEVICE){
			return Platform.ios();
		}else{
			return Platform.all();
		}
	}
	
	private Notification getNotification(int deviceType, String title, String content, int category){
		Map<String, String> extras = new HashMap<String, String>();
		if (category > 0) {
			extras.put(CODE, String.valueOf(category));
			extras.put(RESULT, String.valueOf(category));
		}
		if(deviceType == PUSH_ANDROID_DEVICE){
			return Notification.newBuilder()
					.addPlatformNotification(AndroidNotification.newBuilder()
                    .setAlert(content)
                    .setTitle(title)
                    .addExtras(extras)
                    .build())
	              .build();
		}else if(deviceType == PUSH_IOS_DEVICE){
			return Notification.newBuilder()
	                .addPlatformNotification(IosNotification.newBuilder()
	                .setAlert(content)
	                .setSound("sound.caf")
	                .setBadge(0)
	                .addExtras(extras)
	                .build())
	              .build();
		}else{
			return Notification.newBuilder()
			.addPlatformNotification(AndroidNotification.newBuilder()
	            .setAlert(content)
	            .setTitle(title)
	            .addExtras(extras)
	            .build())
            .addPlatformNotification(IosNotification.newBuilder()
                .setAlert(content)
                .setSound("sound.caf")
                .setBadge(0)
                .addExtras(extras)
                .build())
            .build();
		}	
	}
	
	private Options getOption(){
		Options options = Options.newBuilder().build();
		boolean production = true;
		if(StringUtils.isNotBlank(map.get(PUSH_PRODUCTION))) {
			production = Boolean.parseBoolean(map.get(PUSH_PRODUCTION));
		}
		options.setApnsProduction(production);
		return options;
	}
	
}