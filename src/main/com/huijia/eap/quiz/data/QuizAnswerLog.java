package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;

/**
 * 答题记录
 * @author leon
 *
 */
public class QuizAnswerLog {
	/**
	 * 试卷id
	 */
	@Column
	private long quizId;
	/**
	 * 用户id
	 */
	@Column
	private long userId;
	/**
	 * 企业id
	 */
	@Column
	private long companyId;
	/**
	 * 答题时间
	 */
	@Column
	private long timestamp;
	public long getQuizId() {
		return quizId;
	}
	public void setQuizId(long quizId) {
		this.quizId = quizId;
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
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
