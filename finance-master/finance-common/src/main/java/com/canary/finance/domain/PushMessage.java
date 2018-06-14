package com.canary.finance.domain;

import java.io.Serializable;
import java.util.Date;

public class PushMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String content;
	private short pushType;
	private int productId;
	private int newsId;
	private short osType;
	private short sendType;
	private Date sendTime;
	private short sendTarget;
	private String equipment;
	private short status;
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public short getPushType() {
		return pushType;
	}

	public void setPushType(short pushType) {
		this.pushType = pushType;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public short getOsType() {
		return osType;
	}

	public void setOsType(short osType) {
		this.osType = osType;
	}

	public short getSendType() {
		return sendType;
	}

	public void setSendType(short sendType) {
		this.sendType = sendType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public short getSendTarget() {
		return sendTarget;
	}

	public void setSendTarget(short sendTarget) {
		this.sendTarget = sendTarget;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}