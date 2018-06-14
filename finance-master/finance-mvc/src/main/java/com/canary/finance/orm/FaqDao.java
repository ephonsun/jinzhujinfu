package com.canary.finance.orm;

import java.util.List;

import com.canary.finance.domain.Faq;

public interface FaqDao {
	List<Faq> selectAll();
	Faq selectById(int id);
}