package com.canary.finance.enumeration;

public enum PaymentResultEnum {
	SUCCESS("SUCCESS", "支付成功"), 
	PROCESSING("PROCESSING", "支付处理中"),
	WAITING("WAITING", "等待付款 交易单创建"),
	FAILURE("FAILURE", "支付失败"), 
	REFUND("REFUND", "已退款"),
	EXCEED("EXCEED", "金额超限");

	private final String code;
	private final String msg;

	PaymentResultEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static boolean isPayResult(String code) {
		for (PaymentResultEnum v : PaymentResultEnum.values()) {
			if (v.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static String getMsgByCode(String code) {
		for (PaymentResultEnum v : PaymentResultEnum.values()) {
			if (v.getCode().equals(code)) {
				return v.getMsg();
			}
		}
		return "未知结果";
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
