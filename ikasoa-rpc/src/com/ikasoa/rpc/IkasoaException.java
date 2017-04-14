package com.ikasoa.rpc;

/**
 * IKASOA通用异常类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaException extends Exception {

	private static final long serialVersionUID = 1L;

	public IkasoaException() {
		super();
	}

	public IkasoaException(String message) {
		super(message);
	}

	public IkasoaException(Throwable cause) {
		super(cause);
	}

	public IkasoaException(String message, Throwable cause) {
		super(message, cause);
	}
}