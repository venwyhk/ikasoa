package com.ikasoa.zk;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 服务注册对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
@RequiredArgsConstructor
public class ZkServerNode implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	private String serverName;

	@NonNull
	private String serverHost;

	@NonNull
	private Integer serverPort;

	private String transportFactoryClassName;

	private String protocolFactoryClassName;

	private String processorFactoryClassName;

	private String processorClassName;

}
