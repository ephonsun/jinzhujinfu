package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.Merchant;

public interface CustomerService {
	List<Customer> getCustomerList(String name, String cellphone, String beginTime, String endTime, int channelId, int offset, int size);
	int getCustomerCount(String name, String cellphone, String beginTime, String endTime, int channelId);
	Customer getCustomer(int customerId);
	Customer getCustomer(String cellphone);
	Customer getCustomer(String cellphone, String idcard);
	boolean saveCustomer(Customer customer);
	
	List<Merchant> getMerchantList(String name, int status, int offset, int size);
	int getMerchantCount(String name, int status);
	List<Merchant> getMerchantList(int status);
	Merchant getMerchant(int merchantId);
	Merchant getMerchant(String businessLicenseNO);
	boolean saveMerchant(Merchant merchant);
}
