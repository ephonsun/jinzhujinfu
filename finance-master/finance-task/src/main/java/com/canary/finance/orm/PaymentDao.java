package com.canary.finance.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.domain.Payment;

public interface PaymentDao {
	List<Payment> selectByDate(String payDate);
	List<Payment> selectByDateAndProductId(@Param("payDate")String payDate, @Param("productId")int productId);
	Payment selectByOrderNO(String orderNO);
	int delete(String orderNO);
	int deleteBatch(@Param("payments")List<Payment> payments);
	int paybackInBatch(@Param("orders")List<CustomerOrder> orders);
	int insertBalanceLogInBatch(@Param("logs")List<CustomerBalance> logs);
	int batchInsertMessages(@Param("messages")List<CustomerMessage> messages);
}