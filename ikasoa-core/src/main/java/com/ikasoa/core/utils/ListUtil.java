package com.ikasoa.core.utils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.LinkedList;

/**
 * List工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class ListUtil {

	public static <E> List<E> newArrayList() {
		return new ArrayList<>();
	}

	public static <E> List<E> newArrayList(int initialCapacity) {
		return new ArrayList<>(initialCapacity);
	}

	public static <E> List<E> newArrayList(Collection<? extends E> c) {
		return new ArrayList<>(c);
	}

	@SafeVarargs
	public static <E> List<E> buildArrayList(E... values) {
		return new ArrayList<>(Arrays.asList(values));
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends List> getArrayListClass() {
		return newArrayList(0).getClass();
	}

	public static <E> List<E> newLinkedList() {
		return new LinkedList<>();
	}

	public static <E> List<E> newLinkedList(Collection<? extends E> c) {
		return new LinkedList<>(c);
	}

	@SafeVarargs
	public static <E> List<E> buildLinkedList(E... values) {
		return new LinkedList<>(Arrays.asList(values));
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends List> getLinkedListClass() {
		return newLinkedList().getClass();
	}

	public static boolean isEmpty(List<?> list) {
		return ObjectUtil.isNull(list) || list.isEmpty();
	}

	public static <T> void forEach(int startIndex, int spanNum, Iterable<? extends T> elements,
			BiConsumer<Integer, ? super T> action) {
		if (ObjectUtil.orIsNull(elements, action))
			throw new IllegalArgumentException("Incorrect parameters !");
		int index = 1;
		for (T element : elements) {
			if (index <= NumberUtil.limitInt(startIndex, 0, Integer.MAX_VALUE)) {
				index++;
				continue;
			}
			action.accept(index - 1, element);
			index = index + NumberUtil.limitInt(spanNum, 1, Integer.MAX_VALUE);
		}
	}

}
