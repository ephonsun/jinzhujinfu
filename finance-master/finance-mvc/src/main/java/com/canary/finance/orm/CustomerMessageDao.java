package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerMessage;

public interface CustomerMessageDao {
	List<CustomerMessage> queryList(@Param("customerId")int customerId);
	int queryUnreadCount(@Param("customerId")int customerId);
	int read(@Param("id")int id);
	int insert(CustomerMessage message);
	int batchInsert(@Param("messages")List<CustomerMessage> messages);
}