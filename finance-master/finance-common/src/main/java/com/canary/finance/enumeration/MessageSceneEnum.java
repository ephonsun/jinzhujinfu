package com.canary.finance.enumeration;

public enum MessageSceneEnum {
	PAY_SUCCESS(1, "购买成功"),
	PRODUCT_INTEREST(2, "产品起息"),
	PAYBACK_SUCCESS(4, "回款成功");
	
	private final int scene;
	private final String msg;
	
	private MessageSceneEnum(int scene, String msg) {
		this.scene = scene;
		this.msg = msg;
	}
	
	public int getScene() {
		return this.scene;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public static String getMsg(int scene) {
		for (MessageSceneEnum s : MessageSceneEnum.values()) {
			if (s.getScene() == scene) {
				return s.getMsg();
			}
		}
		return "";
	}
}