package com.huijia.eap.quiz.data;

/**
 * 试卷类别
 * 
 * @author leon
 * 
 */
public class QuizCategory {
	public QuizCategory(int id, String name, int priority) {
		this.id = id;
		this.name = name;
		this.priority = priority;
	}

	/**
	 * id
	 */
	private int id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 优先级
	 */
	private int priority;

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
