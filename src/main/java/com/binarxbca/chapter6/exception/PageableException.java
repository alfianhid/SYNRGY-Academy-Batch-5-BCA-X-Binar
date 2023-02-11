package com.binarxbca.chapter6.exception;

import org.springframework.http.HttpStatus;

public class PageableException extends RuntimeException {
	private final HttpStatus status;
	private final String message;

	public PageableException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public PageableException(HttpStatus status, String message, Throwable exception) {
		super(exception);
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
