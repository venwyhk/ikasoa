package com.ikasoa.core.loadbalance.impl;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * 一致性Hash负载均衡实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5
 */
@NoArgsConstructor
public class ConsistencyHashLoadBalanceImpl<S> implements LoadBalance<S> {

	private TreeMap<Long, Node<S>> nodes = null;

	/**
	 * 设置虚拟节点数目
	 */
	private final int VIRTUAL_NUM = 4;

	private SoftReference<String> hashReference;

	@SneakyThrows
	public ConsistencyHashLoadBalanceImpl(List<Node<S>> nodeList, String hash) {
		if (StringUtil.isEmpty(hash))
			throw new IllegalArgumentException("Constructor must exist hash parameter !");
		hashReference = new SoftReference<String>(StringUtil.merge(InetAddress.getLocalHost().getHostAddress(), hash));
		nodes = MapUtil.newTreeMap();
		ListUtil.forEach(nodeList, (index, node) -> IntStream.range(0, VIRTUAL_NUM)
				.forEach(i -> nodes.put(hash(computeMd5(String.format("SHARD-%d-NODE-%d", index, i)), i), node)));
	}

	@Override
	public Node<S> getNode() {
		SortedMap<Long, Node<S>> tailMap = nodes.tailMap(hash(computeMd5(hashReference.get()), 0));
		return nodes.get(tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey());
	}

	@Override
	public Node<S> next() throws IkasoaException {
		return getNode();
	}

	private long hash(byte[] digest, int nTime) {
		return (((long) (digest[3 + nTime * 4] & 0xFF) << 24) | ((long) (digest[2 + nTime * 4] & 0xFF) << 16)
				| ((long) (digest[1 + nTime * 4] & 0xFF) << 8) | (digest[0 + nTime * 4] & 0xFF)) & 0xffffffffL;
	}

	private byte[] computeMd5(String value) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported .", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(String.format("Unknown string : %s .", value), e);
		}
		md5.update(keyBytes);
		return md5.digest();
	}

}
