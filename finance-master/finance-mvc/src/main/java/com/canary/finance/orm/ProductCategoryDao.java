package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.ProductCategory;

public interface ProductCategoryDao {
	List<ProductCategory> queryForList(@Param("status")int status);
	List<ProductCategory> selectByProperty(@Param("property")String property);
	int queryForCountByProperty(String property);
	int insert(ProductCategory productCategory);
	int update(ProductCategory productCategory);
	ProductCategory selectById(int id);
	ProductCategory selectByName(String name);
}