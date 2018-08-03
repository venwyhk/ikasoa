package com.ikasoa.rpc.service;

import java.io.Serializable;

public abstract class ExampleSuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String descriptor;

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

}
