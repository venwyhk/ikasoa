package com.ikasoa.rpc.handler.impl;

import java.util.Optional;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ikasoa.core.utils.Base64Util;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

import lombok.NoArgsConstructor;

/**
 * 转换协议处理器Kryo实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
public class KryoProtocolHandlerImpl<T, R> implements ProtocolHandler<T, R> {

	private ReturnData resultData;

	private Kryo kryo;

	public KryoProtocolHandlerImpl(ReturnData resultData) {
		this.resultData = resultData;
		this.kryo = new Kryo();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T strToArg(String str) {
		return (T) kryo.readObject(new Input(Base64Util.decode(str)), kryo.register(Object[].class).getType());
	}

	@Override
	@SuppressWarnings("unchecked")
	public String argToStr(T arg) {
		arg = Optional.ofNullable(arg).orElse((T) new Object[0]);
		try (Output output = new Output(1, 4096)) {
			kryo.writeObject(output, arg);
			byte[] bb = output.toBytes();
			output.flush();
			return Base64Util.encode(bb);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String resultToStr(R result) {
		result = Optional.ofNullable(result).orElse((R) String.valueOf(V));
		try (Output output = new Output(1, 4096)) {
			kryo.writeObject(output, result);
			byte[] bb = output.toBytes();
			output.flush();
			return Base64Util.encode(bb);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public R strToResult(String str) {
		if (StringUtil.equals(String.valueOf(V), str))
			return null;
		return resultData.isArray()
				? (R) kryo.readObject(new Input(Base64Util.decode(str)),
						kryo.register(ListUtil.getArrayListClass()).getType())
				: resultData.isMap()
						? (R) kryo.readObject(new Input(Base64Util.decode(str)),
								kryo.register(MapUtil.getHashMapClass()).getType())
						: (R) kryo.readObject(new Input(Base64Util.decode(str)),
								kryo.register(resultData.getClassType()).getType());
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
