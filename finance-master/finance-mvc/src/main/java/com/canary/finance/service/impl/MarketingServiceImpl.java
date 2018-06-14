package com.canary.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.BranchBank;
import com.canary.finance.domain.Faq;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.PayBank;
import com.canary.finance.domain.RegionCode;
import com.canary.finance.orm.BranchBankDao;
import com.canary.finance.orm.FaqDao;
import com.canary.finance.orm.FeedbackDao;
import com.canary.finance.orm.PayBankDao;
import com.canary.finance.service.MarketingService;

@Service
public class MarketingServiceImpl implements MarketingService {
	@Autowired
	private FaqDao faqDao;
	@Autowired
	private FeedbackDao feedbackDao;
	@Autowired
	private PayBankDao payBankDao;
	@Autowired
	private BranchBankDao branchBankDao;
	
	@Override
	public List<RegionCode> getProvinces() {
		return payBankDao.queryForProvinces();
	}
	
	@Override
	public List<RegionCode> getCities(String provinceId) {
		return payBankDao.queryForCities(provinceId);
	}
	
	@Override
	public List<Faq> getFaqList() {
		return this.faqDao.selectAll();
	}

	@Override
	public Faq getFaq(int faqId) {
		return this.faqDao.selectById(faqId);
	}

	@Override
	public boolean saveFeedback(Feedback feedback) {
		if(feedback != null && feedback.getId() > 0) {
			//TODO donothing.
			return true;
		} else {
			return this.feedbackDao.insert(feedback)>0 ? true : false;
		}
	}
	
	@Override
	public List<PayBank> getPayBankList() {
		return this.payBankDao.selectByStatus(1);
	}

	@Override
	public PayBank getPayBank(int bankId) {
		return this.payBankDao.selectById(bankId);
	}
	
	@Override
	public PayBank getPayBank(String bankNO) {
		return this.payBankDao.selectByBankNO(bankNO);
	}
	
	@Override
	public BranchBank getBranchBank(String bankNO, String cityId) {
		return this.branchBankDao.selectByBankNOAndCityId(bankNO, cityId);
	}
}
