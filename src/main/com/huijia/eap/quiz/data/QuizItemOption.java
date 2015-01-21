package com.huijia.eap.quiz.data;

public class QuizItemOption {
	/**
	 * 序号：ABCDE
	 */
	private String index;
	/**
	 * 选项内容，例如：非常符合/一般/非常不同意
	 */
	private String content;
	/**
	 * 维度名称：选项所属的维度
	 */
	private String categoryName;
	/**
	 * 分数
	 */
	private int value;

	public QuizItemOption(String index, String content, String categoryName,
			int value) {
		this.setIndex(index);
		this.categoryName = categoryName;
		this.content = content;
		this.value = value;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
