package com.ikasoa.core.utils;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SerializableTestObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

}
