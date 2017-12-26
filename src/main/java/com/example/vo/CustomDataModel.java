package com.example.vo;

import net.sf.json.JSONObject;


public class CustomDataModel {

	public CustomDataModel() {
		status = new Status();
	}

	private JSONObject model;

	private Status status;

	public CustomDataModel(boolean success, String message, int code) {
		super();
		this.status = new Status();
		this.status.success = success;
		this.status.message = message;
		this.status.code = code;
	}

	public JSONObject getModel() {
		if (this.model == null) {
			this.model = new JSONObject();
		}
		return this.model;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setSuccess(boolean success) {
		this.status.setSuccess(success);
	}

	public void setMessage(String message) {
		this.status.setMessage(message);
	}

	public void setCode(int code) {
		this.status.setCode(code);
	}

	public void setModel(JSONObject model) {
		this.model = model;
	}

	public void addObject(String attributeName, Object attributeValue) {
		getModel().put(attributeName, attributeValue);
	}

	public void addPageInfo(PageInfo pageInfo) {
		getModel().put("totalPage", pageInfo.getTotalPage());
		getModel().put("totalRecord", pageInfo.getTotalRecord());
		getModel().put("startPage", pageInfo.getStartPage());
		getModel().put("length", pageInfo.getLength());
		getModel().put("list", pageInfo.getList());
	}
}