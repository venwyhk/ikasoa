package com.ikasoa.core.loadbalance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 负载均衡节点
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.3
 */
@AllArgsConstructor
@Data
@ToString
public class Node<S> {

	/**
	 * 服务器节点内容
	 */
	private S value;

	/**
	 * 权重值 (在支持权重的负载均很实现中,此值越大,被调用的机率越高)
	 */
	private int weightNumber = 0;

	public Node(S value) {
		this.value = value;
	}

}
