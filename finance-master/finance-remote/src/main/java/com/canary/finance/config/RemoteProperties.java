package com.canary.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="canary.finance.remote")
public class RemoteProperties {
	private String serverIp;
	private int serverPort;
	private String serverError;
	private String notAcceptable;
	private String defaultMessage;
	private String smsHuaxingUrl;
	private String smsHuaxingUser;
	private String smsHuaxingPassword;
	private String smsHuaxingAction;
	private String fuiouVersion;
	private String merchantCode;
	private String openAccountUrl;
	private String accountBalanceUrl;
	private String preAuthUrl;
	private String transferAccountUrl;
	private String allocateFundsUrl;
	private String jpushApikey;
	private String jpushSecretkey;
	private String jpushIosProduction;
	private String flowAccount;
	private String flowPassword;
	private String flowUrl;
	
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	public String getPreAuthUrl() {
		return preAuthUrl;
	}

	public void setPreAuthUrl(String preAuthUrl) {
		this.preAuthUrl = preAuthUrl;
	}
	
	public String getAllocateFundsUrl() {
		return allocateFundsUrl;
	}

	public void setAllocateFundsUrl(String allocateFundsUrl) {
		this.allocateFundsUrl = allocateFundsUrl;
	}

	public String getTransferAccountUrl() {
		return transferAccountUrl;
	}

	public void setTransferAccountUrl(String transferAccountUrl) {
		this.transferAccountUrl = transferAccountUrl;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public String getJpushApikey() {
		return jpushApikey;
	}

	public void setJpushApikey(String jpushApikey) {
		this.jpushApikey = jpushApikey;
	}

	public String getJpushSecretkey() {
		return jpushSecretkey;
	}

	public void setJpushSecretkey(String jpushSecretkey) {
		this.jpushSecretkey = jpushSecretkey;
	}

	public String getJpushIosProduction() {
		return jpushIosProduction;
	}

	public void setJpushIosProduction(String jpushIosProduction) {
		this.jpushIosProduction = jpushIosProduction;
	}

	public String getFuiouVersion() {
		return fuiouVersion;
	}

	public void setFuiouVersion(String fuiouVersion) {
		this.fuiouVersion = fuiouVersion;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOpenAccountUrl() {
		return openAccountUrl;
	}

	public void setOpenAccountUrl(String openAccountUrl) {
		this.openAccountUrl = openAccountUrl;
	}

	public String getServerError() {
		return serverError;
	}

	public void setServerError(String serverError) {
		this.serverError = serverError;
	}

	public String getNotAcceptable() {
		return notAcceptable;
	}

	public void setNotAcceptable(String notAcceptable) {
		this.notAcceptable = notAcceptable;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String getSmsHuaxingUrl() {
		return smsHuaxingUrl;
	}

	public void setSmsHuaxingUrl(String smsHuaxingUrl) {
		this.smsHuaxingUrl = smsHuaxingUrl;
	}

	public String getSmsHuaxingUser() {
		return smsHuaxingUser;
	}

	public void setSmsHuaxingUser(String smsHuaxingUser) {
		this.smsHuaxingUser = smsHuaxingUser;
	}

	public String getSmsHuaxingPassword() {
		return smsHuaxingPassword;
	}

	public void setSmsHuaxingPassword(String smsHuaxingPassword) {
		this.smsHuaxingPassword = smsHuaxingPassword;
	}

	public String getSmsHuaxingAction() {
		return smsHuaxingAction;
	}

	public void setSmsHuaxingAction(String smsHuaxingAction) {
		this.smsHuaxingAction = smsHuaxingAction;
	}

	public String getAccountBalanceUrl() {
		return accountBalanceUrl;
	}

	public void setAccountBalanceUrl(String accountBalanceUrl) {
		this.accountBalanceUrl = accountBalanceUrl;
	}

	public String getFlowAccount() {
		return flowAccount;
	}

	public void setFlowAccount(String flowAccount) {
		this.flowAccount = flowAccount;
	}

	public String getFlowPassword() {
		return flowPassword;
	}

	public void setFlowPassword(String flowPassword) {
		this.flowPassword = flowPassword;
	}

	public String getFlowUrl() {
		return flowUrl;
	}

	public void setFlowUrl(String flowUrl) {
		this.flowUrl = flowUrl;
	}
}
