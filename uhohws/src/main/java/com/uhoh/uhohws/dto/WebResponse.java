package com.uhoh.uhohws.dto;

import java.io.Serializable;

public class WebResponse implements Serializable {

	int code;
	String reason;
	Object result;

	public WebResponse() {
	}

	public WebResponse(int code, String reason, Object result) {
		super();
		this.code = code;
		this.reason = reason;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new String("Response: code : " + code + " ; message: " + reason);
	}

}
