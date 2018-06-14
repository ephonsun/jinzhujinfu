package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Product;
import com.canary.finance.domain.ProductCategory;
import com.canary.finance.orm.ProductCategoryDao;
import com.canary.finance.orm.ProductDao;
import com.canary.finance.pojo.PaybackOrderVO;
import com.canary.finance.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<PaybackOrderVO> getPaybackProductList(String name, int merchantId, int loanStatus, String startDate, String endDate, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("merchantId", merchantId);
		params.put("loanStatus", loanStatus);
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		params.put("offset", offset);
		params.put("size", size);
		return productDao.queryPaybackForList(params);
	}
	
	@Override
	public int getPaybackProductCount(String name, int merchantId, int loanStatus, String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("merchantId", merchantId);
		params.put("loanStatus", loanStatus);
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		return productDao.queryPaybackForCount(params);
	}
	
	@Override
	public boolean updatePayback(int productId, int payback) {
		return productDao.updatePayback(productId, payback) > 0 ? true : false;
	}
	
	@Override
	public List<Product> getLoanProductList(String name, int merchantId, int loanStatus, String startDate, String endDate, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("merchantId", merchantId);
		params.put("loanStatus", loanStatus);
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		params.put("offset", offset);
		params.put("size", size);
		return productDao.queryLoanForList(params);
	}
	
	
	@Override
	public int getLoanProductCount(String name, int merchantId, int loanStatus, String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("merchantId", merchantId);
		params.put("loanStatus", loanStatus);
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		return productDao.queryLoanForCount(params);
	}
	
	@Override
	public List<ProductCategory> getCategoryList(int status) {
		return this.productCategoryDao.queryForList(status);
	}

	@Override
	public ProductCategory getCategory(int categoryId) {
		return this.productCategoryDao.selectById(categoryId);
	}

	@Override
	public boolean saveCategory(ProductCategory category) {
		if(category != null && category.getId() > 0) {
			return this.productCategoryDao.update(category)>0 ? true : false;
		} else {
			return this.productCategoryDao.insert(category)>0 ? true : false;
		}
	}

	@Override
	public ProductCategory getCategory(String categoryName) {
		return this.productCategoryDao.selectByName(categoryName);
	}

	@Override
	public List<Product> getProductList(String name, int categoryId, int merchantId, Integer periodStart, Integer periodEnd, int offset, int size) {
		Map<String, Object> params = this.getParams(name, categoryId, merchantId, periodStart, periodEnd);
		params.put("offset", offset);
		params.put("size", size);
		return this.productDao.queryForList(params);
	}

	@Override
	public int getProductCount(String name, int categoryId, int merchantId, Integer periodStart, Integer periodEnd) {
		return this.productDao.queryForCount(this.getParams(name, categoryId, merchantId, periodStart, periodEnd));
	}

	@Override
	public Product getProduct(int productId) {
		return this.productDao.selectById(productId);
	}

	@Override
	public Product getProduct(String productName) {
		return this.productDao.selectByName(productName);
	}

	@Override
	public boolean saveProduct(Product product) {
		if(product != null && product.getId() > 0) {
			return this.productDao.update(product)>0 ? true : false;
		} else {
			return this.productDao.insert(product)>0 ? true : false;
		}
	}
	
	private Map<String, Object> getParams(String name, int categoryId, int merchantId, Integer periodStart, Integer periodEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("categoryId", categoryId);
		params.put("merchantId", merchantId);
		params.put("periodStart", periodStart == null ? 0 : periodStart);
		params.put("periodEnd", periodEnd == null ? 0 : periodEnd);
		return params;
	}
}
