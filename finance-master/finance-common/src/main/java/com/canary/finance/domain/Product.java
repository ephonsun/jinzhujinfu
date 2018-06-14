package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String label;
	private double yearIncome;
	private int totalAmount;
	private int actualAmount;
	private int financePeriod;
	private int repayment;
	private int lowestMoney;
	private int highestMoney;
	private double increaseInterest;
	private double loanYearIncome;
	private Date raisedTime;
	private Date interestDate;
	private String summary;
	private String risk;
	private String attachment;
	private int status;
	private int sorting;
	private ProductCategory category;
	private Coupon coupon;
	private Merchant merchant;
	private int payback;
	
	public Product(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getPayback() {
		return payback;
	}

	public void setPayback(int payback) {
		this.payback = payback;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getYearIncome() {
		return yearIncome;
	}

	public void setYearIncome(double yearIncome) {
		this.yearIncome = yearIncome;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(int actualAmount) {
		this.actualAmount = actualAmount;
	}

	public int getFinancePeriod() {
		return financePeriod;
	}

	public void setFinancePeriod(int financePeriod) {
		this.financePeriod = financePeriod;
	}

	public int getRepayment() {
		return repayment;
	}

	public void setRepayment(int repayment) {
		this.repayment = repayment;
	}

	public int getLowestMoney() {
		return lowestMoney;
	}

	public void setLowestMoney(int lowestMoney) {
		this.lowestMoney = lowestMoney;
	}

	public int getHighestMoney() {
		return highestMoney;
	}

	public void setHighestMoney(int highestMoney) {
		this.highestMoney = highestMoney;
	}

	public double getIncreaseInterest() {
		return increaseInterest;
	}

	public void setIncreaseInterest(double increaseInterest) {
		this.increaseInterest = increaseInterest;
	}

	public double getLoanYearIncome() {
		return loanYearIncome;
	}

	public void setLoanYearIncome(double loanYearIncome) {
		this.loanYearIncome = loanYearIncome;
	}

	public Date getRaisedTime() {
		return raisedTime;
	}

	public void setRaisedTime(Date raisedTime) {
		this.raisedTime = raisedTime;
	}

	public Date getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(Date interestDate) {
		this.interestDate = interestDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
}