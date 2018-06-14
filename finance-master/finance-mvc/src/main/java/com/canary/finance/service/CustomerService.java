package com.canary.finance.service;

import java.util.List;

import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.pojo.CustomerCouponVO;
import com.canary.finance.pojo.InvitorCouponVO;

public interface CustomerService {
	Customer getCustomer(int customerId);
	Customer getCustomer(String cellphone);
	Customer getCustomer(String cellphone, String idcard);
	Customer getCustomerWithIdcard(String idcard);
	boolean saveCustomer(Customer customer);
	List<Customer> getInvitationList(String cellphone, int offset, int size);
	int getInvitationCount(String cellphone);
	
	int getTotalPrincipal(int customerId);
	double getUnPaybackProfit(int customerId);
	double getPaybackProfit(int customerId);
	
	List<CustomerBalance> getCustomerBalanceList(int customerId, int offset, int size);
	int getCustomerBalanceCount(int customerId);
	
	List<CustomerCoupon> getCustomerCouponList(int customerId, int status);
	CustomerCoupon getCustomerCoupon(int couponId);
	boolean saveCustomerCoupon(CustomerCoupon coupon);
	boolean saveCustomerCoupon(List<CustomerCoupon> coupons);
	List<CustomerCoupon> getCustomerCouponList(int customerId, int status, int offset, int size);
	int getCustomerCouponCount(int customerId, int status);
	
	List<Customer> getInvitorList(String cellphone, int offset, int size);
	int getInvitorCount(String cellphone);
	List<InvitorCouponVO> getInvitorCouponForList(int customerId, int offset, int size);
	int getInvitorCouponForCount(int customerId);
	List<CustomerCouponVO> getCouponList(int customerId);
	
	List<CustomerMessage> getMessageList(int customerId);
	int getUnreadCount(int customerId);
	boolean read(int messageId);
}
