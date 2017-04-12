package com.ikasoa.example.rpc;

public class ExampleVO {

	private int id;
	private String string;

	public ExampleVO() {
	}

	public ExampleVO(int id, String string) {
		this.id = id;
		this.string = string;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}