package org.bwyou.springboot2.exceptions;

import org.bwyou.springboot2.viewmodels.WebStatusMessageBody;
import org.springframework.http.HttpStatus;

public class UnAuthorizedWebException extends WebException {

	private static final long serialVersionUID = 7839800582624901546L;

	public UnAuthorizedWebException(WebStatusMessageBody body) {
		super(HttpStatus.UNAUTHORIZED, body);
	}

	public UnAuthorizedWebException(Exception ex) {
		super(HttpStatus.UNAUTHORIZED, ex);
	}

	public UnAuthorizedWebException() {
		super(HttpStatus.UNAUTHORIZED, new Exception(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
	}
}
