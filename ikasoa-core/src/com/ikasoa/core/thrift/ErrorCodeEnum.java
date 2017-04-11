package com.ikasoa.core.thrift;

/**
 * Thrift服务器接口异常错误编码枚举
 * <p>
 * 只是建议使用该套错误编码,并非必须使用.
 * <p>
 * <i>PARAMETER_ERROR</i>:参数错误,<i>DATA_ERROR</i>:数据错误, <i>EXECUTE_ERROR</i>
 * :执行错误,<i>OTHER_ERROR</i>:其它错误.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public enum ErrorCodeEnum {

	PARAMETER_ERROR(100), DATA_ERROR(200), EXECUTE_ERROR(300), OTHER_ERROR(400);

	private final int code;

	ErrorCodeEnum(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

	public static ErrorCodeEnum fromCode(int code) {
		for (ErrorCodeEnum c : ErrorCodeEnum.values()) {
			if (c.code == code) {
				return c;
			}
		}
		throw new IllegalArgumentException("Error Code : " + code + " isn't valid !");
	}
}
