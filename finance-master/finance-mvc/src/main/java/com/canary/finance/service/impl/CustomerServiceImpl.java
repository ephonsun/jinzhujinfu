package com.canary.finance.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import static com.canary.finance.util.ConstantUtil.COMMA;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canary.finance.domain.Coupon;
import com.canary.finance.domain.CouponRule;
import com.canary.finance.domain.Customer;
import com.canary.finance.domain.CustomerBalance;
import com.canary.finance.domain.CustomerCoupon;
import com.canary.finance.domain.CustomerMessage;
import com.canary.finance.enumeration.CouponRuleCategoryEnum;
import com.canary.finance.orm.CouponRuleDao;
import com.canary.finance.orm.CustomerBalanceDao;
import com.canary.finance.orm.CustomerCouponDao;
import com.canary.finance.orm.CustomerDao;
import com.canary.finance.orm.CustomerMessageDao;
import com.canary.finance.orm.CustomerOrderDao;
import com.canary.finance.pojo.CustomerCouponVO;
import com.canary.finance.pojo.InvitorCouponVO;
import com.canary.finance.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private CouponRuleDao couponRuleDao;
	@Autowired
	private CustomerOrderDao customerOrderDao;
	@Autowired
	private CustomerCouponDao customerCouponDao;
	@Autowired
	private CustomerMessageDao customerMessageDao;
	@Autowired
	private CustomerBalanceDao customerBalanceDao;
	
	@Override
	public List<CustomerMessage> getMessageList(int customerId) {
		return customerMessageDao.queryList(customerId);
	}

	@Override
	public int getUnreadCount(int customerId) {
		return customerMessageDao.queryUnreadCount(customerId);
	}

	@Override
	public boolean read(int messageId) {
		return customerMessageDao.read(messageId) > 0 ? true : false;
	}
	
	@Override
	public List<CustomerCouponVO> getCouponList(int customerId) {
		return customerCouponDao.queryVOForList(customerId);
	}
	
	@Override
	public List<InvitorCouponVO> getInvitorCouponForList(int customerId, int offset, int size) {
		if (customerId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerId", customerId);
			params.put("offset", offset);
			params.put("size", size);
			return customerCouponDao.queryInvitorCouponForList(params);
		}
		return null;
	}
	
	@Override
	public int getInvitorCouponForCount(int customerId) {
		if (customerId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("customerId", customerId);
			return customerCouponDao.queryInvitorCouponForCount(params);
		}
		return 0;
	}
	
	@Override
	public List<Customer> getInvitorList(String cellphone, int offset, int size) {
		if (StringUtils.isNotBlank(cellphone)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cellphone", cellphone);
			params.put("offset", offset);
			params.put("size", size);
			return this.customerDao.queryInvitorForList(params);
		}
		return null;
	}
	
	@Override
	public int getInvitorCount(String cellphone) {
		if (StringUtils.isNotBlank(cellphone)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cellphone", cellphone);
			return this.customerDao.queryInvitorForCount(params);
		}
		return 0;
	}
	
	@Override
	public int getTotalPrincipal(int customerId) {
		Integer amount = customerOrderDao.getTotalPrincipal(customerId);
		return amount == null ? 0 : amount;
	}
	
	@Override
	public double getUnPaybackProfit(int customerId) {
		Double profit = customerOrderDao.getUnPaybackProfit(customerId);
		return profit == null ? 0 : profit;
	}
	
	@Override
	public double getPaybackProfit(int customerId) {
		Double profit = customerOrderDao.getPaybackProfit(customerId);
		return profit == null ? 0 : profit;
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
	public Customer getCustomerWithIdcard(String idcard) {
		return this.customerDao.selectByIdcard(idcard);
	}

	@Override
	public boolean saveCustomer(Customer customer) {
		if(customer != null && customer.getId() > 0) {
			return this.customerDao.update(customer)>0 ? true : false;
		} else {
			Customer cust = this.customerDao.selectByCellphone(customer.getCellphone());
			if (cust == null) {
				boolean resul = this.customerDao.insert(customer)>0 ? true : false;
				if (!resul) {
					return false;
				}
				cust = this.customerDao.selectByCellphone(customer.getCellphone());
				if (cust == null) {
					return false;
				}
				CouponRule regRule = couponRuleDao.selectByCategory(CouponRuleCategoryEnum.REGISTERED.getValue());
				saveCoupons(regRule, cust, 0);
				if (StringUtils.isNotBlank(cust.getInviterPhone())) {
					Customer friend = customerDao.selectByCellphone(cust.getInviterPhone());
					if (friend != null) {
						CouponRule inviterRule = couponRuleDao.selectByCategory(CouponRuleCategoryEnum.BEINVITE.getValue());
						saveCoupons(inviterRule, cust, 0);
						CouponRule invitorRule = couponRuleDao.selectByCategory(CouponRuleCategoryEnum.INVITATION.getValue());
						saveCoupons(invitorRule, friend, cust.getId());
					}
				}
				return true;
			}
			return false;
		}
	}
	
	private boolean saveCoupons(CouponRule regRule, Customer customer, int invitorId) {
		if (regRule != null && StringUtils.isNotBlank(regRule.getCouponIds()) && StringUtils.isNotBlank(regRule.getCouponAmounts())) {
			String[] couponIds = StringUtils.split(regRule.getCouponIds(), COMMA);
			String[] couponAmounts = StringUtils.split(regRule.getCouponAmounts(), COMMA);
			if (couponIds != null && couponAmounts != null && couponAmounts.length == couponIds.length) {
				List<CustomerCoupon> coupons = new ArrayList<CustomerCoupon>();
				for (int i = 0; i < couponIds.length; i++) {
					try {
						CustomerCoupon customerCoupon = new CustomerCoupon();
						customerCoupon.setCustomerId(customer.getId());
						Coupon coupon = new Coupon();
						coupon.setId(Integer.parseInt(couponIds[i]));
						customerCoupon.setInvitorId(invitorId);
						customerCoupon.setCoupon(coupon);
						customerCoupon.setAmount(Integer.parseInt(couponAmounts[i]));
						customerCoupon.setCreateTime(Calendar.getInstance().getTime());
						coupons.add(customerCoupon);
					} catch (Exception e) {
						continue;
					}
				}
				return customerCouponDao.insertCustomerCoupons(coupons) > 0 ? true : false;
			}
		}
		return true;
	}
	
	@Override
	public List<Customer> getInvitationList(String cellphone, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cellphone", cellphone);
		params.put("offset", offset);
		params.put("size", size);
		
		return this.customerDao.selectInvitationList(params);
	}
	
	@Override
	public int getInvitationCount(String cellphone) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cellphone", cellphone);
		return this.customerDao.selectInvitationCount(params);
	}

	@Override
	public List<CustomerBalance> getCustomerBalanceList(int customerId, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerBalanceDao.queryForList(params);
	}

	@Override
	public int getCustomerBalanceCount(int customerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		return this.customerBalanceDao.queryForCount(params);
	}
	
	@Override
	public List<CustomerCoupon> getCustomerCouponList(int customerId, int status) {
		return this.customerCouponDao.selectByCustomerIdAndStatus(customerId, status);
	}

	@Override
	public CustomerCoupon getCustomerCoupon(int couponId) {
		return this.customerCouponDao.selectById(couponId);
	}

	@Override
	public boolean saveCustomerCoupon(CustomerCoupon coupon) {
		if(coupon != null && coupon.getId() > 0) {
			return this.customerCouponDao.update(coupon)>0 ? true : false;
		} else {
			return this.customerCouponDao.insert(coupon)>0 ? true : false;
		}
	}

	@Override
	public boolean saveCustomerCoupon(List<CustomerCoupon> coupons) {
		if(coupons != null && coupons.size() > 0) {
			return this.customerCouponDao.insertBatch(coupons)>0 ? true : false;
		}
		return false;
	}

	@Override
	public List<CustomerCoupon> getCustomerCouponList(int customerId, int status, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("status", status);
		params.put("offset", offset);
		params.put("size", size);
		return this.customerCouponDao.queryForList(params);
	}

	@Override
	public int getCustomerCouponCount(int customerId, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerId", customerId);
		params.put("status", status);
		return this.customerCouponDao.queryForCount(params);
	}
}