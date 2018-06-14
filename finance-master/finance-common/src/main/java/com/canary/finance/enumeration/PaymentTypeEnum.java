package com.canary.finance.enumeration;

public enum PaymentTypeEnum {
	PRIVATE(0),
	PUBLIC(1);
	
	int value;
	
	private PaymentTypeEnum(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
	
	public String toString() {
		return String.valueOf(this.value);
	}
}