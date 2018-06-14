package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Product;
import com.canary.finance.domain.ProductCategory;

public interface ProductService {
	List<ProductCategory> getCategoryList(int status);
	ProductCategory getCategory(int categoryId);
	
	List<Product> getProductList(int categoryId, Integer periodStart, Integer periodEnd, int offset, int size);
	int getProductCount(int categoryId, Integer periodStart, Integer periodEnd);
	Product getProduct(int productId);
	Product getTopNovice();
	List<Product> getTop4Product();
}
