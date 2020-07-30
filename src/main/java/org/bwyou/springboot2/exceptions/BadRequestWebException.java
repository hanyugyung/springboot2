package org.bwyou.springboot2.exceptions;

import org.bwyou.springboot2.viewmodels.WebStatusMessageBody;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class BadRequestWebException extends WebException {

	private static final long serialVersionUID = 2738965184583330402L;

	public BadRequestWebException(WebStatusMessageBody body) {
		super(HttpStatus.BAD_REQUEST, body);
	}

	public BadRequestWebException(Exception ex) {
		super(HttpStatus.BAD_REQUEST, ex);
	}

	public BadRequestWebException() {
		super(HttpStatus.BAD_REQUEST, new Exception(HttpStatus.BAD_REQUEST.getReasonPhrase()));
	}
	
	public BadRequestWebException(BindingResult bindingResult) {
		super(HttpStatus.BAD_REQUEST, new Exception(HttpStatus.BAD_REQUEST.getReasonPhrase()), bindingResult);
	}
}
