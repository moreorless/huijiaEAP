package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * 常模得分配置
 * @author leon
 *
 */
@Table("quiz_normal_config")
public class NormalScoreConfig {

	/**
	 * 维度名称
	 */
	@Column
	private String categoryName;
	
	/**
	 * 常模均分
	 */
	@Column
	private double averageScore;
	
	/**
	 * 标准差
	 */
	@Column
	private double sd;

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

	public double getSd() {
		return sd;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}
	
	
}
