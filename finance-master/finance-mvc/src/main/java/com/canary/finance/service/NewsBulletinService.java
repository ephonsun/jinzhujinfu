package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.NewsMaterial;

public interface NewsBulletinService {
	List<NewsBulletin> getNewsBulletinList(int type, int offset, int size);
	int getNewsBulletinCount(int type);
	NewsBulletin getNewsBulletin(int newsId);
	NewsMaterial getMaterial(int materialId);
	List<NewsBulletin> getTopNotice();
	List<NewsBulletin> getTopNews();
}
