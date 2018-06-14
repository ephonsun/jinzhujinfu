package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Product;
import com.canary.finance.domain.ProductCategory;
import com.canary.finance.pojo.PaybackOrderVO;

public interface ProductService {
	List<ProductCategory> getCategoryList(int status);
	ProductCategory getCategory(int categoryId);
	ProductCategory getCategory(String categoryName);
	boolean saveCategory(ProductCategory category);
	
	List<Product> getProductList(String name, int categoryId, int merchantId, Integer periodStart, Integer periodEnd, int offset, int size);
	int getProductCount(String name, int categoryId, int merchantId, Integer periodStart, Integer periodEnd);
	
	List<Product> getLoanProductList(String name, int merchantId, int loanStatus, String startDate, String endDate, int offset, int size);
	int getLoanProductCount(String name, int merchantId, int loanStatus, String startDate, String endDate);
	
	List<PaybackOrderVO> getPaybackProductList(String name, int merchantId, int loanStatus, String startDate, String endDate, int offset, int size);
	int getPaybackProductCount(String name, int merchantId, int loanStatus, String startDate, String endDate);
	boolean updatePayback(int productId, int payback);
	Product getProduct(int productId);
	Product getProduct(String productName);
	boolean saveProduct(Product product);
}
