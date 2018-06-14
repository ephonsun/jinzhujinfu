package com.canary.finance.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.canary.finance.domain.Coupon;
import com.canary.finance.domain.CouponRule;
import com.canary.finance.enumeration.CouponRuleCategoryEnum;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.service.CouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController {
	@Autowired
	private CouponService couponService;
	
	@RequestMapping("/list")
	public String getCouponList(String name, Integer category, Integer status, Integer page, Integer size, Model model) {
		if(category == null) {
			category = -1;
		}
		if(status == null) {
			status = -1;
		}
		int total = this.couponService.getCouponCount(name, category, status);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("coupons", this.couponService.getCouponList(name, category, status, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("coupons", new Coupon[0]);
		}
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("category", category);
		model.addAttribute("status", status);
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "coupon/list";
	}
	
	@RequestMapping(value="/list/{page:\\d+}/{size:\\d+}")
	@ResponseBody
	public Map<String, Object> getCouponList(@PathVariable("page") int page, @PathVariable("size") int size) {
		Map<String, Object> result = new HashMap<String, Object>();
		int total = this.couponService.getCouponCount(null, -1, 1);
		if (total > 0) {
			result.put("total", total);
			result.put("coupons", this.couponService.getCouponList(null, -1, 1, this.getOffset(page, size), this.getPageSize(size)));
		} else {
			result.put("total", 0);
			result.put("coupons", new Coupon[0]);
		}
		result.put("page", this.getPage(page));
		result.put("size", this.getPageSize(size));
		return result;
	}
	
	@RequestMapping("/{base64}")
	public String forwardCouponList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getCouponList(json.getString("name"), json.getInteger("category"), json.getInteger("status"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/add/{base64}")
	public String addCoupon(@PathVariable("base64")String base64, Model model) {
		model.addAttribute("base64", base64);
		return "coupon/add";
	}
	
	@RequestMapping("/{couponId:\\d+}/edit/{base64}")
	public String editCoupon(@PathVariable("couponId")int couponId, @PathVariable("base64")String base64, Model model) {
		Coupon coupon = this.couponService.getCoupon(couponId);
		if(coupon == null) {
			coupon = new Coupon();
		}
		model.addAttribute("coupon", coupon);
		model.addAttribute("base64", base64);
		return "coupon/edit";
	}
	
	@PostMapping("/save")
	public String saveCoupon(Coupon coupon, String base64, Model model) throws Exception {
		this.couponService.saveCoupon(coupon);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/coupon/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/{couponId:\\d+}")
	@ResponseBody
	public Coupon getCoupon(@PathVariable("couponId")int couponId) {
		return this.couponService.getCoupon(couponId);
	}
	
	@PostMapping("/{couponId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateCoupon(@PathVariable("couponId")int couponId, @PathVariable("operate")int status) {
		Coupon coupon = this.couponService.getCoupon(couponId);
		if(coupon != null && coupon.getId() > 0) {
			coupon.setStatus(status);
			return this.couponService.saveCoupon(coupon);
		}
		return false;
	}
	
	@GetMapping("/validate")
	@ResponseBody
	public String validateCoupon(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Coupon coupon = this.couponService.getCoupon(fieldValue);
		dto.setObject(fieldId);
		if(coupon != null && coupon.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@RequestMapping("/rule")
	public String getCouponRuleList(Model model) {
		Map<Integer, String> categories = new LinkedHashMap<Integer, String>();
		for(CouponRuleCategoryEnum category : CouponRuleCategoryEnum.values()){
			categories.put(category.getValue(), category.getName());
		}
		model.addAttribute("categories", categories);
		model.addAttribute("rules", this.couponService.getCouponRuleList());
		return "coupon/rule/list";
	}
	
	@RequestMapping("/rule/{ruleId:\\d+}/edit")
	public String getCouponRule(@PathVariable("ruleId")int ruleId, Model model) {
		Map<Integer, String> categories = new LinkedHashMap<Integer, String>();
		for(CouponRuleCategoryEnum category : CouponRuleCategoryEnum.values()){
			categories.put(category.getValue(), category.getName());
		}
		model.addAttribute("categories", categories);
		model.addAttribute("rule", this.couponService.getCouponRule(ruleId));
		return "coupon/rule/edit";
	}
	
	@PostMapping("/rule/save")
	public String saveCouponRule(CouponRule rule, String base64, Model model) throws Exception {
		this.couponService.saveCouponRule(rule);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/coupon/rule/"+StringUtils.trimToEmpty(base64);
	}
	
	@PostMapping("/rule/{ruleId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateCouponRule(@PathVariable("ruleId")int ruleId, @PathVariable("operate")int status) {
		CouponRule rule = this.couponService.getCouponRule(ruleId);
		if(rule != null && rule.getId() > 0) {
			rule.setStatus(status);
			return this.couponService.saveCouponRule(rule);
		}
		return false;
	}
}
