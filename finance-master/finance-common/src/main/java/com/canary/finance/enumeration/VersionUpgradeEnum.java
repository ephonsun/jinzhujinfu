package com.canary.finance.enumeration;

public enum VersionUpgradeEnum {
	OPTIONAL(0),
	FORCE(1);
	
	private int type;
	
	private VersionUpgradeEnum(int type){
		this.type = type;
	}
	
	public int Value(){
		return this.type;
	}
}
