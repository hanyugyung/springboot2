package org.bwyou.springboot2.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.bwyou.springboot2.viewmodels.WebStatusMessageBody;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class WebException extends RuntimeException {

	private static final long serialVersionUID = 788198260815252665L;
	
	WebStatusMessageBody body;
	BindingResult bindingResult;

	public WebStatusMessageBody getBody() {
		return body;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public WebException(HttpStatus status, WebStatusMessageBody body) {
		super(body.getMessage());
		body.setStatus(status.value());
		this.body = body;
	}
	
	public WebException(HttpStatus status, Exception ex) {
		super(ex);
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));	//보안 상 트레이스 값은 보여주지 말자. 디버그 모드에서는 보여 주게 하는 것도 좋을 듯
		body = new WebStatusMessageBody("E" + String.format("%03d", status.value())
							, ex.getMessage() != null ? ex.getMessage() : ex.toString(), /*errors.toString()*/"", "");
		body.setStatus(status.value());
	}
	
	public WebException(HttpStatus status, Exception ex, BindingResult bindingResult) {
		super(ex);
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		body = new WebStatusMessageBody("E" + String.format("%03d", status.value())
							, ex.getMessage() != null ? ex.getMessage() : ex.toString(), /*errors.toString()*/"", "");
		body.setStatus(status.value());
		this.bindingResult = bindingResult;
	}

	public WebException(WebStatusMessageBody body) {
		super(body.getMessage());
		this.body = body;
	}
	
	public WebException(Exception ex) {
		super(ex);
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		body = new WebStatusMessageBody("E" + String.format("%03d", HttpStatus.INTERNAL_SERVER_ERROR.value())
							, ex.getMessage() != null ? ex.getMessage() : ex.toString(), /*errors.toString()*/"", "");
		body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public WebException() {
		super();
		body = new WebStatusMessageBody("E" + String.format("%03d", HttpStatus.INTERNAL_SERVER_ERROR.value())
							, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "", "");
		body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

}
