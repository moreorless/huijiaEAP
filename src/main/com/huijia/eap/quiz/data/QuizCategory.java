package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 试卷类别
 * 
 * @author leon
 * 
 */
@Table("quiz_category")
public class QuizCategory {
	public QuizCategory(int id, String name, int priority) {
		this.id = id;
		this.name = name;
		this.priority = priority;
	}

	public QuizCategory() {
		// TODO Auto-generated constructor stub
	}

	@Column
	@Id(auto = false)
	private int id;

	/**
	 * quizId
	 */
	@Column
	private int quizId;

	@Column
	private int level;

	@Column
	private int parentId;

	/**
	 * 名称
	 */
	@Column
	private String name;

	/**
	 * 全称
	 */
	@Column
	private String fullName;

	/**
	 * 优先级
	 */
	@Column
	private int priority;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
