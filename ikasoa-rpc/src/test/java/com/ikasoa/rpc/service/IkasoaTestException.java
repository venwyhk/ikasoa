package com.ikasoa.rpc.service;

public class IkasoaTestException extends Exception {

	private static final long serialVersionUID = 1L;

	private int c = 1;

	public IkasoaTestException() {
		super();
	}

	public IkasoaTestException(String message) {
		super(message);
	}

	public IkasoaTestException(Throwable cause) {
		super(cause);
	}

	public IkasoaTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

}
