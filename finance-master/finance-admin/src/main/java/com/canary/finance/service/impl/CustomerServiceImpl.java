package com.canary.finance.service.impl;

import static com.canary.finance.util.ConstantUtil.MIN_TIME;
import static com.canary.finance.util.ConstantUtil.MAX_TIME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.Merchant;
import com.canary.finance.orm.CustomerDao;
import com.canary.finance.orm.MerchantDao;
import com.canary.finance.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private MerchantDao merchantDao;
	
	@Override
	public List<Customer> getCustomerList(String name, String cellphone, String beginTime, String endTime, int channelId, int offset, int size) {
		Map<String, Object> params = this.getParams(name, cellphone, beginTime, endTime, channelId);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerDao.queryForList(params);
	}

	@Override
	public int getCustomerCount(String name, String cellphone, String beginTime, String endTime, int channelId) {
		return this.customerDao.queryForCount(this.getParams(name, cellphone, beginTime, endTime, channelId));
	}
	
	@Override
	public Customer getCustomer(int customerId) {
		return this.customerDao.selectById(customerId);
	}

	@Override
	public Customer getCustomer(String cellphone) {
		return this.customerDao.selectByCellphone(cellphone);
	}

	@Override
	public Customer getCustomer(String cellphone, String idcard) {
		//TODO ignore cellphone for use.
		return this.customerDao.selectByIdcard(idcard);
	}

	@Override
	public boolean saveCustomer(Customer customer) {
		if(customer != null && customer.getId() > 0) {
			return this.customerDao.update(customer)>0 ? true : false;
		} else {
			return this.customerDao.insert(customer)>0 ? true : false;
		}
	}
	
	@Override
	public List<Merchant> getMerchantList(String name, int status, int offset, int size) {
		Map<String, Object> params = this.getParams(name, status);
		params.put("offset", offset);
		params.put("size", size);
		return this.merchantDao.queryForList(params);
	}

	@Override
	public int getMerchantCount(String name, int status) {
		return this.merchantDao.queryForCount(this.getParams(name, status));
	}
	
	@Override
	public List<Merchant> getMerchantList(int status) {
		return this.merchantDao.selectByStatus(status);
	}

	@Override
	public Merchant getMerchant(int merchantId) {
		return this.merchantDao.selectById(merchantId);
	}

	@Override
	public Merchant getMerchant(String license) {
		return this.merchantDao.selectByLicense(license);
	}

	@Override
	public boolean saveMerchant(Merchant merchant) {
		if(merchant != null && merchant.getId() > 0) {
			return this.merchantDao.update(merchant)>0 ? true : false;
		} else {
			return this.merchantDao.insert(merchant)>0 ? true : false;
		}
	}
	
	private Map<String, Object> getParams(String name, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		params.put("status", status);
		return params;
	}
	
	private Map<String, Object> getParams(String name, String cellphone, String beginTime, String endTime, int channelId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)) {
			params.put("name", name);
		}
		if(StringUtils.isNotBlank(cellphone)) {
			params.put("cellphone", cellphone);
		}
		params.put("channelId", channelId);
		if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
			params.put("beginTime", beginTime+MIN_TIME);
			params.put("endTime", endTime+MAX_TIME);
		}
		return params;
	}
}
