package com.ikasoa.rpc.service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ikasoa.rpc.annotation.IkasoaService;

@IkasoaService(name = "ExampleService")
public interface ExampleService {

	// 返回对象
	ExampleVO findVO(int id);

	// 返回对象列表
	List<ExampleVO> getVOList();

	// 返回Boolean
	Boolean getBoolean();

	// 返回boolean
	boolean getBoolean2();

	// 返回double
	double getDouble(long l);

	// 多参数&返回时间
	Date getDate(int id, Date date);

	// 可变参数&返回数组
	String[] testByStrings(String... strings);

	// 数组参数
	int testByInts(Integer[] ints);

	// Map参数和返回
	Map<String, ExampleVO> getMap(int id, Map<String, ExampleVO> map);

	// 字节组
	byte[] getBytes(byte[] bytes);

	// 测试自定义异常
	int getInt() throws IkasoaTestException, Exception;

	ExampleSuperVO getExampleSuperVO(ExampleSuperVO esvo);

	List<Map<String, Object>> testContainerType();

	List<List<String>> testContainerType2();

	// 下载文件
	byte[] down() throws FileNotFoundException;

	void tVoid();

}
