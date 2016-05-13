package com.ikamobile.ikasoa.rpc.service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ikamobile.ikasoa.rpc.annotation.IkasoaService;

@IkasoaService(name = "ExampleService")
public interface ExampleService {

	// 返回对象
	public ExampleVO findVO(int id);

	// 返回对象列表
	public List<ExampleVO> getVOList();

	// 返回Boolean
	public Boolean getBoolean();

	// 返回boolean
	public boolean getBoolean2();

	// 返回double
	public double getDouble(long l);

	// 多参数&返回时间
	public Date getDate(int id, Date date);

	// 可变参数&返回数组
	public String[] testByStrings(String... strings);

	// 数组参数
	public int testByInts(Integer[] ints);

	// Map参数和返回
	public Map<String, ExampleVO> getMap(int id, Map<String, ExampleVO> map);

	// 字节组
	public byte[] getBytes(byte[] bytes);

	// 测试自定义异常
	public int getInt() throws IkasoaTestException, Exception;

	// 下载文件
	public byte[] down() throws FileNotFoundException;

	public ExampleSuperVO getExampleSuperVO(ExampleSuperVO esvo);

}
