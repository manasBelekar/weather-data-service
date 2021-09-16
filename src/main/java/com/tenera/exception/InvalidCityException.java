package com.tenera.exception;

public class InvalidCityException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidCityException(String message) {
		super(message);
	}
}
