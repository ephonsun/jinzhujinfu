package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Product;
import com.canary.finance.pojo.PaybackOrderVO;


public interface ProductDao{
	List<Product> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	List<Product> queryLoanForList(Map<String, Object> params);
	int queryLoanForCount(Map<String, Object> params);
	
	List<PaybackOrderVO> queryPaybackForList(Map<String, Object> params);
	int queryPaybackForCount(Map<String, Object> params);
	Product selectById(int id);
	Product selectByName(String name);
	int insert(Product product);
	int update(Product product);
	
	int updatePayback(@Param("productId")int productId, @Param("payback")int payback);
}