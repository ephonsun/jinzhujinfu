package com.canary.finance.orm;

import java.util.List;

import com.canary.finance.domain.PictureLibrary;

public interface PictureLibraryDao {
	List<PictureLibrary> selectForWebsite();
	List<PictureLibrary> selectForMobile();
	PictureLibrary selectById(int id);
}
