package com.huijia.eap.quiz.data;

public class QuizItemOption {
	/**
	 * 序号：ABCDE
	 */
	private String index;
	/**
	 * 符合/非常符合
	 */
	private String name;
	/**
	 * 分数
	 */
	private int value;
	
	public QuizItemOption(String index, String name, int value){
		this.setIndex(index);
		this.name = name;
		this.value = value;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}


	public String getIndex() {
		return index;
	}


	public void setIndex(String index) {
		this.index = index;
	}
}
