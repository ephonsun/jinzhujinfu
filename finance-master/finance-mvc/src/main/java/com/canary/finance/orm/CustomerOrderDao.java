package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerOrder;
import com.canary.finance.pojo.CustomerOrderVO;
import com.canary.finance.pojo.ProductOrderVO;

public interface CustomerOrderDao {
	List<ProductOrderVO> queryVOForList(Map<String, Object> params);
	int queryVOForCount(Map<String, Object> params);
	
	List<CustomerOrder> queryForList(Map<String, Object> params);
	int queryForCount(Map<String, Object> params);
	List<CustomerOrder> selectByProductId(@Param("productId") int productId);
	CustomerOrder selectById(int id);
	CustomerOrder selectByOrderNO(String orderNO);
	Integer selectSumPrincipalByCustomerId(@Param("customerId") int customerId);
	Double selectAccumulatedIncomeByCustomerId(@Param("customerId") int customerId);
	Double selectSumAssetByCustomerId(@Param("customerId") int customerId);
	Double selectYestodayIncomeByCustomerId(@Param("customerId") int customerId);
	int insert(CustomerOrder order);
	int update(CustomerOrder order);
	void updateBatch(@Param("orders")List<CustomerOrder> orders);
	List<CustomerOrder> selectProfitListByCustomerId(@Param("customerId") int customerId, @Param("offset") int offset, @Param("size") int size);
	int selectProfitCountByCustomerId(@Param("customerId") int customerId);
	List<CustomerOrder> selectRefundListByCustomerId(@Param("customerId") int customerId, @Param("offset") int offset, @Param("size") int size);
	int selectRefundCountByCustomerId(@Param("customerId") int customerId);
	
	Integer getTotalPrincipal(@Param("customerId")int customerId);
	Double getUnPaybackProfit(@Param("customerId")int customerId);
	Double getPaybackProfit(@Param("customerId")int customerId);
	List<CustomerOrderVO> queryOrderForList(Map<String, Object> params);
	CustomerOrderVO selectDetailByOrderNO(@Param("orderNO")String orderNO);
	int queryOrderForCount(Map<String, Object> params);
}