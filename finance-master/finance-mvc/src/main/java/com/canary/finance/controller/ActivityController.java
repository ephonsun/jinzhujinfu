package com.canary.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.domain.Activity;
import com.canary.finance.service.ActivityService;

@Controller
@RequestMapping("/")
public class ActivityController extends BaseController {
	@Autowired
	private ActivityService activityService;
	
	@RequestMapping("activity")
	public String getActivityList(Integer page, Model model) {
		int size = 6;
		int total = this.activityService.getActivityCount();
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("activities", this.activityService.getActivityList(this.getOffset(page, size), 6));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("activities", new Activity[0]);
		}
		
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "activity";
	}
	
	@RequestMapping("/activity/{base64}")
	public String forwardActivityList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getActivityList(json.getInteger("page"), model); 
	}
	
	@RequestMapping("activity/detail/{activityId:\\d+}")
	public String getActivityDetail(@PathVariable("activityId")int activityId, Model model) {
		model.addAttribute("activity", this.activityService.getActivity(activityId));
		
		return "activity-detail";
	}
}
