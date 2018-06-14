package com.canary.finance.enumeration;

public enum CouponRuleCategoryEnum {
	REGISTERED(1, "注册赠送"),
	FIRSTTRADE(2, "首次购买赠送"),
	INVITATION(3, "邀请好友赠送"),
	BEINVITE(4, "被邀请赠送"),
	ACTIVITY(5, "活动红包"),
	SHARE(6, "分享赠送"),
	HOLIDAY(7, "节日赠送"),
	BIRTHDAY(8, "生日赠送" ),
	VOUCHER(9, "红包兑换券"),
	PAYBACK_COUPON(10, "回款奖励");
	
	private final int value;
	private final String name;
	
	CouponRuleCategoryEnum(int value, String name){
		this.value = value;
		this.name = name;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String getName(){
		return this.name;
	}
}
