package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Channel;

public interface ChannelDao {
	List<Channel> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	List<Channel> selectByStatus(int status);
	Channel selectById(int id);
	Channel selectByName(String name);
	int insert(Channel channel);
	int update(Channel channel);
}