package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Faq;

public interface FaqDao {
	List<Faq> selectAll();
	List<Faq> queryForList(Map<String, Object> params);
	int queryForCount();
	Faq selectById(int id);
	Faq selectByAsk(@Param("ask")String ask);
	int insert(Faq faq);
	int update(Faq faq);
	int delete(int id);
}