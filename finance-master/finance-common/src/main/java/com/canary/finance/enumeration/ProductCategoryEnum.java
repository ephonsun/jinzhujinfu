package com.canary.finance.enumeration;

public enum ProductCategoryEnum {
	COMMON("常规产品"),
	EXPERIENCE("体验专享"),
	NOVICE("新手专享"),
	ACTIVITY("活动专享"),
	TREASURE("金竹宝");
	
	private String property;
	
	private ProductCategoryEnum(String property){
		this.property = property;
	}
	
	@Override
	public String toString(){
		return this.property;
	}
}