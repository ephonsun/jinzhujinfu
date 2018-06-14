package com.canary.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="canary.finance")
public class CanaryFinanceProperties {
	private String instantpay;
	private String instantpayConfirm;
	private String instantpayQuery;
	private String instantpayNotify;
	private String instantpayDate;
	private String lianlianPublicKey;
	private String ourPrivateKey;
	private String remotingUrl;
	private String huaxingSmsUri;
	private String flowSmsUrl;
	private String huaxingSignature;
	private String allocateFundsUrl;
	private String accountBalanceUrl;
	
	public String getInstantpay() {
		return instantpay;
	}
	
	public void setInstantpay(String instantpay) {
		this.instantpay = instantpay;
	}
	
	public String getAccountBalanceUrl() {
		return accountBalanceUrl;
	}

	public void setAccountBalanceUrl(String accountBalanceUrl) {
		this.accountBalanceUrl = accountBalanceUrl;
	}

	public String getInstantpayConfirm() {
		return instantpayConfirm;
	}

	public void setInstantpayConfirm(String instantpayConfirm) {
		this.instantpayConfirm = instantpayConfirm;
	}

	public String getInstantpayQuery() {
		return instantpayQuery;
	}

	public void setInstantpayQuery(String instantpayQuery) {
		this.instantpayQuery = instantpayQuery;
	}

	public String getInstantpayNotify() {
		return instantpayNotify;
	}
	
	public String getHuaxingSmsUri() {
		return huaxingSmsUri;
	}

	public void setHuaxingSmsUri(String huaxingSmsUri) {
		this.huaxingSmsUri = huaxingSmsUri;
	}

	public String getFlowSmsUrl() {
		return flowSmsUrl;
	}

	public void setFlowSmsUrl(String flowSmsUrl) {
		this.flowSmsUrl = flowSmsUrl;
	}

	public String getHuaxingSignature() {
		return huaxingSignature;
	}

	public void setHuaxingSignature(String huaxingSignature) {
		this.huaxingSignature = huaxingSignature;
	}

	public void setInstantpayNotify(String instantpayNotify) {
		this.instantpayNotify = instantpayNotify;
	}

	public String getInstantpayDate() {
		return instantpayDate;
	}

	public void setInstantpayDate(String instantpayDate) {
		this.instantpayDate = instantpayDate;
	}

	public String getLianlianPublicKey() {
		return lianlianPublicKey;
	}

	public void setLianlianPublicKey(String lianlianPublicKey) {
		this.lianlianPublicKey = lianlianPublicKey;
	}

	public String getOurPrivateKey() {
		return ourPrivateKey;
	}

	public void setOurPrivateKey(String ourPrivateKey) {
		this.ourPrivateKey = ourPrivateKey;
	}

	public String getRemotingUrl() {
		return remotingUrl;
	}

	public void setRemotingUrl(String remotingUrl) {
		this.remotingUrl = remotingUrl;
	}

	public String getAllocateFundsUrl() {
		return allocateFundsUrl;
	}

	public void setAllocateFundsUrl(String allocateFundsUrl) {
		this.allocateFundsUrl = allocateFundsUrl;
	}
}
