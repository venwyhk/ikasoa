package com.ikasoa.core.thrift.service.base;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseHelper;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Thrift基础对象抽象类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@NoArgsConstructor
public abstract class AbstractThriftBase
		implements TBase<AbstractThriftBase, AbstractThriftBase.FieldsEnum>, Cloneable {

	private static final long serialVersionUID = -7518103311477787917L;

	@Getter
	private String str;

	public AbstractThriftBase(String str) {
		this();
		this.str = str;
	}

	public AbstractThriftBase(AbstractThriftBase other) {
		if (other.isSet(null))
			str = other.str;
	}

	protected abstract TStruct getTStruct();

	@Override
	public FieldsEnum fieldForId(int i) {
		return FieldsEnum.VALUE;
	}

	@Override
	public Object getFieldValue(FieldsEnum fieldsEnum) {
		return str;
	}

	@Override
	public void setFieldValue(FieldsEnum fieldsEnum, Object obj) {
		str = (String) obj;
	}

	@Override
	public boolean isSet(FieldsEnum fieldsEnum) {
		return StringUtil.isNotEmpty(str);
	}

	@Override
	public void clear() {
		str = null;
	}

	/**
	 * 读取操作
	 */
	@Override
	public void read(TProtocol iprot) throws TException {
		if (!StringUtil.equals("org.apache.thrift.scheme.StandardScheme", iprot.getScheme().getName()))
			throw new TApplicationException("Service scheme must be 'org.apache.thrift.scheme.StandardScheme' !");
		TField schemeField;
		iprot.readStructBegin();
		while (true) {
			schemeField = iprot.readFieldBegin();
			if (ObjectUtil.same(schemeField.type, TType.STOP))
				break;
			if (ObjectUtil.same(schemeField.type, TType.STRING))
				str = iprot.readString();
			else
				throw new TApplicationException("field type must be 'String' !");
			iprot.readFieldEnd();
		}
		iprot.readStructEnd();
	}

	/**
	 * 写入操作
	 */
	@Override
	public void write(TProtocol oprot) throws TException {
		if (!StringUtil.equals("org.apache.thrift.scheme.StandardScheme", oprot.getScheme().getName()))
			throw new TApplicationException("Service scheme must be 'org.apache.thrift.scheme.StandardScheme' !");
		oprot.writeStructBegin(getTStruct());
		if (ObjectUtil.isNotNull(str)) {
			oprot.writeFieldBegin(new TField("value", TType.STRING, (short) 0));
			oprot.writeString(str);
			oprot.writeFieldEnd();
		}
		oprot.writeFieldStop();
		oprot.writeStructEnd();
	}

	@Override
	public int compareTo(AbstractThriftBase other) {
		if (!ObjectUtil.equals(getClass(), other.getClass()))
			return getClass().getName().compareTo(other.getClass().getName());
		int lastComparison = Boolean.valueOf(isSet(null)).compareTo(isSet(null));
		if (lastComparison != 0)
			return lastComparison;
		if (isSet(null)) {
			lastComparison = TBaseHelper.compareTo(str, other.getStr());
			if (lastComparison != 0)
				return lastComparison;
		}
		return 0;
	}

	/**
	 * 字段枚举
	 * <p>
	 * 标准服务中的参数与返回值都为单个<i>String</i>,所以在这里只设置了<i>VALUE</i>一个值.
	 */
	public enum FieldsEnum implements TFieldIdEnum {

		VALUE((short) 0, "value");

		private final short fieldId;
		private final String fieldName;

		FieldsEnum(short fieldId, String fieldName) {
			this.fieldId = fieldId;
			this.fieldName = fieldName;
		}

		public short getThriftFieldId() {
			return fieldId;
		}

		public String getFieldName() {
			return fieldName;
		}
	}

}
