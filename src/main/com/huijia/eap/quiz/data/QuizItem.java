package com.huijia.eap.quiz.data;

import java.util.LinkedList;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.huijia.eap.quiz.QuizImportService.QuizItemOption;

@Table("quiz_item")
public class QuizItem {

	@Column
	@Id(auto=false)
	private long id;
	
	/**
	 * 题目内容
	 */
	@Column
	private String question;
	
	/**
	 * 维度
	 */
	@Column
	private String category;

	/**
	 * 是否是测谎题
	 */
	@Column
	private boolean lieFlag;
	
	/**
	 * 数据库中存放的文本选项
	 */
	@Column
	private String optionsJson;
	
	private LinkedList<QuizItemOption> options = new LinkedList<QuizItemOption>();
	
	/**
	 * 评价标准
	 */
	private QuizEvaluation evaluation;
	
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



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getOptionsJson() {
		return optionsJson;
	}



	public void setOptionsJson(String optionsJson) {
		this.optionsJson = optionsJson;
	}



	public LinkedList<QuizItemOption> getOptions() {
		return options;
	}



	public void setOptions(LinkedList<QuizItemOption> options) {
		this.options = options;
	}

	public void addOption(String index, String name, int value){
		QuizItemOption option = new QuizItemOption(index, name, value);
		this.options.add(option);
	}



	public boolean isLieFlag() {
		return lieFlag;
	}



	public void setLieFlag(boolean lieFlag) {
		this.lieFlag = lieFlag;
	}
}
