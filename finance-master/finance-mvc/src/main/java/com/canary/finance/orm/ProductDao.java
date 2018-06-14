package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Product;

public interface ProductDao{
	List<Product> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	Product selectById(int id);
	int update(Product product);
	Product selectTopNovice();
	List<Product> selectTop4();
}