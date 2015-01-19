package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("quiz_item_relation")
public class QuizItemRelation {

	/**
	 * 题目内容
	 */
	@Column
	private long quizId;

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getQuizItemId() {
		return quizItemId;
	}

	public void setQuizItemId(long quizItemId) {
		this.quizItemId = quizItemId;
	}

	/**
	 * 维度id
	 */
	@Column
	private long quizItemId;
}