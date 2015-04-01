package com.huijia.eap.quiz.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.Json;

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

	private boolean _bConverted = false;
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
		if (!_bConverted) {
			this.convertOptions();
		}
		return options;
	}

	public void setOptions(LinkedList<QuizItemOption> options) {
		this.options = options;
	}

	public boolean isLieFlag() {
		return lieFlag;
	}

	public void setLieFlag(boolean lieFlag) {
		this.lieFlag = lieFlag;
	}

	/**
	 * 转换选项
	 */
	public void convertOptions() {
		LinkedList<QuizItemOption> optionList = new LinkedList<QuizItemOption>();
		List _list = (List) Json.fromJson(optionJson);
		Iterator<Map> iter = _list.iterator();
		while (iter.hasNext()) {
			Map optObj = iter.next();
			String index = (String) optObj.get("index");
			String content = (String) optObj.get("content");
			String categoryName = (String) optObj.get("categoryName");
			int value = (int) optObj.get("value");
			int categoryId = (int) optObj.get("categoryId");

			QuizItemOption option = new QuizItemOption(index, content,
					categoryId, categoryName, value);
			optionList.add(option);
		}

		this.options = optionList;
		_bConverted = true;
	}
	
	public String addCategoryTblMaxIdForOptionJson(String currentJson, int categoryTblMaxId) {
		LinkedList<QuizItemOption> optionList = new LinkedList<QuizItemOption>();
		List _list = (List) Json.fromJson(currentJson);
		Iterator<Map> iter = _list.iterator();
		while (iter.hasNext()) {
			Map optObj = iter.next();
			String index = (String) optObj.get("index");
			String content = (String) optObj.get("content");
			String categoryName = (String) optObj.get("categoryName");
			int value = (int) optObj.get("value");
			int categoryId = (int) optObj.get("categoryId") + categoryTblMaxId;

			QuizItemOption option = new QuizItemOption(index, content,
					categoryId, categoryName, value);
			optionList.add(option);
		}
		return Json.toJson(optionList);
	}
	
	/**
	 * 
	 * @param optIndex
	 * @return
	 */
	public QuizItemOption getOption(String optIndex) {
		if (!_bConverted) {
			this.convertOptions();
		}
		Iterator<QuizItemOption> iter = this.options.iterator();
		while (iter.hasNext()) {
			QuizItemOption option = iter.next();
			if (option.getIndex().equals(optIndex)) {
				return option;
			}
		}
		return null;
	}
}
