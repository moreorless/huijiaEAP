package com.huijia.eap.quiz.service.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.util.excel.ExcelParser;

@IocBean
public class QuizImportHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	@Inject("refer:quizCategoryService")
	private QuizCategoryService quizCategoryService;

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
	private static String EOF = "END"; // 结束标识符

	class Option {
		public String index; // ABCDE
		public String content; // 符合/不太符合...
		public int categoryId; // 维度名称
		public String categoryName; // 维度名称
		public int value; // 5/10/15

	}

	class Category {
		public int id; // 维度编号，从0开始
		public String name; // 维度名称
		public int priority;
		public int level;
		public int quizId;
		public int parentId;
		public String fullName;
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
		// int quizId;
		int itemNum;
		int categoryNum; // 主维度个数
		LinkedList<Category> categories;
		int lieBorder;
		ItemMeta itemMeta;

		SingleMeta singleMainMeta; // 个人评价表单->主维度评价信息
		SingleMeta singleSubMeta; // 个人评价表单->子维度评价信息
		TeamMeta teamMainMeta; // 团体评价表单-> 主维度评语信息
		TeamMeta teamSubMeta;// 团体评价表单-> 子维度评语信息
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
	private LinkedList<QuizEvaluation> quizEvaluationsSingleMain = new LinkedList<QuizEvaluation>();
	private LinkedList<QuizEvaluation> quizEvaluationsSingleSub = new LinkedList<QuizEvaluation>();
	private LinkedList<QuizEvaluation> quizEvaluationsTeamMain = new LinkedList<QuizEvaluation>();
	private LinkedList<QuizEvaluation> quizEvaluationsTeamSub = new LinkedList<QuizEvaluation>();

	public LinkedList<QuizItem> getQuizItems() {

		return this.quizItems;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluations() {

		return this.quizEvaluations;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsSingleMain() {

		return this.quizEvaluationsSingleMain;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsSingleSub() {

		return this.quizEvaluationsSingleSub;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsTeamMain() {

		return this.quizEvaluationsTeamMain;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluationsTeamSub() {

		return this.quizEvaluationsTeamSub;
	}

	public LinkedList<Category> getCategories() {

		return this.meta.categories;
	}

	public LinkedList<QuizCategory> getQuizCategories() {
		LinkedList<QuizCategory> quizCategories = new LinkedList<QuizCategory>();

		for (Category c : this.meta.categories) {
			QuizCategory quizCategory = new QuizCategory();
			quizCategory.setId(c.id);
			quizCategory.setFullName(c.fullName);
			quizCategory.setLevel(c.level);
			quizCategory.setName(c.name);
			quizCategory.setParentId(c.parentId);
			quizCategory.setPriority(c.priority);
			quizCategory.setQuizId(c.quizId);
			quizCategories.add(quizCategory);
		}

		return quizCategories;
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
		// this.meta.quizId = quizId;
		this.meta.itemNum = 0;
		this.meta.categoryNum = 0;
		this.meta.categories = new LinkedList<Category>();
		this.meta.lieBorder = 0;
		this.meta.itemMeta = new ItemMeta();
		this.meta.singleMainMeta = new SingleMeta();
		this.meta.singleSubMeta = new SingleMeta();
		this.meta.teamMainMeta = new TeamMeta();
		this.meta.teamSubMeta = new TeamMeta();
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

	private int locatePositionByString(String s, int sheetIndex,
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

	private int getCategoryIdByCategoryFullName(String name) {
		int id = -1;
		for (Iterator<Category> it = this.meta.categories.iterator(); it
				.hasNext();) {
			Category category = it.next();
			if (category.fullName.equals(name))
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
		if (locatePositionByString("题目个数", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到题目个数。");
			this.meta = null;
			return -1;
		}
		this.meta.itemNum = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (locatePositionByString("测谎线", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到测谎线。");
			this.meta = null;
			return -1;
		}
		this.meta.lieBorder = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (locatePositionByString("维度个数", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到主维度个数。");
			this.meta = null;
			return -1;
		}
		this.meta.categoryNum = Integer.parseInt(this.getCellValue(
				position.sheetIndex, position.rowIndex,
				position.columnIndex + 1));
		if (locatePositionByString("维度名称", SHEETINDEXFORITEM, position) == -1) {
			// 执行到这里，说明初始化表单0失败
			logger.error("处理题目表单失败：找不到主维度名称。");
			this.meta = null;
			return -1;
		}
		// 初始化主维度信息
		int	currentTblMaxId = 0; //当时测试代码遗留，不必修改
		for (int i = 0; i < this.meta.categoryNum; i++) {
			Category category = new Category();
			category.quizId = -1; // 在quizCategoryService中插入数据库时再赋予真正的quizId
			category.id = currentTblMaxId + i;
			category.level = 1;

			category.name = this.getCellValue(position.sheetIndex,
					position.rowIndex, position.columnIndex + 1 + i).split(":")[0];

			if (category.name.equals("")) {
				logger.error("处理题目表单失败：主维度名称为空。");
				this.meta = null;
				return -1;
			}
			for (Category c : this.meta.categories) {
				if (c.name.equals(category.name)) {
					logger.error("处理题目表单失败：主维度名称有重复。");
					this.meta = null;
					return -1;
				}
			}

			category.fullName = category.name;
			category.parentId = 0;
			try {
				category.priority = Integer.parseInt(this.getCellValue(
						position.sheetIndex, position.rowIndex,
						position.columnIndex + 1 + i).split(":")[1]);
			} catch (NumberFormatException ex) {
				logger.error("处理题目表单失败：维度优先级格式错误，非数字格式。");
				this.meta = null;
				return -1;
			}
			this.meta.categories.add(category);
		}
		// 初始化子维度信息
		int id_tmp = this.meta.categoryNum;
		for (int i = 0; i < this.meta.categoryNum; i++) {
			for (int j = 0;; j++) {
				if (this.getCellValue(position.sheetIndex,
						position.rowIndex + j + 1, position.columnIndex + 1 + i)
						.equals(EOF))
					break;
				if (j == FORMAX) {
					// 循环一直没有停止，出错
					logger.error("解析子维度失败：找不到<END>单元格，循环超过最大次数。");
					return -1;
				}

				Category category = new Category();
				category.id = currentTblMaxId + id_tmp;
				id_tmp++;
				category.quizId = -1;
				category.parentId = i;
				category.level = 2;

				String parentName = this.getCellValue(position.sheetIndex,
						position.rowIndex, position.columnIndex + 1 + i).split(
						":")[0];
				category.name = this
						.getCellValue(position.sheetIndex,
								position.rowIndex + j + 1,
								position.columnIndex + 1 + i).split(":")[0];
				category.fullName = parentName + "/" + category.name;
				try {
					category.priority = Integer.parseInt(this.getCellValue(
							position.sheetIndex, position.rowIndex + j + 1,
							position.columnIndex + 1 + i).split(":")[1]);
				} catch (NumberFormatException ex) {
					logger.error("处理题目表单失败：子维度优先级格式错误，非数字格式。");
					this.meta = null;
					return -1;
				}
				this.meta.categories.add(category);
			}
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
		if (locatePositionByString("题目编号", SHEETINDEXFORITEM, position) == -1) {
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
	 * // 初始化表单1中主维度的定位元素
	 */
	private int initSingleMainMeta() {

		CellPosition position = new CellPosition();

		if (locatePositionByString("一级维度", SHEETINDEXFORSINGLE, position) == -1) {
			// 执行到这里，说明初始化个人评估表单失败
			logger.error("个人评估表单中寻找主维度定位元素失败：在个人评估表单中找不到<一级维度>单元格。");
			this.meta = null;
			return -1;
		}

		this.meta.singleMainMeta.sheetIndex = position.sheetIndex;
		this.meta.singleMainMeta.rowIndex = position.rowIndex + 1;
		this.meta.singleMainMeta.categoryColumnIndex = position.columnIndex;

		String value;

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<维度>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("维度")) {
				this.meta.singleMainMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.singleMainMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.singleMainMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.singleMainMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.singleMainMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.singleMainMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单主维度元信息失败：个人评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.singleMainMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * // 初始化表单1中子维度的定位元素
	 */
	private int initSingleSubMeta() {

		CellPosition position = new CellPosition();

		if (locatePositionByString("二级维度", SHEETINDEXFORSINGLE, position) == -1) {
			// 执行到这里，说明初始化个人评估表单失败
			logger.error("个人评估表单中寻找子维度定位元素失败：在个人评估表单中找不到<二级维度>单元格。");
			this.meta = null;
			return -1;
		}

		this.meta.singleSubMeta.sheetIndex = position.sheetIndex;
		this.meta.singleSubMeta.rowIndex = position.rowIndex + 1;
		this.meta.singleSubMeta.categoryColumnIndex = position.columnIndex;

		String value;

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<维度>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("维度")) {
				this.meta.singleSubMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.singleSubMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.singleSubMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.singleSubMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.singleSubMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.singleSubMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估表单子维度元信息失败：个人评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.singleSubMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * 初始化表单1的元信息
	 */
	private int preprocessSheetSingle() {
		if (this.initSingleMainMeta() == -1) {
			logger.error("初始化个人评估表单主维度元信息失败。");
			return -1;
		}

		if (this.initSingleSubMeta() == -1) {
			logger.error("初始化个人评估表单子维度元信息失败。");
			return -1;
		}

		return 0;
	}

	/**
	 * // 初始化表单2（团体评价表单）中主维度的定位元素
	 */
	private int initTeamMainMeta() {

		CellPosition position = new CellPosition();
		if (locatePositionByString("一级维度", SHEETINDEXFORTEAM, position) == -1) {
			// 执行到这里，说明初始化团体评估表单失败
			logger.error("团体评估表单中寻找主维度定位元素失败：在团体评估表单中找不到<一级维度>单元格。");
			this.meta = null;
			return -1;
		}
		this.meta.teamMainMeta.sheetIndex = position.sheetIndex;
		this.meta.teamMainMeta.rowIndex = position.rowIndex + 1;
		this.meta.teamMainMeta.categoryColumnIndex = position.columnIndex;

		String value;
		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<维度>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("维度")) {
				this.meta.teamMainMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.teamMainMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.teamMainMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.teamMainMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.teamMainMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.teamMainMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单主维度元信息失败：团体评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.teamMainMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * // 初始化表单2（团体评价表单）中主维度的定位元素
	 */
	private int initTeamSubMeta() {

		CellPosition position = new CellPosition();
		if (locatePositionByString("二级维度", SHEETINDEXFORTEAM, position) == -1) {
			// 执行到这里，说明初始化团体评估表单失败
			logger.error("团体评估表单中寻找子维度定位元素失败：在团体评估表单中找不到<二级维度>单元格。");
			this.meta = null;
			return -1;
		}
		this.meta.teamSubMeta.sheetIndex = position.sheetIndex;
		this.meta.teamSubMeta.rowIndex = position.rowIndex + 1;
		this.meta.teamSubMeta.categoryColumnIndex = position.columnIndex;

		String value;
		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<维度>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("维度")) {
				this.meta.teamSubMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				this.meta.teamSubMeta.scoreColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				this.meta.teamSubMeta.healthColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<结果评价>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果评价")) {
				this.meta.teamSubMeta.evaluationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<解释>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("解释")) {
				this.meta.teamSubMeta.explanationColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议")) {
				this.meta.teamSubMeta.suggestionColumnIndex = i;
				break;
			}
		}

		for (int i = 0;; i++) {
			value = this.getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex, i);
			if (i == FORMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估表单子维度元信息失败：团体评估表单中找不到<特征>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("特征")) {
				this.meta.teamSubMeta.featureColumnIndex = i;
				break;
			}
		}

		return 0;
	}

	/**
	 * 初始化表单2的元信息
	 */
	private int preprocessSheetTeam() {
		if (this.initTeamMainMeta() == -1) {
			logger.error("初始化团体评估表单主维度元信息失败。");
			return -1;
		}
		if (this.initTeamSubMeta() == -1) {
			logger.error("初始化团体评估表单子维度元信息失败。");
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * @return 选项表
	 */
	private LinkedList<Option> getItemOptionList(int rowIndex, boolean isLie) {
		LinkedList<Option> optionList = new LinkedList<Option>();

		for (int i = 0;; i++) {
			if (i == FORMAX) {
				logger.error("获取题目选项信息失败。错误题目所在行号：" + rowIndex);
				return null;
			}

			String content;
			String categoryName;
			int categoryId = -1;

			int value;
			content = this.getCellValue(this.meta.itemMeta.sheetIndex,
					rowIndex, this.meta.itemMeta.optionColumnIndex + i);
			if (content.equals("")) {
				break;
			}
			categoryName = this.getCellValue(this.meta.itemMeta.sheetIndex,
					rowIndex + 1, this.meta.itemMeta.optionColumnIndex + i);

			if (!isLie) { // 如果不是测谎题，需要检验维度全称是否填写正确，并为CategoryID赋值
				for (Category c : this.meta.categories) {
					if (categoryName.equals(c.fullName))
						categoryId = c.id;
				}
				if (categoryId == -1) {
					logger.error("获取题目选项信息失败。维度全称<" + rowIndex + ">不在维度列表中.");
					return null;
				}
			} else { // 是测谎题的话，为CategoryID赋值为0
				categoryId = 0;
			}

			value = Integer.parseInt(this.getCellValue(
					this.meta.itemMeta.sheetIndex, rowIndex + 2,
					this.meta.itemMeta.optionColumnIndex + i));
			Option option = new Option();
			option.index = CHARARRAY[i];
			option.content = content;
			option.categoryName = categoryName;
			option.value = value;
			option.categoryId = categoryId;
			optionList.add(option);
		}
		return optionList;
	}

	/**
	 * 根据预处理得到的元信息，从Excel中导入题目
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
			LinkedList<Option> optionList = this.getItemOptionList(
					itemRowIndex, item.isLieFlag());
			if (optionList == null) {
				logger.error("解析题目表单失败：在第" + itemRowIndex + "行中解析题目选项失败");
				return -1;
			}
			LinkedList<QuizItemOption> quizItemOptionList = new LinkedList<QuizItemOption>();
			for (Iterator<Option> it = optionList.iterator(); it.hasNext();) {
				Option option = it.next();
				QuizItemOption quizItemOption = new QuizItemOption(
						option.index, option.content, option.categoryId,
						option.categoryName, option.value);
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
	 * @return 个人评价列表主维度部分
	 */
	private LinkedList<QuizEvaluation> generateSingleMainEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.categoryColumnIndex);
			if (value.equals("") || value.equals(EOF)) { // 到达最后一行，跳出循环
				break;
			}
			if (value.equals("二级维度") || value.equals("一级维度")) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：没有使用<END>标注结尾");
				return null;
			}

			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("singleMain");
			if (getCategoryIdByCategoryFullName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryFullName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析个人评估表单出错：第"
							+ this.meta.singleSubMeta.rowIndex + i + 1
							+ "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析个人评估表单出错：第" + this.meta.singleSubMeta.rowIndex
						+ i + 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.singleMainMeta.sheetIndex,
					this.meta.singleMainMeta.rowIndex + i + 1,
					this.meta.singleMainMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析个人主维度评语表单结束.");

		return evaluations;
	}

	/**
	 * 
	 * @return 个人评价列表主维度部分
	 */
	private LinkedList<QuizEvaluation> generateSingleSubEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.categoryColumnIndex);
			if (value.equals("") || value.equals(EOF)) { // 到达最后一行，跳出循环
				break;
			}
			if (value.equals("二级维度") || value.equals("一级维度")) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：没有使用<END>标注结尾");
				return null;
			}

			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("singleSub");
			if (getCategoryIdByCategoryFullName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryFullName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析个人评估表单出错：第"
							+ this.meta.singleSubMeta.rowIndex + i + 1
							+ "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析个人评估表单出错：第" + this.meta.singleSubMeta.rowIndex
						+ i + 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.singleSubMeta.sheetIndex,
					this.meta.singleSubMeta.rowIndex + i + 1,
					this.meta.singleSubMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析个人子维度评语表单结束.");

		return evaluations;
	}

	/**
	 * 
	 * @return 团队评价列表->主维度评价信息
	 */
	private LinkedList<QuizEvaluation> generateTeamMainEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.categoryColumnIndex);

			if (value.equals("") || value.equals(EOF)) { // 到达最后一行，跳出循环
				break;
			}
			if (value.equals("二级维度") || value.equals("一级维度")) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：没有使用<END>标注结尾");
				return null;
			}

			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("teamMain");
			if (getCategoryIdByCategoryFullName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryFullName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析团体评估表单出错：第"
							+ this.meta.teamMainMeta.rowIndex + i + 1
							+ "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析团体评估表单出错：第" + this.meta.teamMainMeta.rowIndex
						+ i + 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.teamMainMeta.sheetIndex,
					this.meta.teamMainMeta.rowIndex + i + 1,
					this.meta.teamMainMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析团体主维度评语表单结束.");

		return evaluations;
	}

	/**
	 * 
	 * @return 团队评价列表->主维度评价信息
	 */
	private LinkedList<QuizEvaluation> generateTeamSubEvaluations() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.categoryColumnIndex);

			if (value.equals("") || value.equals(EOF)) { // 到达最后一行，跳出循环
				break;
			}
			if (value.equals("二级维度") || value.equals("一级维度")) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：没有使用<END>标注结尾");
				return null;
			}

			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setId(0); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置
			evaluation.setType("teamSub");
			if (getCategoryIdByCategoryFullName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryFullName(value));
			evaluation.setCategoryName(value);

			// 获取“分数区间”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.scoreColumnIndex);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析团体评估表单出错：第"
							+ this.meta.teamSubMeta.rowIndex + i + 1
							+ "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else if (value.equals("")) {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			} else {
				logger.error("解析团体评估表单出错：第" + this.meta.teamSubMeta.rowIndex
						+ i + 1 + "行缺少分数区间");
				return null;
			}

			// 获取“健康状态”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.healthColumnIndex);
			evaluation.setHealthStatus(value);

			// 获取“结果评价”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.evaluationColumnIndex);
			evaluation.setEvaluation(value);

			// 获取“解释”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.explanationColumnIndex);
			evaluation.setExplanation(value);

			// 获取“建议”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.suggestionColumnIndex);
			evaluation.setSuggestion(value);

			// 获取“特征”
			value = getCellValue(this.meta.teamSubMeta.sheetIndex,
					this.meta.teamSubMeta.rowIndex + i + 1,
					this.meta.teamSubMeta.featureColumnIndex);
			evaluation.setFeature(value);

			evaluations.add(evaluation);
		}

		logger.info("解析团体子维度评语表单结束.");

		return evaluations;
	}

	/**
	 * 主处理流程
	 * 
	 * @param path
	 *            ：输入的excel文件路径
	 * @param isTest
	 *            ：是否在本机内测试；
	 * @return 成功返回0，失败返回1
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

		this.quizEvaluationsSingleMain = generateSingleMainEvaluations();
		if (this.quizEvaluationsSingleMain == null) {
			logger.error("导入问卷失败：未能成功解析个人主维度评估表单");
			return -1;
		}
		this.quizEvaluationsSingleSub = generateSingleSubEvaluations();
		if (this.quizEvaluationsSingleSub == null) {
			logger.error("导入问卷失败：未能成功解析个人子维度评估表单");
			return -1;
		}
		this.quizEvaluationsTeamMain = generateTeamMainEvaluations();
		if (this.quizEvaluationsTeamMain == null) {
			logger.error("导入问卷失败：未能成功解析团体主维度评估表单");
			return -1;
		}
		this.quizEvaluationsTeamSub = generateTeamSubEvaluations();
		if (this.quizEvaluationsTeamSub == null) {
			logger.error("导入问卷失败：未能成功解析团体子维度评估表单");
			return -1;
		}

		for (Iterator<QuizEvaluation> it = this.quizEvaluationsSingleMain
				.iterator(); it.hasNext();)
			this.quizEvaluations.add(it.next());
		for (Iterator<QuizEvaluation> it = this.quizEvaluationsSingleSub
				.iterator(); it.hasNext();)
			this.quizEvaluations.add(it.next());
		for (Iterator<QuizEvaluation> it = this.quizEvaluationsTeamMain
				.iterator(); it.hasNext();)
			this.quizEvaluations.add(it.next());
		for (Iterator<QuizEvaluation> it = this.quizEvaluationsTeamSub
				.iterator(); it.hasNext();)
			this.quizEvaluations.add(it.next());
		logger.info("导入问卷成功: 导入题目" + this.quizItems.size() + "个；\n导入个人主维度报告评语"
				+ this.quizEvaluationsSingleMain.size() + "条；\n导入个人子维度报告评语"
				+ this.quizEvaluationsSingleSub.size() + "条；\n导入团体主维度评语"
				+ this.quizEvaluationsTeamMain.size() + "条；\n导入团体子维度评语"
				+ this.quizEvaluationsTeamSub.size() + "条；\n导入评语总数"
				+ quizEvaluations.size() + "条.\n");
		return 0;

	}

	public static void main(String[] args) throws Exception {

		// int t = Integer.parseInt("123abc");

		// QuizItemOption quizItemOption = new QuizItemOption();

		// String s = "13-100";
		// System.out.println(s.matches("[0-9]+-[0-9]+"));

		String s = "a/b/c/d";

		System.out.println(s.split("/")[1]);

		QuizImportHandler quizImportHandler = new QuizImportHandler();
		// quizImportHandler.process("D:\\20140120_quiz3_冲突处理.xls");
		// quizImportHandler.process("D:\\20140312_quiz1_个人心理分析.xls");
		// quizImportHandler.process("D:\\20140312_quiz2.1_沟通风格.xls");
		// quizImportHandler.process("D:\\20140312_quiz2.2_冲突处理.xls");
		// quizImportHandler.process("D:\\20150316_quiz3_企业员工调研问卷.xls");
		quizImportHandler.process("D:\\20150316_quiz4_情绪管理倾向.xls");

		// quizImportHandler.initSheetPositions
		// System.out.println(quizImportHandler.getOptionNum());
		// System.out.println(quizImportHandler.getCategoryNum());
		return;

	}
}
