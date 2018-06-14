package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.pojo.MaterialVO;

public interface NewsBulletinDao {
	List<NewsBulletin> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	NewsBulletin selectById(int id);
	int update(int id);
	List<NewsBulletin> selectTopN(@Param("type")int type, @Param("n") int n);
	
	List<MaterialVO> queryVOForList(Map<String, Object> params);
	int queryVOForCount(Map<String, Object> params);
	MaterialVO selectVOById(int id);
}