package com.canary.finance.enumeration;

public enum CouponCategoryEnum {
	UNLIMITTED(0),
	SINGLE(1),
	MULTIPLE(2);
	
	private int category;
	
	private CouponCategoryEnum(int category){
		this.category = category;
	}
	
	public int Value(){
		return this.category;
	}
}
