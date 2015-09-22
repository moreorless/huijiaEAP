package com.huijia.eap.quiz.data;

public class EmotionFeatureBean {

	/**
	 * 维度id
	 */
	private int categoryId;
	/**
	 * 维度名称
	 */
	private String categoryName;
	/**
	 * 均分
	 */
	private double averageScore;
	/**
	 * 常模均分
	 */
	private double normalScore;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public double getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}
	public double getNormalScore() {
		return normalScore;
	}
	public void setNormalScore(double normalScore) {
		this.normalScore = normalScore;
	}
	
	
	
}
