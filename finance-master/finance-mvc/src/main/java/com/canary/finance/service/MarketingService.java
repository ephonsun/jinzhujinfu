package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.BranchBank;
import com.canary.finance.domain.Faq;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.PayBank;
import com.canary.finance.domain.RegionCode;

public interface MarketingService {
	List<Faq> getFaqList();
	Faq getFaq(int faqId);
	
	boolean saveFeedback(Feedback feedback);
	
	List<PayBank> getPayBankList();
	PayBank getPayBank(int bankId);
	PayBank getPayBank(String bankNO);
	List<RegionCode> getProvinces();
	List<RegionCode> getCities(String provinceId);
	BranchBank getBranchBank(String bankNO, String cityId);
}
