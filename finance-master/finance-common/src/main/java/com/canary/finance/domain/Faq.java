package com.canary.finance.domain;

import java.io.Serializable;

public class Faq implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String ask;
	private String question;
	private int status;
	
	public Faq(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}