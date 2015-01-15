package com.huijia.eap.util.excel;

/**
 * 每个可修改参数的单元格元素
 * @author gaoxl
 *
 */
public class WritableCell {
	private int sheetIndex;
	
	private String column;
	
	private int row;
	
	private double value;

	/**
	 * 参数名称
	 */
	private String paramName;
	
	/**
	 * 参数描述
	 */
	private String description;
	
	/**
	 * 是否是公式
	 */
	private boolean formula = false;
	
	public WritableCell(int sheetIndex, String column, int row, double value){
		this.sheetIndex = sheetIndex;
		this.column = column;
		this.row = row;
		this.value = value;
	}
	
	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getFormula() {
		return formula;
	}

	public void setFormula(boolean formula) {
		this.formula = formula;
	}
	
	
	
}
