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
	private List<String> securityUri;
	private String remotingUrl;
	private String huaxingSmsUri;
	private String flowSmsUrl;
	private String huaxingSignature;
	private String openAccountUrl;
	private String accountBalanceUrl;
	private String webUrl;
	private String merchantCode;
	private String appTradePasswordSetUrl;
	private String webTradePasswordSetUrl;
	private String appWithdrawUrl;
	private String webWithdrawUrl;
	private String appRechargeUrl;
	private String webRechargeUrl;
	private String preAuthUrl;
	private String transferAccountUrl;

	public long getExpireInMillis() {
		return expireInMillis;
	}

	public void setExpireInMillis(long expireInMillis) {
		this.expireInMillis = expireInMillis;
	}

	public String getFlowSmsUrl() {
		return flowSmsUrl;
	}

	public void setFlowSmsUrl(String flowSmsUrl) {
		this.flowSmsUrl = flowSmsUrl;
	}

	public String getPreAuthUrl() {
		return preAuthUrl;
	}

	public void setPreAuthUrl(String preAuthUrl) {
		this.preAuthUrl = preAuthUrl;
	}
	
	public String getAppWithdrawUrl() {
		return appWithdrawUrl;
	}

	public void setAppWithdrawUrl(String appWithdrawUrl) {
		this.appWithdrawUrl = appWithdrawUrl;
	}

	public String getWebWithdrawUrl() {
		return webWithdrawUrl;
	}

	public void setWebWithdrawUrl(String webWithdrawUrl) {
		this.webWithdrawUrl = webWithdrawUrl;
	}

	public String getTransferAccountUrl() {
		return transferAccountUrl;
	}

	public void setTransferAccountUrl(String transferAccountUrl) {
		this.transferAccountUrl = transferAccountUrl;
	}

	public long getRefreshOnMillis() {
		return refreshOnMillis;
	}

	public void setRefreshOnMillis(long refreshOnMillis) {
		this.refreshOnMillis = refreshOnMillis;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
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

	public List<String> getSecurityUri() {
		return securityUri;
	}

	public void setSecurityUri(List<String> securityUri) {
		this.securityUri = securityUri;
	}

	public String getRemotingUrl() {
		return remotingUrl;
	}

	public void setRemotingUrl(String remotingUrl) {
		this.remotingUrl = remotingUrl;
	}

	public String getHuaxingSmsUri() {
		return huaxingSmsUri;
	}

	public void setHuaxingSmsUri(String huaxingSmsUri) {
		this.huaxingSmsUri = huaxingSmsUri;
	}

	public String getHuaxingSignature() {
		return huaxingSignature;
	}

	public void setHuaxingSignature(String huaxingSignature) {
		this.huaxingSignature = huaxingSignature;
	}

	public String getOpenAccountUrl() {
		return openAccountUrl;
	}

	public void setOpenAccountUrl(String openAccountUrl) {
		this.openAccountUrl = openAccountUrl;
	}

	public String getAccountBalanceUrl() {
		return accountBalanceUrl;
	}

	public void setAccountBalanceUrl(String accountBalanceUrl) {
		this.accountBalanceUrl = accountBalanceUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getAppTradePasswordSetUrl() {
		return appTradePasswordSetUrl;
	}

	public void setAppTradePasswordSetUrl(String appTradePasswordSetUrl) {
		this.appTradePasswordSetUrl = appTradePasswordSetUrl;
	}

	public String getWebTradePasswordSetUrl() {
		return webTradePasswordSetUrl;
	}

	public void setWebTradePasswordSetUrl(String webTradePasswordSetUrl) {
		this.webTradePasswordSetUrl = webTradePasswordSetUrl;
	}

	public String getAppRechargeUrl() {
		return appRechargeUrl;
	}

	public void setAppRechargeUrl(String appRechargeUrl) {
		this.appRechargeUrl = appRechargeUrl;
	}

	public String getWebRechargeUrl() {
		return webRechargeUrl;
	}

	public void setWebRechargeUrl(String webRechargeUrl) {
		this.webRechargeUrl = webRechargeUrl;
	}
}
