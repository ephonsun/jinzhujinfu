package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.PictureLibrary;

public interface PictureLibraryDao {
	List<PictureLibrary> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	int insert(PictureLibrary pictureLibrary);
	int update(PictureLibrary pictureLibrary);
	PictureLibrary selectById(int id);
	PictureLibrary selectByNameAndPlatform(@Param("name")String name, @Param("platform")int platform);
}
