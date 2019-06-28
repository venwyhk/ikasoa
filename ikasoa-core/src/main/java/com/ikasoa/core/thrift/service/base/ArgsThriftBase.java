package com.ikasoa.core.thrift.service.base;

import org.apache.thrift.protocol.TStruct;

/**
 * Thrift基础参数对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ArgsThriftBase extends AbstractThriftBase {

	private static final long serialVersionUID = -1198784318916932231L;

	private static final TStruct STRUCT_DESC = new TStruct("get_args");

	public ArgsThriftBase() {
		super();
	}

	public ArgsThriftBase(String value) {
		super(value);
	}

	public ArgsThriftBase(ArgsThriftBase other) {
		super(other);
	}

	@Override
	public ArgsThriftBase deepCopy() {
		return new ArgsThriftBase(this);
	}

	@Override
	protected TStruct getTStruct() {
		return STRUCT_DESC;
	}

}
