package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class NewsBulletin implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private int type;
	private int status;
	private int hits;
	private Date createTime;
	private NewsMaterial news;
	
	public NewsBulletin(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public NewsMaterial getNews() {
		return news;
	}

	public void setNews(NewsMaterial news) {
		this.news = news;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
}