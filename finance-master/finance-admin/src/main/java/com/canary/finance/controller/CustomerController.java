package com.canary.finance.controller;

import org.apache.commons.lang.StringUtils;
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
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.Merchant;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.MarketingService;
import com.canary.finance.service.OperationService;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private MarketingService marketingService;
	
	@RequestMapping("/investor/list")
	public String getInvestorList(String name, String cellphone, Integer channelId, String beginTime, String endTime, Integer page, Integer size, Model model){
		if(channelId == null) {
			channelId = -1;
		}
		int total = this.customerService.getCustomerCount(name, cellphone, beginTime, endTime, channelId);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("customers", this.customerService.getCustomerList(name, cellphone, beginTime, endTime, channelId, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("customers", new Customer[0]);
		}
		model.addAttribute("channels", this.operationService.getChannelList(1));
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("cellphone", StringUtils.trimToEmpty(cellphone));
		model.addAttribute("channelId", channelId);
		model.addAttribute("beginTime", StringUtils.trimToEmpty(beginTime));
		model.addAttribute("endTime", StringUtils.trimToEmpty(endTime));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "customer/investor/list";
	}
	
	@RequestMapping("/financier/list")
	public String getFinancierList(String name, Integer page, Integer size, Model model){
		int status = -1;
		int total = this.customerService.getMerchantCount(name, status);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("merchants", this.customerService.getMerchantList(name, status, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("merchants", new Merchant[0]);
		}
		model.addAttribute("financiers", this.customerService.getMerchantList(1));
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "customer/financier/list";
	}
	
	@RequestMapping("/financier/{base64}")
	public String forwardFinancierList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getFinancierList(json.getString("name"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/financier/add/{base64}")
	public String addFinancier(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("banks", this.marketingService.getPayBankList(1));
		model.addAttribute("base64", base64);
		return "customer/financier/add";
	}
	
	@RequestMapping("/financier/{financierId:\\d+}/edit/{base64}")
	public String editFinancier(@PathVariable("financierId")int financierId, @PathVariable("base64")String base64, Model model) {
		Merchant merchant = this.customerService.getMerchant(financierId);
		if(merchant == null) {
			merchant = new Merchant();
		}
		model.addAttribute("financier", merchant);
		model.addAttribute("banks", this.marketingService.getPayBankList(1));
		model.addAttribute("base64", base64);
		return "customer/financier/edit";
	}
	
	@RequestMapping("/financier/{financierId:\\d+}/detail/{base64}")
	public String detailFinancier(@PathVariable("financierId")int financierId, @PathVariable("base64")String base64, Model model) {
		Merchant merchant = this.customerService.getMerchant(financierId);
		if(merchant == null) {
			merchant = new Merchant();
		}
		model.addAttribute("financier", merchant);
		model.addAttribute("banks", this.marketingService.getPayBankList(1));
		model.addAttribute("category", "detail");
		model.addAttribute("base64", base64);
		return "customer/financier/edit";
	}
	
	@PostMapping("/financier/save")
	public String saveFinancier(Merchant merchant, String base64, Model model) throws Exception {
		this.customerService.saveMerchant(merchant);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/customer/financier/"+StringUtils.trimToEmpty(base64);
	}
	
	@GetMapping("/financier/validate")
	@ResponseBody
	public String validateFinancier(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Merchant merchant = this.customerService.getMerchant(fieldValue);
		dto.setObject(fieldId);
		if(merchant != null && merchant.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@PostMapping("/financier/{financierId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateFinancier(@PathVariable("financierId")int financierId, @PathVariable("operate")int status) {
		Merchant merchant = this.customerService.getMerchant(financierId);
		if(merchant != null && merchant.getId() > 0) {
			merchant.setStatus(status);
			return this.customerService.saveMerchant(merchant);
		}
		return false;
	}
}
