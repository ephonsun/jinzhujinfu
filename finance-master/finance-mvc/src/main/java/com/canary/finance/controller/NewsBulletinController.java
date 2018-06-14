package com.canary.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.service.NewsBulletinService;

@Controller
@RequestMapping("/")
public class NewsBulletinController extends BaseController {
	@Autowired
	private NewsBulletinService newsBulletinService;
	
	@RequestMapping("news/{type:\\d+}")
	public String getNewsBulletinList(@PathVariable("type")int type, Integer page, Integer size, Model model) {
		int total = this.newsBulletinService.getNewsBulletinCount(type);
		model.addAttribute("type", type);
		model.addAttribute("total", total);
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "news"; 
	}
	
	@RequestMapping(value="news/{type:\\d+}/content", method=RequestMethod.POST)
	public String getNewsBulletinListContent(@PathVariable("type")int type, Integer page, Integer size, Model model) {
		int total = this.newsBulletinService.getNewsBulletinCount(type);
		if(total > 0) {
			model.addAttribute("newsset", this.newsBulletinService.getNewsBulletinList(type, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("newsset", new NewsBulletin[0]);
		}
		return "content";
	}
	
	@RequestMapping("news/bulletin/detail/{newsId:\\d+}")
	public String getNewsBulletinDetail(@PathVariable("newsId")int newsId, Model model) {
		model.addAttribute("news", this.newsBulletinService.getNewsBulletin(newsId));
		
		return "news-detail";
	}
	@RequestMapping("news/detail/{newsId:\\d+}")
	public String getNewsDetail(@PathVariable("newsId")int newsId, Model model) {
		model.addAttribute("news", this.newsBulletinService.getMaterial(newsId));
		return "news-material-detail";
	}
}
