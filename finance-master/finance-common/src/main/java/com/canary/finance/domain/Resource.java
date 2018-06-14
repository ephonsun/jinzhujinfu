package com.canary.finance.domain;

import java.io.Serializable;

public class Resource implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String icon;
	private String url;
	private short enable;
	private String category;
	private int parentId;
	private short shortcut;
	
	public Resource(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public short getEnable() {
		return enable;
	}

	public void setEnable(short enable) {
		this.enable = enable;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public short getShortcut() {
		return shortcut;
	}

	public void setShortcut(short shortcut) {
		this.shortcut = shortcut;
	}
}