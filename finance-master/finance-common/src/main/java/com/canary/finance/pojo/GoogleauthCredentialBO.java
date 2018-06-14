package com.canary.finance.pojo;

import java.io.Serializable;
import java.util.List;

public class GoogleauthCredentialBO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String key;
    private int verificationCode;
    private String totpUrl;
    private String token;
    private List<Integer> scratchCodes;
    
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public int getVerificationCode() {
		return verificationCode;
	}
	
	public void setVerificationCode(int verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public String getTotpUrl() {
		return totpUrl;
	}

	public void setTotpUrl(String totpUrl) {
		this.totpUrl = totpUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Integer> getScratchCodes() {
		return scratchCodes;
	}
	
	public void setScratchCodes(List<Integer> scratchCodes) {
		this.scratchCodes = scratchCodes;
	}
}
