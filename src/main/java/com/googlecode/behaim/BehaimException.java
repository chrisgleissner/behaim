package com.googlecode.behaim;

public class BehaimException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BehaimException() {
	}

	public BehaimException(String message) {
		super(message);
	}

	public BehaimException(String message, Throwable cause) {
		super(message, cause);
	}

	public BehaimException(Throwable cause) {
		super(cause);
	}

}
