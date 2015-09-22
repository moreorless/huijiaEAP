package com.huijia.eap.quiz.data;

/**
 * 迫选题使用的常模得分计分方法
 * @author leon
 *
 */
public class NormResultBean {

	/**
	 * 维度id
	 */
	private long categoryId;
	
	/**
	 * 维度名称
	 */
	private String categoryName;
	
	/**
	 * 原始平均分
	 */
	private double originalAver;
	
	/**
	 * 平均分
	 */
	private int averageScore;
	
	/**
	 * 常模均分
	 */
	private int normalAver;
	
	/**
	 * 常模得分
	 */
	private int normalScore;
	
	
	/**
	 *倾向指数(1 - 5)
	 */
	private int index;
	
	/**
	 * 总体标准差
	 */
	private double varp;

	/**
	 * 正态分布下的累积概率值
	 */
	private double normdist;
	
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(int averageScore) {
		this.averageScore = averageScore;
	}

	public int getNormalScore() {
		return normalScore;
	}

	public void setNormalScore(int normalScore) {
		this.normalScore = normalScore;
	}

	public double getVarp() {
		return varp;
	}

	public void setVarp(double varp) {
		this.varp = varp;
	}

	public double getNormdist() {
		return normdist;
	}

	public void setNormdist(double normdist) {
		this.normdist = normdist;
	}
	
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getOriginalAver() {
		return originalAver;
	}

	public void setOriginalAver(double originalAver) {
		this.originalAver = originalAver;
	}

	
	public int getNormalAver() {
		return normalAver;
	}

	public void setNormalAver(int normalAver) {
		this.normalAver = normalAver;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString(){
		return "维度 id " + categoryId + "\t 名称 : " + categoryName
				+ "\t原始平均分=" + originalAver + "\t常模均分=" + normalAver + "\t总体标准差=" + varp + "\t倾向指数=" + index
				+ "\t累计概率值 =" + normdist + "\t得分=" + averageScore + "\t常模得分=" + normalScore;
	}
	

}
