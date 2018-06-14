package com.canary.finance.orm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.pojo.CustomerCouponVO;
import com.canary.finance.pojo.InvitorCouponVO;

public interface CustomerCouponDao {
	List<CustomerCoupon> queryForList(Map<String,Object> params);
	List<CustomerCouponVO> queryVOForList(@Param("customerId")int customerId);
	int queryForCount(Map<String,Object> params);
	List<CustomerCoupon> selectByCustomerIdAndStatus(@Param("customerId")int customerId, @Param("status")int status);
	CustomerCoupon selectById(int id);
	int insert(CustomerCoupon coupon);
	int insertBatch(@Param("coupons")List<CustomerCoupon> coupons);
	int update(CustomerCoupon coupon);
	int insertCustomerCoupons(@Param("customerCoupons")List<CustomerCoupon> customerCoupons);
	
	List<InvitorCouponVO> queryInvitorCouponForList(Map<String, Object> params);
	int queryInvitorCouponForCount(Map<String, Object> params);
}