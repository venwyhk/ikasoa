package com.ikamobile.ikasoa.example;

public class ExampleServiceImpl implements ExampleService {
	@Override
	public ExampleVO findVO(int id) {
		return new ExampleVO(id, "helloworld");
	}
}
