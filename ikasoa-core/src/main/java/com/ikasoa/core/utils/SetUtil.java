package com.ikasoa.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;

/**
 * Set工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class SetUtil {

	public static <E> Set<E> newHashSet() {
		return new HashSet<>();
	}

	public static <E> Set<E> newHashSet(int initialCapacity) {
		return new HashSet<>(initialCapacity);
	}

	public static <E> Set<E> newHashSet(int initialCapacity, float loadFactor) {
		return new HashSet<>(initialCapacity, loadFactor);
	}

	public static <E> Set<E> newHashSet(Collection<? extends E> c) {
		return new HashSet<>(c);
	}

	@SafeVarargs
	public static <E> Set<E> newHashSet(E... values) {
		return new HashSet<>(Arrays.asList(values));
	}

	public static boolean isEmpty(Set<?> set) {
		return ObjectUtil.isNull(set) || set.isEmpty();
	}

}
