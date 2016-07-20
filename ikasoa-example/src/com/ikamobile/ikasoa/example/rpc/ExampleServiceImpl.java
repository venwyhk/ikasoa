package com.ikamobile.ikasoa.example.rpc;

public class ExampleServiceImpl implements ExampleService {
	@Override
	public ExampleVO findVO(int id) {
		return new ExampleVO(id, "helloworld");
	}
}
