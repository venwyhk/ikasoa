package com.ikasoa.rpc.handler.impl;

import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;
import com.ikasoa.rpc.utils.StreamUtil;

import lombok.AllArgsConstructor;

/**
 * 转换协议处理器JavaSerializable实现 (BETA)
 * <p>
 * 使用该实现的对象需要实现<code>java.io.Serializable</code>接口.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.3
 */
@AllArgsConstructor
public class SerializableProtocolHandlerImpl<T, R> implements ProtocolHandler<T, R> {

	private ReturnData resultData;

	@Override
	@SuppressWarnings("unchecked")
	public T strToArg(String str) {
		return (T) StreamUtil.bytesToObject(StringUtil.hexStrToBytes(str));
	}

	@Override
	public String argToStr(T arg) {
		return StringUtil.bytesToHexStr(StreamUtil.objectToBytes(arg));
	}

	@Override
	public String resultToStr(R result) {
		if (result == null)
			return String.valueOf(V);
		return StringUtil.bytesToHexStr(StreamUtil.objectToBytes(result));
	}

	@Override
	@SuppressWarnings("unchecked")
	public R strToResult(String str) {
		if (String.valueOf(V).equals(str))
			return null;
		return (R) StreamUtil.bytesToObject(StringUtil.hexStrToBytes(str));
	}

	@Override
	public Throwable strToThrowable(String str) {
		// TODO: 该实现暂不对异常返回做处理
		return null;
	}

	@Override
	public ReturnData getReturnData() {
		return resultData;
	}

}
