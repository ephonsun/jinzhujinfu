package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.PictureLibrary;

public interface BannerService {
	List<PictureLibrary> getWebsiteList();
	List<PictureLibrary> getMobileList();
	PictureLibrary get(int id);
}
