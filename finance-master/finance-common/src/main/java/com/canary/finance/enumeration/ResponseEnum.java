package com.canary.finance.enumeration;

public enum ResponseEnum {
	MOBILE_FORMAT_ERROR(401, "土豪，没有这样的手机号哦~"),
	REGISTER_FIRST(402, "该手机号还未注册，请先注册"),
	ACCOUNT_ERROR(403, "手机号或密码有误"),
	OLD_PASSWORD_ERROR(405, "土豪，原密码有误"),
	PASSWORD_CAN_NOTEQU(406, "土豪，新密码不能和原密码相同哦"),
	VALIDATE_CODE_ERROR(407, "验证码有误，请重新输入"),
	ALREADY_REGISTER(408, "该手机号已注册，可直接登录"),
	IDCARD_NAME_ERROR(409, "您输入的姓名或身份证号码有误"),
	IDCARD_BINDED(410, "该身份证已被使用"),
	PRODUCT_SOLD_OUT(411, "产品已售罄"),
	PRODUCT_BALANCE(412, "抱歉，该产品最多还可购买%s元"),
	LACK_OF_BALANCE(413, "余额不足，请充值"),
	FAIL(400, "网络连接超时，请重试"),
	NOVICE_LIMIT(401, "您已购买过新手专享产品"),
	SUCCESS(200, "成功"); 
	
	private final int code;
	private final String msg;

	ResponseEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}