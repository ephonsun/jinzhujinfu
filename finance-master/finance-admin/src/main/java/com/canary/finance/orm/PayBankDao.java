package com.canary.finance.orm;

import java.util.List;

import com.canary.finance.domain.PayBank;

public interface PayBankDao {
	List<PayBank> selectAll();
	List<PayBank> selectByStatus(int status);
	PayBank selectById(int id);
	int update(PayBank payBank);
}