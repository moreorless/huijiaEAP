package com.huijia.eap.quiz.data;

public class AnswerBean {
	/**
	 * 题目id
	 */
	private long questionId;
	/**
	 * 选项
	 */
	private String optionIndex;
	/**
	 * 分数（迫选题的最终分数，不一定与选项标定的分数一致，可能会上下浮动）
	 */
	private float score;
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public String getOptionIndex() {
		return optionIndex;
	}
	public void setOptionIndex(String optionIndex) {
		this.optionIndex = optionIndex;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	
	
	
}
