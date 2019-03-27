package org.bwyou.springboot2.viewmodels;

import lombok.Getter;

@Getter
public class WebStatusMessageBody {
	Integer status;
	String code;
	String message;
	String link;
	String developerMessage;

	public WebStatusMessageBody(String code, String message, String developerMessage, String link) {
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.link = link;
	}

	public WebStatusMessageBody(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

