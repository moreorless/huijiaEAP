package com.huijia.eap.util.excel;

import java.util.LinkedList;
import java.util.List;

public class WritableExcel {

	/**
	 * 表格名字
	 */
	private String name;
	
	/**
	 * 对应的excel文件
	 */
	private String excel;
	
	/**
	 * 页签
	 */
	private List<WritableSheet> sheets = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WritableSheet> getSheets() {
		return sheets;
	}

	public void setSheets(List<WritableSheet> sheets) {
		this.sheets = sheets;
	}

	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}
	
	public void addSheet(WritableSheet sheet){
		this.sheets.add(sheet);
	}
	
	
}
