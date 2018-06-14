package com.canary.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Faq;
import com.canary.finance.domain.Feedback;
import com.canary.finance.domain.PayBank;
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
	
	@Override
	public List<Faq> getFaqList() {
		return this.faqDao.selectAll();
	}

	@Override
	public List<Faq> getFaqList(int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("size", size);
		return this.faqDao.queryForList(params);
	}

	@Override
	public int getFaqCount() {
		return this.faqDao.queryForCount();
	}
	
	@Override
	public Faq getFaq(int faqId) {
		return this.faqDao.selectById(faqId);
	}

	@Override
	public Faq getFaq(String ask) {
		return this.faqDao.selectByAsk(ask);
	}

	@Override
	public boolean saveFaq(Faq faq) {
		if(faq != null && faq.getId() > 0) {
			return this.faqDao.update(faq)>0 ? true : false;
		} else {
			return this.faqDao.insert(faq)>0 ? true : false;
		}
	}

	@Override
	public boolean deleteFaq(int faqId) {
		return this.faqDao.delete(faqId)>0 ? true : false;
	}

	@Override
	public List<Feedback> getFeedbackList(String contact, String content, String beginTime, String endTime, int offset, int size) {
		Map<String, Object> params = this.getParams(contact, content, beginTime, endTime);
		params.put("offset", offset);
		params.put("size", size);
		return this.feedbackDao.queryForList(params);
	}
	
	@Override
	public int getFeedbackCount(String contact, String content, String beginTime, String endTime) {
		return this.feedbackDao.queryForCount(this.getParams(contact, content, beginTime, endTime));
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
		return this.payBankDao.selectAll();
	}
	
	@Override
	public List<PayBank> getPayBankList(int status) {
		return this.payBankDao.selectByStatus(status);
	}

	@Override
	public boolean savePayBank(PayBank payBank) {
		if(payBank != null && payBank.getId() > 0) {
			return this.payBankDao.update(payBank)>0 ? true : false;
		}
		return false;
	}

	@Override
	public PayBank getPayBank(int bankId) {
		return this.payBankDao.selectById(bankId);
	}
	
	private Map<String, Object> getParams(String contact, String content, String beginTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(contact)) {
			params.put("contact", contact);
		}
		if(StringUtils.isNotBlank(content)) {
			params.put("content", content);
		}
		if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
			params.put("beginTime", beginTime);
			params.put("endTime", endTime);
		}
		return params;
	}

}
