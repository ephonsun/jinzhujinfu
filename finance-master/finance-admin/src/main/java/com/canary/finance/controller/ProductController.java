package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.COMMA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.canary.finance.domain.Product;
import com.canary.finance.domain.ProductCategory;
import com.canary.finance.enumeration.ProductCategoryEnum;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.service.CustomerService;
import com.canary.finance.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/category/list")
	public String getCategoryList(Model model) {
		model.addAttribute("categories", this.productService.getCategoryList(-1));
		model.addAttribute("properties", this.getCategoryProperties());
		return "product/category";
	}
	
	@PostMapping("/category")
	public String saveCategory(ProductCategory category) {
		category.setStatus(1);
		this.productService.saveCategory(category);
    	return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/product/category/list";
  	}
	
	@PostMapping("/category/{categoryId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateCategory(@PathVariable("categoryId")int categoryId, @PathVariable("operate")int status) {
		ProductCategory category = this.productService.getCategory(categoryId);
		if(category != null && category.getId() > 0) {
			category.setStatus(status);
			return this.productService.saveCategory(category);
		}
		return false;
	}
	
	@GetMapping("/category/validate")
	@ResponseBody
	public String validateCategory(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		ProductCategory category = this.productService.getCategory(fieldValue);
		dto.setObject(fieldId);
		if(category != null && category.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@RequestMapping("/list")
	public String getProductList(String name, Integer categoryId, Integer merchantId, Integer periodStart, Integer periodEnd, Integer page, Integer size, Model model) {
		if(categoryId == null) {
			categoryId = 0;
		}
		if(merchantId == null) {
			merchantId = 0;
		}
		int total = this.productService.getProductCount(name, categoryId, merchantId, periodStart, periodEnd);
		if(total > 0) {
			model.addAttribute("total", total);
			model.addAttribute("products", this.productService.getProductList(name, categoryId, merchantId, periodStart, periodEnd, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			model.addAttribute("total", 0);
			model.addAttribute("products", new Product[0]);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("systemTime", sdf.format(new Date()));
		model.addAttribute("categories", this.productService.getCategoryList(1));
		model.addAttribute("merchants", this.customerService.getMerchantList(1));
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("periodStart", periodStart);
		model.addAttribute("periodEnd", periodEnd);
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "product/list";
	}
	
	@RequestMapping("/{base64}")
	public String forwardProductList(@PathVariable("base64")String base64, Model model) throws Exception {
		JSONObject json = this.getJSONFromBase64(base64);
		return this.getProductList(json.getString("name"), json.getInteger("categoryId"), json.getInteger("merchantId"), json.getInteger("periodStart"), json.getInteger("periodEnd"), json.getInteger("page"), json.getInteger("size"), model); 
	}
	
	@RequestMapping("/add/{base64}")
	public String addProduct(@PathVariable("base64")String base64, Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("systemTime", sdf.format(new Date()));
		model.addAttribute("categories", this.productService.getCategoryList(1));
		model.addAttribute("merchants", this.customerService.getMerchantList(1));
		model.addAttribute("base64", base64);
		return "product/add";
	}
	
	@RequestMapping("/{productId:\\d+}/edit/{base64}")
	public String editProduct(@PathVariable("productId")int productId, @PathVariable("base64")String base64, Model model) {
		Product product = this.productService.getProduct(productId);
		if(product == null) {
			product = new Product();
		}
		model.addAttribute("product", product);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("systemTime", sdf.format(new Date()));
		model.addAttribute("categories", this.productService.getCategoryList(1));
		model.addAttribute("merchants", this.customerService.getMerchantList(1));
		model.addAttribute("base64", base64);
		return "product/edit";
	}
	
	
	@PostMapping("/save")
	public String saveProduct(Product product, String base64, HttpServletRequest request, Model model) {
		Map<String, String> result = upload(request);
		if(product != null && result.size() > 0) {
			String attachment = StringUtils.join(result.values().toArray(new String[0]), COMMA);
			if(StringUtils.isNotEmpty(product.getAttachment())) {
				product.setAttachment(product.getAttachment()+COMMA+attachment);
			} else {
				product.setAttachment(attachment);
			}
		}
		this.productService.saveProduct(product);
    	return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/product/"+StringUtils.trimToEmpty(base64);
  	}
	
	@PostMapping("/{productId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doOperateProduct(@PathVariable("productId")int productId, @PathVariable("operate")int status) {
		Product product = this.productService.getProduct(productId);
		if(product != null && product.getId() > 0) {
			product.setStatus(status);
			return this.productService.saveProduct(product);
		}
		return false;
	}
	
	@GetMapping("/validate")
	@ResponseBody
	public String validateProduct(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Product product = this.productService.getProduct(fieldValue);
		dto.setObject(fieldId);
		if(product != null && product.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	private Map<String, String> getCategoryProperties() {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		for(ProductCategoryEnum property : ProductCategoryEnum.values()){
			properties.put(property.name(), property.toString());
		}
		
		return properties;
	}
}
