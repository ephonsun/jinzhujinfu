package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.Payment;

public interface PaymentDao {
	int batchInsert(@Param("payments")List<Payment> payments);
}