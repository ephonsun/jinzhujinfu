package com.canary.finance.enumeration;

public enum PurposeEnum {
	RECHARGE(0, "充值"),
	WITHDRAW(1, "提现"),
	PAY_SUCCESS(2, "购买成功"),
	PAYBACK(3, "到期回款"),
	COUPON(4, "红包"),
	FEE(5, "手续费");
	
    private final int type;
    private final String purpose;
    
    private PurposeEnum(int type, String purpose) {
        this.type = type;
        this.purpose = purpose;
    }
    
    public int getType() {
    	return this.type;
    }
    
    public String getPurpose() {
    	return this.purpose;
    }
}