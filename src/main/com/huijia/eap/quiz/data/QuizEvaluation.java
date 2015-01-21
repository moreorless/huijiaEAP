package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("quiz_evaluation")
public class QuizEvaluation {

	@Column
	@Id(auto = false)
	private long id;
	@Column
	private long quizId;
	@Column
	private long categoryId;
	@Column
	private String categoryName;

	// 团体报告还是个人报告
	@Column
	private String type;

	// 健康状况
	@Column
	private String healthStatus;

	// 解释
	@Column
	private String explanation;

	// 特征(关键词)
	@Column
	private String feature;

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public int getMinScore() {
		return minScore;
	}

	public void setMinScore(int minScore) {
		this.minScore = minScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * 下限(含)
	 */
	@Column
	private int minScore;

	/**
	 * 上限(含)
	 */
	@Column
	private int maxScore;
	/**
	 * 评语
	 */
	@Column
	private String evaluation;
	/**
	 * 建议
	 */
	@Column
	private String suggestion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

}
