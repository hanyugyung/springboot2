package org.bwyou.springboot2.exceptions;

import org.bwyou.springboot2.viewmodels.WebStatusMessageBody;
import org.springframework.http.HttpStatus;

public class ForbiddenWebException extends WebException {

	private static final long serialVersionUID = 352464846729007885L;

	public ForbiddenWebException(WebStatusMessageBody body) {
		super(HttpStatus.FORBIDDEN, body);
	}

	public ForbiddenWebException(Exception ex) {
		super(HttpStatus.FORBIDDEN, ex);
	}

	public ForbiddenWebException() {
		super(HttpStatus.FORBIDDEN, new Exception(HttpStatus.FORBIDDEN.getReasonPhrase()));
	}
}
