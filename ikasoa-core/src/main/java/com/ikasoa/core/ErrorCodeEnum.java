package com.ikasoa.core;

/**
 * 服务器接口异常错误编码枚举
 * <p>
 * 只是建议使用该套错误编码,并非必须使用.
 * <p>
 * <i>PARAMETER_ERROR</i>:参数错误,<i>DATA_ERROR</i>:数据错误, <i>EXECUTE_ERROR</i>
 * :执行错误,<i>AUTH_ERROR</i>:权限错误,<i>OTHER_ERROR</i>:其它错误.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public enum ErrorCodeEnum {

	PARAMETER_ERROR((short) 100), DATA_ERROR((short) 200), EXECUTE_ERROR((short) 300), AUTH_ERROR(
			(short) 400), OTHER_ERROR((short) 999);

	private final short code;

	ErrorCodeEnum(short code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

	public static ErrorCodeEnum fromCode(short code) {
		for (ErrorCodeEnum c : ErrorCodeEnum.values())
			if (c.code == code)
				return c;
		throw new IllegalArgumentException(String.format("Error Code (%d) isn't valid !", code));
	}
}
