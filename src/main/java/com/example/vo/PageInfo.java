package com.example.vo;

import java.util.ArrayList;
import java.util.List;

public class PageInfo {
	//总记录数
	private long totalRecord;
	//当前页数(从1开始)
	private int startPage = 1;
	//每页记录数
	private int length = 10;
	//分页结果集
	private List list = new ArrayList<>(0);
	
	public PageInfo(){};
	
	public PageInfo(long totalRecord, int startPage, int length, List list) {
		super();
		this.totalRecord = totalRecord;
		this.startPage = startPage;
		this.length = length;
		this.list = list;
	}

	// 返回记录起始位置
	public int getFirst() {
		return (startPage - 1) * length;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public long getTotalPage() {
		return (totalRecord / length) + (totalRecord % length == 0 ? 0 : 1);
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}