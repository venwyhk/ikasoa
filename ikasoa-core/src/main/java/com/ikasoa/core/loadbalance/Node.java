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
public class Node<I> {

	/**
	 * 节点信息
	 */
	private I value;

	/**
	 * 权重值 (如果有用负载均衡,那么此值越大,被调用的机率越高)
	 */
	private int weightNumber = 0;

	public Node(I value) {
		this.value = value;
	}

}
