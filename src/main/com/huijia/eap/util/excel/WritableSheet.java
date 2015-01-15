package com.huijia.eap.util.excel;

import java.util.LinkedList;
import java.util.List;

public class WritableSheet {

	private int index;
	
	private String name;
	
	private List<WritableCell> cells = new LinkedList<>();

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WritableCell> getCells() {
		return cells;
	}

	public void setCells(List<WritableCell> cells) {
		this.cells = cells;
	}
	
	public void addCell(WritableCell cell){
		this.cells.add(cell);
	}
	
}
