package com.canary.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.canary.finance.service.MarketingService;

@Controller
@RequestMapping("/")
public class MarketingController extends BaseController {
	@Autowired
	private MarketingService marketingService;
	
	@RequestMapping("faq")
    public String getFaqList(Model model) {
		model.addAttribute("faqs",this.marketingService.getFaqList());
    	return "faq";
    }
	
	@RequestMapping("bank")
    public String getPayBankList(Model model) {
		model.addAttribute("banks",this.marketingService.getPayBankList());
    	return "bank";
    }
}
