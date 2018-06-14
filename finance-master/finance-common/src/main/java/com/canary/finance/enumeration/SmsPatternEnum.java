package com.canary.finance.enumeration;

public enum SmsPatternEnum {
	REGISTER("reg", "您的注册验证码为：%s（20分钟内有效），欢迎您注册金竹金服。"),
	LOGIN_PASSWORD("loginPassword", "验证码：%s（20分钟内有效）。您正在找回登录密码，请尽快完成操作，切勿向他人提供验证码。"),
	TRADE_PASSWORD("tradePassword", "验证码：%s（20分钟内有效）。您正在找回交易密码，请尽快完成操作，切勿向他人提供验证码。"),
	PAY_SUCCESS("paySuccess", "您已成功购买%s产品：%s元。可进入“我的资产”查看交易详情。"),
	PRODUCT_INTEREST("productInterest", "您购买的%s产品今天开始起息啦~"),
	PAYBACK_SUCCESS("paybackSuccess", "您购买的%s产品已到期，%s元本息已回款至您的账户，欢迎再次购买。");
	
	private SmsPatternEnum(String key, String pattern) {
		this.key = key;
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return this.pattern;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public static SmsPatternEnum getPatternByKey(String key) {
        for (SmsPatternEnum v : SmsPatternEnum.values()) {
            if (v.getKey().equals(key)) {
                return v;
            }
        }
        return SmsPatternEnum.REGISTER;
    }
	
	private String key;
	private String pattern;
}