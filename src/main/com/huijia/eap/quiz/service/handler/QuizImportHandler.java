package com.huijia.eap.quiz.service.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.nutz.json.Json;

import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.util.excel.ExcelParser;

public class QuizImportHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	/***************************************** 公共数据结构区 *****************************************/

	private ExcelParser excel;

	private static String CHARARRAY[] = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	private static int SHEETINDEXFORITEM = 0;
	private static int SHEETINDEXFORSINGLE = 1;
	private static int SHEETINDEXFORTEAM = 2;
	private static int FORMAX = 8192; // 最多循环次数
	private static int SEARCHMAX = 128; // 查找定位元素的最大范围限定在128*128的方格内
	private static int ROWNUMPERITEM = 3; // 在题目表单中，每道题目所占的行数

	class Option {
		public String index; // ABCDE
		public String content; // 符合/不太符合...
		public String categoryName; // 维度名称
		public int value; // 5/10/15

	}

	class Category {
		public int id; // 维度编号，从0开始
		public String name; // 维度名称
		public int priority;
	}

	class CellPosition {
		public int sheetIndex; // 0
		public int rowIndex; // 1
		public int columnIndex; // 1 "ABCD"
	}

	/**
	 * 题目表单元信息结构体
	 */
	class ItemMeta {
		int sheetIndex; // 题目所在的表单号
		int rowIndex; // 标题行所在的行号
		int serialColumnIndex; // 题目编号所在的
		int lieColumnIndex; // 是否测谎题所在的列号
		int questionColumnIndex; // 题目内容所在的列号
		int optionColumnIndex; // 第一个选项所在的列号
	}

	/**
	 * 个人评价信息表单原信息结构体
	 */
	class SingleMeta {
		int sheetIndex; // 个人评估结果所在的表单号
		int rowIndex; // 标题行所在的行号
		int categoryColumnIndex;
		int scoreColumnIndex;
		int healthColumnIndex;
		int evaluationColumnIndex;
		int explanationColumnIndex;
		int suggestionColumnIndex;
		int featureColumnIndex;
	}

	/**
	 * 团体评价信息表单原信息结构体
	 */
	class TeamMeta {
		int sheetIndex; // 个人评估结果所在的表单号
		int rowIndex; // 标题行所在的行号
		int categoryColumnIndex;
		int scoreColumnIndex;
		int healthColumnIndex;
		int evaluationColumnIndex;
		int explanationColumnIndex;
		int suggestionColumnIndex;
		int featureColumnIndex;
	}

	/**
	 * 试卷元信息
	 * 
	 * 题目数量、维度数量及名称、测谎题分解线 题目表单元信息 个人评估表单元信息 团体评估表单元信息
	 * 
	 * @author jianglei
	 * 
	 */
	class QuizMeta {
		int itemNum;
		int categoryNum;
		LinkedList<Category> categories;
		int lieBorder;
		ItemMeta itemMeta;
		SingleMeta singleMeta;
		TeamMeta teamMeta;
	}

	/***************************************** 问卷具体数据区 *****************************************/
	/**
	 * 整个导入的问卷内容包括：问卷元信息+题目列表+评估结果列表 评估结果列表由两部分组成：个人评估结果列表+团体评估结果列表
	 */
	// 问卷元信息
	private QuizMeta meta = new QuizMeta();

	// 題目列表
	private LinkedList<QuizItem> quizItems = new LinkedList<QuizItem>();
	// 评估结果列表
	private LinkedList<QuizEvaluation> quizEvaluations = new LinkedList<QuizEvaluation>();
	private LinkedList<QuizEvaluation> quizEvaluationsSingle = new LinkedList<QuizEvaluation>();
	private LinkedList<QuizEvaluation> quizEvaluationsTeam = new LinkedList<QuizEvaluation>();

	public LinkedList<QuizItem> getQuizItems() {

		return this.quizItems;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluations() {

		return this.quizEvaluations;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsSingle() {

		return this.quizEvaluationsSingle;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsTeam() {

		return this.quizEvaluationsTeam;
	}

	public LinkedList<Category> getCategories() {

		return this.meta.categories;
	}

	public String getCategoryJson() {
		return Json.toJson(this.meta.categories);
	}

	public int getCategoryNum() {
		return this.meta.categoryNum;
	}

	public int getItemNum() {
		return this.meta.itemNum;
	}

	public int getLieBorder() {
		return this.meta.lieBorder;
	}

	/***************************************** 公共函数区 *****************************************/

	public QuizImportHandler() {
		// 初始化试卷元信息
		this.meta.itemNum = 0;
		this.meta.categoryNum = 0;
		this.meta.categories = new LinkedList<Category>();
		this.meta.lieBorder = 0;
		this.meta.itemMeta = new ItemMeta();
		this.meta.singleMeta = new SingleMeta();
		this.meta.teamMeta = new TeamMeta();
	}

	private String getCellValue(int sheetIndex, int rowIndex, int columnIndex) {

		try {
			return excel.getCellStringValue(sheetIndex, rowIndex, columnIndex)
					.trim();
		} catch (IOException e) {
			logger.error("Cannot Get Excel Cell of (" + sheetIndex + ", "
					+ rowIndex + ", " + columnIndex + ")");
			return null;
		}
	}

	private int getPositionByString(String s, int sheetIndex,
			CellPosition position) {
		for (int i = 0; i < SEARCHMAX; i++) {
			for (int j = 0; j < SEARCHMAX; j++)
				if (this.getCellValue(sheetIndex, i, j).equals(s)) {
					position.sheetIndex = sheetIndex;
					position.rowIndex = i;
					position.columnIndex = j;
					return 0;
				}
		}
		return -1;
	}

	private int getCategoryIdByCategoryName(String name) {
		int id = -1;
		for (Iterator<Category> it = this.meta.categories.iterator(); it
				.hasNext();) {
			Category category = it.next();
			if (category.name.equals(name))
				return category.id;
		}
		return id;
	}

	/***************************************** 具体过程函数区 *****************************************/

	/**
	 * 对题目表单进行预处理
	 * 
	 * 1. 初始化题目个数、维度个数等
	 * 
	 * 2. 初始化Item元信息 初始化题目表单元信息
	 */
	private int preprocessSheetItem() {
		CellPosition position = new CellPosition();
		if (getPositionByString("题目个数", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到题目个数。");
			this.meta = null;
			return -1;
		}
		this.meta.itemNum = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (getPositionByString("测谎线", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到测谎线。");
			this.meta = null;
			return -1;
		}
		this.meta.lieBorder = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (getPositionByString("维度个数", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到维度个数。");
			this.meta = null;
			return -1;
		}
		this.meta.categoryNum = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (getPositionByString("维度名称", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到维度名称。");
			this.meta = null;
			return -1;
		}
		for (int i = 0; i < this.meta.categoryNum; i++) {
			Category category = new Category();
			category.id = i;
			category.name = this.getCellValue(position.sheetIndex,
					position.rowIndex, position.columnIndex + 1 + i);
			if (category.name.equals("")) {
				logger.error("处理题目表单失败：维度名称个数与维度个数不符。");
				this.meta = null;
				return -1;
			}
			try {
				category.priority = Integer.parseInt(this.getCellValue(
						position.sheetIndex, position.rowIndex + 1,
						position.columnIndex + 1 + i));
			} catch (NumberFormatException ex) {
				logger.error("处理题目表单失败：维度优先级格式错误，非数字格式。");
				this.meta = null;
				return -1;
			}

			this.meta.categories.add(category);
		}

		if (this.initItemMeta() == -1) {
			logger.error("初始化题目元信息失败。");
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * @return -1表示失败，0表示成功
	 */
	private int initItemMeta() {

		CellPosition position = new CellPosition();
		if (getPositionByString("题目编号", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("题目表单中寻找定位元素失败：在题目表单中找不到<题目编号>单元格。");
			this.meta = null;
			return -1;
		}
		this.meta.itemMeta.sheetIndex = position.sheetIndex;
		this.meta.itemMeta.rowIndex = position.rowIndex;
		this.meta.itemMeta.serialColumnIndex = position.columnIndex;

		String value;
		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					this.meta.itemMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：表单0中找不到<是否测谎题>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("是否测谎题")) {
				this.meta.itemMeta.lieColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					this.meta.itemMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：表单0中找不到<题目>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("题目")) {
				this.meta.itemMeta.questionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					this.meta.itemMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：表单0中找不到<选项>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("选项")) {
				this.meta.itemMeta.optionColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * // 初始化表单1的定位元素
	 */
	private int initSingleMeta() {

		CellPosition position = new CellPosition();
		if (getPositionByString("维度", SHEETINDEXFORSINGLE, position) == -1) {
			// 执行到这里，说明初始化个人评估表单失败
			logger.error("个人评估表单中寻找定位元素失败：在个人评估表单中找不到<维度>单元格。");
			this.meta = null;
			return -1;
		}
		this.meta.singleMeta.sheetIndex = position.sheetIndex;
		this.meta.singleMeta.rowIndex = position.rowIndex;
		this.meta.singleMeta.categoryColumnIndex = position.columnIndex;

		String value;
		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.singleMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.singleMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.singleMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.singleMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.singleMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单失败：个人评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.singleMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * 初始化表单1的元信息
	 */
	private int preprocessSheetSingle() {
		if (this.initSingleMeta() == -1) {
			logger.error("初始化个人评估表单元信息失败。");
			return -1;
		}

		return 0;
	}

	/**
	 * // 初始化表单2的定位元素
	 */
	/**
	 * // 初始化表单1的定位元素
	 */
	private int initTeamMeta() {

		CellPosition position = new CellPosition();
		if (getPositionByString("维度", SHEETINDEXFORTEAM, position) == -1) {
			// 执行到这里，说明初始化团体评估表单失败
			logger.error("团体评估表单中寻找定位元素失败：在团体评估表单中找不到<维度>单元格。");
			this.meta = null;
			return -1;
		}
		this.meta.teamMeta.sheetIndex = position.sheetIndex;
		this.meta.teamMeta.rowIndex = position.rowIndex;
		this.meta.teamMeta.categoryColumnIndex = position.columnIndex;

		String value;
		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.teamMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.teamMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.teamMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.teamMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.teamMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单失败：团体评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.teamMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * 初始化表单2的元信息
	 */
	private int preprocessSheetTeam() {
		if (this.initTeamMeta() == -1) {
			logger.error("初始化团体评估表单元信息失败。");
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * @return 选项表
	 */
	private LinkedList<Option> getItemOptionList(int rowIndex) {
		LinkedList<Option> optionList = new LinkedList<Option>();

		for (int i = 0;; i++) {
			if (i == FORMAX) {
				logger.error("获取题目选项信息失败。错误题目所在行号：" + rowIndex);
				return null;
			}

			String content;
			String categoryName;
			int value;
			content = this.getCellValue(this.meta.itemMeta.sheetIndex,
					rowIndex, this.meta.itemMeta.optionColumnIndex + i);
			if (content.equals("")) {
				break;
			}
			categoryName = this.getCellValue(this.meta.itemMeta.sheetIndex,
					rowIndex + 1, this.meta.itemMeta.optionColumnIndex + i);
			value = Integer.parseInt(this.getCellValue(
					this.meta.itemMeta.sheetIndex, rowIndex + 2,
					this.meta.itemMeta.optionColumnIndex + i));
			Option option = new Option();
			option.index = CHARARRAY[i];
			option.content = content;
			option.categoryName = categoryName;
			option.value = value;
			optionList.add(option);
		}
		return optionList;
	}

	/**
	 * 
	 * @return 成功返回0，失败返回-1
	 */
	private int generateItems() {

		String value;
		for (int i = 0; i < this.meta.itemNum; i++) {
			int itemRowIndex = this.meta.itemMeta.rowIndex + 1 + ROWNUMPERITEM
					* i;
			QuizItem item = new QuizItem();

			// 解析 题目编号
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					itemRowIndex, this.meta.itemMeta.serialColumnIndex);
			if (value.equals("")) {
				logger.error("解析题目表单失败：在第" + itemRowIndex + "行中找不到题目编号" + i + 1);
				return -1;
			}
			item.setId(Integer.parseInt(value));

			// 解析 是否测谎题
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					itemRowIndex, this.meta.itemMeta.lieColumnIndex);
			if (value.equals("是")) {
				item.setLieFlag(true);

			} else if (value.equals("否")) {
				item.setLieFlag(false);
			} else {
				logger.error("解析题目表单失败：在第" + itemRowIndex + "行中<是否测谎题>格式错误");
				return -1;
			}

			// 解析 题目
			value = this.getCellValue(this.meta.itemMeta.sheetIndex,
					itemRowIndex, this.meta.itemMeta.questionColumnIndex);
			if (value.equals("")) {
				logger.error("解析题目表单失败：在第" + itemRowIndex + "行中找不到题目内容");
				return -1;
			}
			item.setQuestion(value);

			// 解析 选项
			LinkedList<Option> optionList = this
					.getItemOptionList(itemRowIndex);
			if (optionList == null) {
				logger.error("解析题目表单失败：在第" + itemRowIndex + "行中解析题目选项失败");
				return -1;
			}
			LinkedList<QuizItemOption> quizItemOptionList = new LinkedList<QuizItemOption>();
			for (Iterator<Option> it = optionList.iterator(); it.hasNext();) {
				Option option = it.next();
				QuizItemOption quizItemOption = new QuizItemOption(
						option.index, option.content, option.categoryName,
						option.value);
				quizItemOptionList.add(quizItemOption);
			}
			item.setOptions(quizItemOptionList);
			item.setOptionJson(Json.toJson(quizItemOptionList));

			this.quizItems.add(item);
		}
		return 0;
	}

	/**
	 * 
	 * @return 个人评价列表
	 */
	private LinkedList<QuizEvaluation> generateSingleEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.categoryColumnIndex);
			if (value.equals("")) { // 到达最后一行，跳出循环
				break;
			}
			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("single");
			if (getCategoryIdByCategoryName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析个人评估表单出错：第" + this.meta.teamMeta.rowIndex
							+ i + 1 + "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析个人评估表单出错：第" + this.meta.teamMeta.rowIndex + i
						+ 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.singleMeta.sheetIndex,
					this.meta.singleMeta.rowIndex + i + 1,
					this.meta.singleMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析个人评语表单结束.");

		return evaluations;
	}

	/**
	 * 
	 * @return 个人评价列表
	 */
	private LinkedList<QuizEvaluation> generateTeamEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.categoryColumnIndex);
			if (value.equals("")) { // 到达最后一行，跳出循环
				break;
			}
			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("team");
			if (getCategoryIdByCategoryName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析团体评估表单出错：第" + this.meta.teamMeta.rowIndex
							+ i + 1 + "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析团体评估表单出错：第" + this.meta.teamMeta.rowIndex + i
						+ 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.teamMeta.sheetIndex,
					this.meta.teamMeta.rowIndex + i + 1,
					this.meta.teamMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析团体评语表单结束.");

		return evaluations;
	}

	/**
	 * 主处理流程
	 */
	public int process(String path) {
		this.excel = new ExcelParser(path);
		if (excel.initSuccess() == false) {
			logger.error("导入问卷失败：非法的Excel文件格式。");
			return -1;
		}

		// Step1 : 定位各个表单的表头元素

		if (this.preprocessSheetItem() == -1) {
			logger.error("导入问卷失败：未能成功初始化题目表单");
			return -1;
		}
		if (this.preprocessSheetSingle() == -1) {
			logger.error("导入问卷失败：未能成功初始化个人评估表单");
			return -1;
		}
		if (this.preprocessSheetTeam() == -1) {
			logger.error("导入问卷失败：未能成功初始化团体评估表单");
			return -1;
		}

		if (this.generateItems() == -1) {
			logger.error("导入题目失败：未能成功解析题目表单");
			return -1;
		}

		this.quizEvaluationsSingle = generateSingleEvaluations();
		if (this.quizEvaluationsSingle == null) {
			logger.error("导入问卷失败：未能成功解析个人评估表单");
			return -1;
		}
		this.quizEvaluationsTeam = generateTeamEvaluations();
		if (this.quizEvaluationsTeam == null) {
			logger.error("导入问卷失败：未能成功解析团体评估表单");
			return -1;
		}

		for (Iterator<QuizEvaluation> it = this.quizEvaluationsSingle
				.iterator(); it.hasNext();)
			this.quizEvaluations.add(it.next());
		for (Iterator<QuizEvaluation> it = this.quizEvaluationsTeam.iterator(); it
				.hasNext();)
			this.quizEvaluations.add(it.next());
		logger.info("导入问卷成功: 导入题目" + this.quizItems.size() + "个；导入个人报告评语"
				+ this.quizEvaluationsSingle.size() + "条；导入团体评语"
				+ this.quizEvaluationsTeam.size() + "条；导入评语总数"
				+ quizEvaluations.size() + "条");
		return 0;

	}

	public static void main(String[] args) throws Exception {

		// int t = Integer.parseInt("123abc");

		// QuizItemOption quizItemOption = new QuizItemOption();

		// String s = "13-100";
		// System.out.println(s.matches("[0-9]+-[0-9]+"));

		QuizImportHandler quizImportHandler = new QuizImportHandler();
		quizImportHandler.process("D:\\20140120_quiz3_冲突处理.xls");
		// quizImportHandler.initSheetPositions
		// System.out.println(quizImportHandler.getOptionNum());
		// System.out.println(quizImportHandler.getCategoryNum());
		return;

	}
}
