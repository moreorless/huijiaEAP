package com.huijia.eap.quiz.data;

/**
 * 试卷类别
 * @author leon
 *
 */
public class QuizCategory {
	public QuizCategory(int id, String name){
		this.id = id;
		this.name = name;
	}
	/**
	 * id
	 */
	private int id;
	/**
	 * 名称
	 */
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
