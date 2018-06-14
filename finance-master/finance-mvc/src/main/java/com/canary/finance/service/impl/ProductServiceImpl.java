package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Product;
import com.canary.finance.domain.ProductCategory;
import com.canary.finance.orm.ProductCategoryDao;
import com.canary.finance.orm.ProductDao;
import com.canary.finance.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductCategory> getCategoryList(int status) {
		return this.productCategoryDao.queryForList(status);
	}

	@Override
	public ProductCategory getCategory(int categoryId) {
		return this.productCategoryDao.selectById(categoryId);
	}

	@Override
	public List<Product> getProductList(int categoryId, Integer periodStart, Integer periodEnd, int offset, int size) {
		Map<String, Object> params = this.getParams(categoryId, periodStart, periodEnd);
		params.put("offset", offset);
		params.put("size", size);
		return this.productDao.queryForList(params);
	}

	@Override
	public int getProductCount(int categoryId, Integer periodStart, Integer periodEnd) {
		return this.productDao.queryForCount(this.getParams(categoryId, periodStart, periodEnd));
	}

	@Override
	public Product getProduct(int productId) {
		return this.productDao.selectById(productId);
	}
	
	@Override
	public Product getTopNovice() {
		return this.productDao.selectTopNovice();
	}

	@Override
	public List<Product> getTop4Product() {
		return this.productDao.selectTop4();
	}
	
	private Map<String, Object> getParams(int categoryId, Integer periodStart, Integer periodEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("categoryId", categoryId);
		if(periodStart != null && periodEnd != null && periodEnd > periodStart) {
			params.put("periodStart", periodStart);
			params.put("periodEnd", periodEnd);
		}
		return params;
	}
}
