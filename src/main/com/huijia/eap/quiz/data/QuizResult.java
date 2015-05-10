package com.huijia.eap.quiz.data;

import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.Json;

/**
 * 答题结果
 * @author leon
 *
 */
@Table("quiz_result")
public class QuizResult {
	@Column
	@Id(auto=false)
	private long id;
	/**
	 * 答题者id
	 */
	@Column
	private long userId;
	/**
	 * 答题者所在企业
	 */
	@Column
	private long companyId;
	/**
	 * 试卷id
	 */
	@Column
	private long quizId;
	
	/**
	 * 答题时间
	 */
	@Column
	private long timestamp;
	/**
	 * 答题明细
	 */
	@Column
	private String answer;
	
	/**
	 * 测谎题总分
	 */
	@Column
	private int lieScore;
	
	/**
	 * 总分
	 */
	@Column
	private int score;
	/**
	 * 各维度得分
	 */
	@Column
	private String scoreJson;
	
	/**
	 * 是否为有效样本
	 */
	@Column
	private boolean valid = true;
	
	/**
	 * 判定的维度类型id
	 */
	@Column
	private int categoryId;
	
	/**
	 * 判定的维度类型名称
	 */
	@Column
	private String categoryName;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getScoreJson() {
		return scoreJson;
	}

	public void setScoreJson(String scoreJson) {
		this.scoreJson = scoreJson;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

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

	private Map<String, Integer> scoreMap = null;
	
	public Map<String, Integer> getScoreMap(){
		if(scoreMap != null) return scoreMap;
		scoreMap = (Map<String, Integer>)Json.fromJson(scoreJson);
		return scoreMap;
	}

	public int getLieScore() {
		return lieScore;
	}

	public void setLieScore(int lieScore) {
		this.lieScore = lieScore;
	}
	
}
