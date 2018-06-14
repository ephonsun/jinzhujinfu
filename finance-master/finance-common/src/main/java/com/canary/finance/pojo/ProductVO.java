package com.canary.finance.pojo;

import java.io.Serializable;

public class ProductVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String label;
	private double yearIncome;
	private int totalAmount;
	private int actualAmount;
	private int financePeriod;
	private int lowestMoney;
	private int highestMoney;
	private double increaseInterest;
	private String raisedTime;
	private String interestDate;
	private String category;
	
	public ProductVO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
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

	public String getRaisedTime() {
		return raisedTime;
	}

	public void setRaisedTime(String raisedTime) {
		this.raisedTime = raisedTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
