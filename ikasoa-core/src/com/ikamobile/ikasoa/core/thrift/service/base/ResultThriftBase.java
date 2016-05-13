package com.ikamobile.ikasoa.core.thrift.service.base;

import org.apache.thrift.TBase;
import org.apache.thrift.protocol.TStruct;

/**
 * Thrift基础值对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ResultThriftBase extends AbstractThriftBase {

	private static final long serialVersionUID = 1L;

	private static final TStruct STRUCT_DESC = new TStruct("get_result");

	public ResultThriftBase() {
		super();
	}

	public ResultThriftBase(String value) {
		super(value);
	}

	public ResultThriftBase(ResultThriftBase other) {
		super(other);
	}

	@Override
	public TBase<AbstractThriftBase, FieldsEnum> deepCopy() {
		return new ResultThriftBase(this);
	}

	@Override
	protected TStruct getTStruct() {
		return STRUCT_DESC;
	}

}
