package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.pojo.ExportDataVO;
import com.canary.finance.pojo.PaybackVO;

public interface CustomerOrderDao {
	List<CustomerOrder> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	CustomerOrder selectById(int id);
	CustomerOrder selectByOrderNO(String orderNO);
	int insert(CustomerOrder order);
	int update(CustomerOrder order);
	void updateBatch(@Param("orders")List<CustomerOrder> orders);
	Integer sumPrincipal(Map<String, Object> params);
	Double sumPaybackAmount(Map<String, Object> params);
	
	List<ExportDataVO> selectByProductId(@Param("productId")int productId);
	List<PaybackVO> queryPaybackForList(@Param("productId")int productId);
	List<PaybackVO> queryCouponForList(@Param("productId")int productId);
	List<PaybackVO> queryIncreaseInterestForList(@Param("productId")int productId);
	int updateCouponStatus(@Param("paybackNO")String paybackNO, @Param("couponStatus")int couponStatus);
	int updateIncreaseInterestStatus(@Param("paybackNO")String paybackNO, @Param("increaseInterestStatus")int increaseInterestStatus);
}