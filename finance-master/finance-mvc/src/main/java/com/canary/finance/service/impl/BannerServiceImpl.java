package com.canary.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.orm.PictureLibraryDao;
import com.canary.finance.service.BannerService;

@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	private PictureLibraryDao pictureLibraryDao;
	
	@Override
	public List<PictureLibrary> getWebsiteList() {
		return this.pictureLibraryDao.selectForWebsite();
	}

	@Override
	public List<PictureLibrary> getMobileList() {
		return this.pictureLibraryDao.selectForMobile();
	}

	@Override
	public PictureLibrary get(int id) {
		return this.pictureLibraryDao.selectById(id);
	}

}
