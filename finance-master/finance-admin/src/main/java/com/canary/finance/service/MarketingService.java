package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Faq;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.PayBank;

public interface MarketingService {
	List<Faq> getFaqList();
	List<Faq> getFaqList(int offset, int size);
	int getFaqCount();
	Faq getFaq(int faqId);
	Faq getFaq(String ask);
	boolean saveFaq(Faq faq);
	boolean deleteFaq(int faqId);
	
	List<Feedback> getFeedbackList(String contact, String content, String beginTime, String endTime, int offset, int size);
	int getFeedbackCount(String contact, String content, String beginTime, String endTime);
	boolean saveFeedback(Feedback feedback);
	
	List<PayBank> getPayBankList();
	List<PayBank> getPayBankList(int status);
	PayBank getPayBank(int bankId);
	boolean savePayBank(PayBank payBank);
}
