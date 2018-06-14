package com.canary.finance.enumeration;

public enum OrderNOPrefixEnum {
	JZO("open account"),
	JZP("purchase"),
	JZB("payback"),
	JZM("password"),
	JZA("auth"),
	JZR("recharge");
	
	private String prefix;
	
	private OrderNOPrefixEnum(String prefix){
		this.prefix = prefix;
	}
	
	public String value(){
		return this.prefix;
	}
}
