package com.canary.finance.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="canary.finance")
public class CanaryFinanceProperties {
	private long expireInMillis;
	private long refreshOnMillis;
	private String issuer;
	private String privateKey;
	private long workerId;
	private long datacenterId;
	private String remotingUrl;
	private String huaxingSmsUri;
	private String flowSmsUrl;
	private String huaxingSignature;
	private String allocateFundsUrl;
	private String accountBalanceUrl;
	private String couponAccount;
	private String feeAccount;
	private List<String> ignoreUri;

	public long getExpireInMillis() {
		return expireInMillis;
	}

	public void setExpireInMillis(long expireInMillis) {
		this.expireInMillis = expireInMillis;
	}

	public long getRefreshOnMillis() {
		return refreshOnMillis;
	}

	public void setRefreshOnMillis(long refreshOnMillis) {
		this.refreshOnMillis = refreshOnMillis;
	}
	
	public String getCouponAccount() {
		return couponAccount;
	}

	public void setCouponAccount(String couponAccount) {
		this.couponAccount = couponAccount;
	}

	public String getFeeAccount() {
		return feeAccount;
	}

	public void setFeeAccount(String feeAccount) {
		this.feeAccount = feeAccount;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
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

	public String getAllocateFundsUrl() {
		return allocateFundsUrl;
	}

	public void setAllocateFundsUrl(String allocateFundsUrl) {
		this.allocateFundsUrl = allocateFundsUrl;
	}

	public String getAccountBalanceUrl() {
		return accountBalanceUrl;
	}

	public void setAccountBalanceUrl(String accountBalanceUrl) {
		this.accountBalanceUrl = accountBalanceUrl;
	}

	public String getRemotingUrl() {
		return remotingUrl;
	}

	public void setRemotingUrl(String remotingUrl) {
		this.remotingUrl = remotingUrl;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}

	public List<String> getIgnoreUri() {
		return ignoreUri;
	}

	public void setIgnoreUri(List<String> ignoreUri) {
		this.ignoreUri = ignoreUri;
	}
}
