package com.ikasoa.rpc;

import com.ikasoa.core.IkasoaException;

/**
 * RPC通用异常类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class RpcException extends IkasoaException {

	private static final long serialVersionUID = 1L;

	public RpcException() {
		super();
	}

	public RpcException(String message) {
		super(message);
	}

	public RpcException(Throwable cause) {
		super(cause);
	}

	public RpcException(String message, Throwable cause) {
		super(message, cause);
	}
}