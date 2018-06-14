package com.canary.finance.enumeration;

public enum ChannelCategoryEnum {
	REGISTER(0),
	DOWNLOAD(1),
	SPREAD(2);
	
	private int category;
	
	private ChannelCategoryEnum(int category){
		this.category = category;
	}
	
	public int Value(){
		return this.category;
	}
}
