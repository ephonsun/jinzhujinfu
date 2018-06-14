package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import com.canary.finance.domain.Customer;

public interface CustomerDao {
	Customer selectById(int id);
	Customer selectByCellphone(String cellphone);
	Customer selectByIdcard(String idcard);
	int insert(Customer customer);
	int update(Customer customer);
	List<Customer> selectInvitationList(Map<String,Object> params);
	int selectInvitationCount(Map<String,Object> params);
	
	List<Customer> queryInvitorForList(Map<String,Object> params);
	int queryInvitorForCount(Map<String,Object> params);
}