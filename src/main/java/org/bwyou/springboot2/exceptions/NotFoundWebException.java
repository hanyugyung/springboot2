package org.bwyou.springboot2.exceptions;

import org.bwyou.springboot2.viewmodels.WebStatusMessageBody;
import org.springframework.http.HttpStatus;

public class NotFoundWebException extends WebException {

	private static final long serialVersionUID = 2776931635003293159L;

	public NotFoundWebException(WebStatusMessageBody body) {
		super(HttpStatus.NOT_FOUND, body);
	}

	public NotFoundWebException(Exception ex) {
		super(HttpStatus.NOT_FOUND, ex);
	}

	public NotFoundWebException() {
		super(HttpStatus.NOT_FOUND, new Exception(HttpStatus.NOT_FOUND.getReasonPhrase()));
	}
}
