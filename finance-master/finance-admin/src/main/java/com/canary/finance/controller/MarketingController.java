package com.canary.finance.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.canary.finance.domain.Admin;
import com.canary.finance.domain.Faq;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.PayBank;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.service.MarketingService;

@Controller
@RequestMapping("/service")
public class MarketingController extends BaseController {
	@Autowired
	private MarketingService marketingService;
	
	@RequestMapping("faq/list")
    public String getFaqList(Model model) {
		List<Faq> list = this.marketingService.getFaqList();
		if(list.size() > 0){
			model.addAttribute("faqs",list);
		}else{
			model.addAttribute("faqs", new Faq[0]);
		}
    	return "service/faq/list";
    }
	
	@RequestMapping("faq/add")
	public String addFaq() {
		return "service/faq/add";
	}
	
	@GetMapping("/faq/validate")
	@ResponseBody
	public String validateFaq(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Faq faq = this.marketingService.getFaq(fieldValue);
		dto.setObject(fieldId);
		if(faq != null && faq.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@PostMapping("faq/save")
	public String  saveFaq(Faq faq, Model model) {
		this.marketingService.saveFaq(faq);
        return this.getFaqList(model);
    }
	
	@RequestMapping("faq/{faqId:\\d+}/edit")
	public String editFaq(@PathVariable("faqId") int faqId, Model model) {
		model.addAttribute("faq", this.marketingService.getFaq(faqId));
		return "service/faq/edit";
	}
	
	@RequestMapping("faq/{faqId:\\d+}/view")
	public String viewFaq(@PathVariable("faqId") int faqId, Model model) {
		model.addAttribute("faq", this.marketingService.getFaq(faqId));
		return "service/faq/view";
	}
	
	@RequestMapping("faq/{faqId:\\d+}/remove")
	public String removeFaq(@PathVariable("faqId") int faqId, Model model) {
		this.marketingService.deleteFaq(faqId);
		return this.getFaqList(model);
	}
	
	@PostMapping("/faq/{faqId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateFaq(@PathVariable("faqId")int faqId, @PathVariable("operate")int status) {
		Faq faq = this.marketingService.getFaq(faqId);
		if(faq != null && faq.getId() > 0) {
			faq.setStatus(status);
			return this.marketingService.saveFaq(faq);
		}
		return false;
	}
	
	@RequestMapping("feedback/list")
	public String getFeedbackList(String contact, String content, String beginTime, String endTime, Integer page, Integer size, Model model) {
		int total = this.marketingService.getFeedbackCount(contact, content, beginTime, endTime);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("feedbacks", this.marketingService.getFeedbackList(contact, content, beginTime, endTime, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("feedbacks", new Feedback[0]);
		}
		model.addAttribute("contact", StringUtils.trimToEmpty(contact));
		model.addAttribute("content", StringUtils.trimToEmpty(content));
		model.addAttribute("beginTime", StringUtils.trimToEmpty(beginTime));
		model.addAttribute("endTime", StringUtils.trimToEmpty(endTime));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "service/feedback";
	}
	
	@RequestMapping("bank/list")
    public String getPayBankList(Model model) {
		List<PayBank> list = this.marketingService.getPayBankList();
		if(list.size() > 0) {
			model.addAttribute("banks",list);
		}else{
			model.addAttribute("banks", new PayBank[0]);
		}
    	return "service/bank";
    }
	
	@PostMapping("bank/save")
	public String saveBank(PayBank bank, Model model) {
		marketingService.savePayBank(bank);
		return this.getPayBankList(model);
	}

	@RequestMapping("bank/edit/{bankId:\\d+}")
	public String editPayBank(@PathVariable("bankId")int bankId, Model model) {
		model.addAttribute("bank", this.marketingService.getPayBank(bankId));
		return "service/edit";
	}
	
	@PostMapping("/bank/{bankId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperatePayBank(@PathVariable("bankId")int bankId, @PathVariable("operate")int status) {
		PayBank bank = this.marketingService.getPayBank(bankId);
		if(bank != null && bank.getId() > 0) {
			bank.setStatus(status);
			return this.marketingService.savePayBank(bank);
		}
		return false;
	}
}
