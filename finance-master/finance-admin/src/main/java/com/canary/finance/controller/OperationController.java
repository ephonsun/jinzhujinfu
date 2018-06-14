package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.NORMAL_DATETIME_FORMAT;
import static com.canary.finance.util.ConstantUtil.UTF_8;
import static com.canary.finance.util.ConstantUtil.COMMA;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.domain.Activity;
import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.Channel;
import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.NewsMaterial;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.domain.PushMessage;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.service.CouponService;
import com.canary.finance.service.OperationService;

@Controller
@RequestMapping("/operation")
public class OperationController extends BaseController {
	private static final String PUSH = "/push";
	private static final String PUSH_OS_TYPE = "osType";
	private static final String PUSH_MSG_TITLE = "title";
	private static final String PUSH_MSG_CONTENT = "content";
	private static final String PUSH_MSG_SEND_TYPE = "sendType";
	private static final String PUSH_MSG_SEND_TIME = "sendTime";
	private static final String PUSH_MSG_SEND_TARGET = "sendTarget";
	private static final String PUSH_MSG_EQUIPMENTS = "equipments";
	@Autowired
	private OperationService opearationService;
	@Autowired
	private CouponService couponService;
	
	@RequestMapping("/message/list")
	public String getmessageList(String beginTime, String endTime, Integer page, Integer size, Model model) {
		int total = this.opearationService.countMessage(beginTime, endTime);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("messages", this.opearationService.listMessage(beginTime, endTime, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("messages", new PushMessage[0]);
		}
		model.addAttribute("beginTime", StringUtils.trimToEmpty(beginTime));
		model.addAttribute("endTime", StringUtils.trimToEmpty(endTime));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/message/list";
	}
	
	@RequestMapping("/message/{base64}")
	public String forwardMessageList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getmessageList(json.getString("beginTime"), json.getString("endTime"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/message/add/{base64}")
	public String addMessage(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/message/add";
	}
	
	@RequestMapping("/message/{messageId:\\d+}/edit/{base64}")
	public String editMessage(@PathVariable("messageId")int messageId, @PathVariable("base64")String base64, Model model) {
		PushMessage pushMessage = this.opearationService.getPushMessage(messageId);
		if(pushMessage == null) {
			pushMessage = new PushMessage();
		}
		model.addAttribute("message", pushMessage);
		model.addAttribute("base64", base64);
		return "operation/message/edit";
	}
	
	@RequestMapping("/message/{messageId:\\d+}/audit/{base64}")
	public String audutMessage(@PathVariable("messageId")int messageId, @PathVariable("base64")String base64, Model model) throws Exception {
		PushMessage pushMessage = this.opearationService.getPushMessage(messageId);
		if(pushMessage != null && pushMessage.getId() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair(PUSH_OS_TYPE, String.valueOf(pushMessage.getOsType())));
			nvps.add(new BasicNameValuePair(PUSH_MSG_TITLE, pushMessage.getTitle()));
			nvps.add(new BasicNameValuePair(PUSH_MSG_SEND_TYPE, String.valueOf(pushMessage.getSendType())));
			if(pushMessage.getSendTime() != null){
				nvps.add(new BasicNameValuePair(PUSH_MSG_SEND_TIME, new SimpleDateFormat(NORMAL_DATETIME_FORMAT).format(pushMessage.getSendTime())));
			}else{
				nvps.add(new BasicNameValuePair(PUSH_MSG_SEND_TIME, null));
			}
			nvps.add(new BasicNameValuePair(PUSH_MSG_CONTENT, pushMessage.getContent()));
			nvps.add(new BasicNameValuePair(PUSH_MSG_SEND_TARGET, String.valueOf(pushMessage.getSendTarget())));
			nvps.add(new BasicNameValuePair(PUSH_MSG_EQUIPMENTS, pushMessage.getEquipment()));
			String url = properties.getRemotingUrl() + PUSH;
			String result = this.execute(url, nvps, UTF_8);
			LOGGER.info("the push result : " + result);
			if (StringUtils.isNotBlank(result)) {
				String[] arr = StringUtils.split(result, COMMA);
				if(arr != null && arr.length == 2){
					opearationService.auditMessage(messageId);
				}
			}
		}
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getmessageList(json.getString("beginTime"), json.getString("endTime"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@PostMapping("/message/save")
	public String saveMessage(PushMessage pushMessage, String base64, Model model) throws Exception {
		this.opearationService.save(pushMessage);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/message/"+StringUtils.trimToEmpty(base64);
	}
	
	@RequestMapping("/banner/list")
	public String getBannerList(Integer page, Integer size, Model model) {
		int total = this.opearationService.getPictureCount(-1, -1);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("banneres", this.opearationService.getPictureList(-1, -1, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("banneres", new PictureLibrary[0]);
		}
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/banner/list";
	}
	
	@RequestMapping("/banner/{base64}")
	public String forwardBannerList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getBannerList(json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/banner/add/{base64}")
	public String addBanner(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/banner/add";
	}
	
	@RequestMapping("/banner/{bannerId:\\d+}/edit/{base64}")
	public String editBanner(@PathVariable("bannerId")int bannerId, @PathVariable("base64")String base64, Model model) {
		PictureLibrary banner = this.opearationService.getPicture(bannerId);
		if(banner == null) {
			banner = new PictureLibrary();
		}
		model.addAttribute("banner", banner);
		model.addAttribute("base64", base64);
		return "operation/banner/edit";
	}
	
	@PostMapping("/banner/save")
	public String saveBanner(PictureLibrary banner, String base64, Model model) throws Exception {
		this.opearationService.savePicture(banner);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/banner/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/banner/{bannerId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateBanner(@PathVariable("bannerId")int bannerId, @PathVariable("operate")int status) {
		PictureLibrary banner = this.opearationService.getPicture(bannerId);
		if(banner != null && banner.getId() > 0) {
			banner.setStatus(status);
			return this.opearationService.savePicture(banner);
		}
		return false;
	}
	
	@RequestMapping("/activity/list")
	public String getActivityList(Integer page, Integer size, Model model) {
		int total = this.opearationService.getActivityCount(-1);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("activities", this.opearationService.getActivityList(-1, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("activities", new Activity[0]);
		}
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/activity/list";
	}
	
	@RequestMapping("/activity/{base64}")
	public String forwardActivityList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getActivityList(json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/activity/add/{base64}")
	public String addActivity(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/activity/add";
	}
	
	@RequestMapping("/activity/{activityId:\\d+}/edit/{base64}")
	public String editActivity(@PathVariable("activityId")int activityId, @PathVariable("base64")String base64, Model model) {
		Activity activity = this.opearationService.getActivity(activityId);
		if(activity == null) {
			activity = new Activity();
		}
		model.addAttribute("activity", activity);
		model.addAttribute("base64", base64);
		return "operation/activity/edit";
	}
	
	@PostMapping("/activity/save")
	public String saveActivity(Activity activity, String base64, Model model) throws Exception {
		this.opearationService.saveActivity(activity);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/activity/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/activity/{activityId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateActivity(@PathVariable("activityId")int activityId, @PathVariable("operate")int status) {
		Activity activity = this.opearationService.getActivity(activityId);
		if(activity != null && activity.getId() > 0) {
			activity.setStatus(status);
			return this.opearationService.saveActivity(activity);
		}
		return false;
	}
	
	@RequestMapping("/notice/list")
	public String getNoticeList(String title, Integer type, Integer page, Integer size, Model model) {
		if(type == null) {
			type = 0;
		}
		int total = this.opearationService.getNewsBulletinCount(title, -1, type);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("notices", this.opearationService.getNewsBulletinList(title, -1, type, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("notices", new NewsBulletin[0]);
		}
		model.addAttribute("title", StringUtils.trimToEmpty(title));
		model.addAttribute("type", type);
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/notice/list";
	}
	
	@RequestMapping("/notice/{base64}")
	public String forwardNoticeList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getNoticeList(json.getString("title"), json.getInteger("type") ,json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/notice/add/{base64}")
	public String addNotice(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/notice/add";
	}
	
	@RequestMapping("/notice/{noticeId:\\d+}/edit/{base64}")
	public String editNotice(@PathVariable("noticeId")int noticeId, @PathVariable("base64")String base64, Model model) {
		NewsBulletin notice = this.opearationService.getNewsBulletin(noticeId);
		if(notice == null) {
			notice = new NewsBulletin();
		}
		model.addAttribute("notice", notice);
		model.addAttribute("base64", base64);
		return "operation/notice/edit";
	}
	
	@PostMapping("/notice/save")
	public String saveNotice(NewsBulletin notice, String base64, Model model) throws Exception {
		this.opearationService.saveNewsBulletin(notice);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/notice/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/notice/{noticeId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateNotice(@PathVariable("noticeId")int noticeId, @PathVariable("operate")int status) {
		NewsBulletin notice = this.opearationService.getNewsBulletin(noticeId);
		if(notice != null && notice.getId() > 0) {
			notice.setStatus(status);
			return this.opearationService.saveNewsBulletin(notice);
		}
		return false;
	}
	
	@RequestMapping("/news/list")
	public String getNewsList(Integer page, Integer size, Model model) {
		int total = this.opearationService.getNewsMaterialCount();
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("newses", this.opearationService.getNewsMaterialList(this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("newses", new NewsMaterial[0]);
		}
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/news/list";
	}
	
	@RequestMapping(value="/news/{page:\\d+}/{size:\\d+}")
	@ResponseBody
	public Map<String, Object> getNewsList(@PathVariable("page") int page, @PathVariable("size") int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		int total = this.opearationService.getNewsMaterialCount();
		if (total > 0) {
			result.put("total", total);
			result.put("news", this.opearationService.getNewsMaterialList(this.getOffset(page, size), this.getPageSize(size)));
		} else {
			result.put("total", 0);
			result.put("news", new NewsMaterial[0]);
		}
		result.put("page", this.getPage(page));
		result.put("size", this.getPageSize(size));
		return result;
	}
	
	@RequestMapping("/news/{base64}")
	public String forwardNewsList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getNewsList(json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/news/add/{base64}")
	public String addNews(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/news/add";
	}
	
	@RequestMapping("/news/{newsId:\\d+}/edit/{base64}")
	public String editNews(@PathVariable("newsId")int newsId, @PathVariable("base64")String base64, Model model) {
		NewsMaterial news = this.opearationService.getNewsMaterial(newsId);
		if(news == null) {
			news = new NewsMaterial();
		}
		model.addAttribute("news", news);
		model.addAttribute("base64", base64);
		return "operation/news/edit";
	}
	
	@PostMapping("/news/save")
	public String saveNews(NewsMaterial news, String base64, Model model) throws Exception {
		this.opearationService.saveNewsMaterial(news);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/news/"+StringUtils.trimToEmpty(base64);
	}
	
	@GetMapping("/news/validate")
	@ResponseBody
	public String validateNews(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		NewsMaterial news = this.opearationService.getNewsMaterial(fieldValue);
		dto.setObject(fieldId);
		if(news != null && news.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@RequestMapping("/channel/list")
	public String getChannelList(String name, Integer page, Integer size, Model model) {
		int total = this.opearationService.getChannelCount(name);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("channels", this.opearationService.getChannelList(name, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("channels", new Channel[0]);
		}
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/channel/list";
	}
	
	@RequestMapping("/channel/{base64}")
	public String forwardChannelList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getChannelList(json.getString("name"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/channel/{channelId:\\d+}/edit/{base64}")
	public String editChannel(@PathVariable("channelId")int channelId, @PathVariable("base64")String base64, Model model) {
		Channel channel = this.opearationService.getChannel(channelId);
		if(channel == null) {
			channel = new Channel();
		}
		model.addAttribute("coupons", this.couponService.getCouponList(1));
		model.addAttribute("channel", channel);
		model.addAttribute("base64", base64);
		return "operation/channel/edit";
	}
	
	@PostMapping("/channel/save")
	public String saveChannel(Channel channel, String base64, Model model) throws Exception {
		this.opearationService.saveChannel(channel);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/channel/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/channel/{channelId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateChannel(@PathVariable("channelId")int channelId, @PathVariable("operate")int status) {
		Channel channel = this.opearationService.getChannel(channelId);
		if(channel != null && channel.getId() > 0) {
			channel.setStatus(status);
			return this.opearationService.saveChannel(channel);
		}
		return false;
	}
	
	@GetMapping("/channel/validate")
	@ResponseBody
	public String validateChannel(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Channel channel = this.opearationService.getChannel(fieldValue);
		dto.setObject(fieldId);
		if(channel != null && channel.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@RequestMapping("/version/list")
	public String getAppVersionList(String version, String content, Integer page, Integer size, Model model) {
		int total = this.opearationService.getAppVersionCount(version, content);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("versions", this.opearationService.getAppVersionList(version, content, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("versions", new AppVersion[0]);
		}
		model.addAttribute("version", StringUtils.trimToEmpty(version));
		model.addAttribute("content", StringUtils.trimToEmpty(content));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "operation/version/list";
	}
	
	@RequestMapping("/version/{base64}")
	public String forwardAppVersionList(@PathVariable("base64")String base64, Model model) throws Exception {
		LOGGER.info("forward to app version list, parameter: {}", base64);
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getAppVersionList(json.getString("version"), json.getString("content"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/version/add/{base64}")
	public String addAppVersion(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "operation/version/add";
	}
	
	@RequestMapping("/version/{versionId:\\d+}/edit/{base64}")
	public String editAppVersion(@PathVariable("versionId")int versionId, @PathVariable("base64")String base64, Model model) {
		AppVersion version = this.opearationService.getAppVersion(versionId);
		if(version == null) {
			version = new AppVersion();
		}
		model.addAttribute("version", version);
		model.addAttribute("base64", base64);
		return "operation/version/edit";
	}
	
	@PostMapping("/version/save")
	public String saveAppVersion(AppVersion version, String base64, Model model) throws Exception {
		this.opearationService.saveAppVersion(version);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/operation/version/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/version/{versionId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateAppVersion(@PathVariable("versionId")int versionId, @PathVariable("operate")int status) {
		AppVersion version = this.opearationService.getAppVersion(versionId);
		if(version != null && version.getId() > 0) {
			version.setStatus(status);
			return this.opearationService.saveAppVersion(version);
		}
		return false;
	}
	
	@GetMapping("/version/validate")
	@ResponseBody
	public String validateAppVersion(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		AppVersion version = this.opearationService.getAppVersion(fieldValue);
		dto.setObject(fieldId);
		if(version != null && version.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
}
