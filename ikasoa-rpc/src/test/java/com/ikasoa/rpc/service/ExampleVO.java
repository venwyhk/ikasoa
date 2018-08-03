package com.ikasoa.rpc.service;

public class ExampleVO extends ExampleSuperVO {

	private static final long serialVersionUID = 1L;

	private int id;

	private String string;

	// 测试对象嵌套
	private ExampleVO evo;

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

	public ExampleVO getEvo() {
		return evo;
	}

	public void setEvo(ExampleVO evo) {
		this.evo = evo;
	}

}
