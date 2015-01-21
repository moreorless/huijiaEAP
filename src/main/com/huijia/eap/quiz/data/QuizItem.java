package com.huijia.eap.quiz.data;

import java.util.LinkedList;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("quiz_item")
public class QuizItem {

	@Column
	@Id(auto = false)
	private long id;

	@Column
	private long quizId;

	/**
	 * 题目内容
	 */
	@Column
	private String question;

	/**
	 * 是否是测谎题
	 */
	@Column
	private boolean lieFlag;

	/**
	 * 数据库中存放的文本选项
	 */
	@Column
	private String optionJson;

	private LinkedList<QuizItemOption> options = new LinkedList<QuizItemOption>();

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOptionJson() {
		return optionJson;
	}

	public void setOptionJson(String optionJson) {
		this.optionJson = optionJson;
	}

	public LinkedList<QuizItemOption> getOptions() {
		return options;
	}

	public void setOptions(LinkedList<QuizItemOption> options) {
		this.options = options;
	}

	public void addOption(String index, String content, String categoryName,
			int value) {
		QuizItemOption option = new QuizItemOption(index, content,
				categoryName, value);
		this.options.add(option);
	}

	public boolean isLieFlag() {
		return lieFlag;
	}

	public void setLieFlag(boolean lieFlag) {
		this.lieFlag = lieFlag;
	}
}
