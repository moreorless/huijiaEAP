package com.huijia.eap.quiz.service.excel;

import java.io.IOException;
import java.util.HashSet;
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
	/**
	 * 第0个表单：存放具体的题目 第1个表单：存放个人报告评语 第2个表单：存放团体报告评语
	 */
	private ExcelParser excel;
	private static int rowIndexForSearch = 36;
	private static String columnIndexForSearch[] = { "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N" };
	private static String COLUMNARRAY[] = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	private static int SHEETINDEXFORITEM = 0;
	private static int SHEETINDEXFORSINGLE = 1;
	private static int SHEETINDEXFORTEAM = 2;
	private static int ITEMMAX = 65536; // 最多题目个数

	QuizImportHandler(String path) {
		this.excel = new ExcelParser(path);
	}

	/**
	 * @param sheetIndex
	 *            (表单)
	 * @param columnIndex
	 *            （栏）
	 * @param rowIndex
	 *            (行)
	 */
	class CellPosition {
		public int sheetIndex; // 0
		public int rowIndex; // 1
		public String columnIndex; // "A"
	}

	class Option {
		public String index; // ABCDE
		public String name; // 符合/不太符合...
		public String value; // 5/10/15
	}

	class Category {
		public int id; // 维度编号，0为总分维度，从1开始输入每个题目所属维度
		public String name; // 维度名称
	}

	// 需要返回的数据
	private LinkedList<Option> options = new LinkedList<Option>();
	private LinkedList<Category> categories = new LinkedList<Category>();
	private LinkedList<QuizItem> quizItems = new LinkedList<QuizItem>();
	private LinkedList<QuizEvaluation> quizEvaluations = new LinkedList<QuizEvaluation>();

	private int itemNum = 0; // 题目个数
	private int optionNum = 0; // 选项个数
	private int categoryNum = 0;// 维度个数

	// Sheet0（题目列表）的初始元素存放位置：“题目编号”的位置
	private CellPosition initPostionSheetItem = new CellPosition();
	private String lieColumnIndexOfSheetItem; // 测谎题所在列号
	private String categoryColumnIndexOfSheetItem; // 维度所在列号
	private String questionColumnIndexOfSheetItem; // 题目所在列号
	private int optionAColumnIndexOfSheetItem; // 选项A所在列号（整数型）

	// Sheet1（个人评估）的初始元素存放位置：“题目编号”的位置
	private CellPosition initPostionSheetSingle = new CellPosition();
	private String scoreColumnIndexOfSheetSingle; // 分数区间所在列号
	private String healthColumnIndexOfSheetSingle; // 健康状况所在列号
	private String evaluationColumnIndexOfSheetSingle; // 结果描述所在列号
	private String suggestionColumnIndexOfSheetSingle; // 建议所在列号

	// Sheet2（团体报告评语）的初始元素存放位置：“维度”的位置
	private CellPosition initPostionSheetTeam = new CellPosition();
	private String scoreColumnIndexOfSheetTeam; // 分数区间所在列号
	private String healthColumnIndexOfSheetTeam; // 健康状况所在列号
	private String evaluationColumnIndexOfSheetTeam; // 结果描述所在列号
	private String suggestionColumnIndexOfSheetTeam; // 建议所在列号

	private String getCellValue(int sheetIndex, int rowIndex, String columnIndex) {

		try {
			return excel.getCellStringValue(sheetIndex, rowIndex, columnIndex)
					.trim();
		} catch (IOException e) {
			logger.error("Cannot Get Excel Cell of (" + sheetIndex + ", "
					+ rowIndex + ", " + columnIndex + ")");
			return null;
		}
	}

	private String getCellValue(CellPosition position) {

		try {
			return excel.getCellStringValue(position.sheetIndex,
					position.rowIndex, position.columnIndex).trim();
		} catch (IOException e) {
			logger.error("Error: cannot Get Excel Cell of ("
					+ position.sheetIndex + ", " + position.rowIndex + ", "
					+ position.columnIndex + ")");
			return null;
		}
	}

	public LinkedList<QuizItem> getQuizItems() {

		return this.quizItems;
	}

	public LinkedList<QuizEvaluation> getQuizEvaluations() {

		return this.quizEvaluations;
	}

	public LinkedList<Option> getOptions() {

		return this.options;
	}

	public LinkedList<Category> getCategories() {

		return this.categories;
	}

	/**
	 * 初始化表单0的定位元素
	 */
	private int preprocessSheetItem() {
		if (this.initSheetItemPosition() == -1) {
			logger.error("在题目表单中寻找定位元素失败。");
			return -1;
		}
		if (this.initSheetItemHeaderElements() == -1) {
			logger.error("在题目表单中初始化表头位置失败。");
			return -1;
		}
		return 0;
	}

	/**
	 * // 初始化表单0的定位元素<题目编号>
	 */
	private int initSheetItemPosition() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORITEM;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];

				if (this.getCellValue(position).equals("题目编号")) {
					this.initPostionSheetItem.sheetIndex = position.sheetIndex;
					this.initPostionSheetItem.rowIndex = position.rowIndex;
					this.initPostionSheetItem.columnIndex = position.columnIndex;
					logger.info("成功获取表单0定位元素<题目编号>位置:("
							+ this.initPostionSheetItem.sheetIndex + ", "
							+ this.initPostionSheetItem.rowIndex + ", "
							+ this.initPostionSheetItem.columnIndex + ")");
					return 0;
				}
			}
		}
		// 执行到这里，说明初始化表单0失败
		this.initPostionSheetItem = null;
		logger.error("错误：在题目表单中找不到<题目编号>单元格。");
		return -1;
	}

	/**
	 * 
	 * @return -1表示失败，0表示成功
	 */
	private int initSheetItemHeaderElements() {
		int i = 0;
		String columnChar = this.initPostionSheetItem.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<是否测谎题>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("是否测谎题")) {
				lieColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到测谎列
				return -1;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<维度>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("维度")) {
				categoryColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return -1;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<题目>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("题目")) {
				questionColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return -1;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<A>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("A")) {
				optionAColumnIndexOfSheetItem = columnIndex + i;
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return -1;
		}
		return 0;
	}

	/**
	 * // 初始化表单1的定位元素
	 */
	private int initSheetSinglePosition() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORSINGLE;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];
				if (this.getCellValue(position).equals("维度")) {
					this.initPostionSheetSingle.sheetIndex = position.sheetIndex;
					this.initPostionSheetSingle.rowIndex = position.rowIndex;
					this.initPostionSheetSingle.columnIndex = position.columnIndex;
					logger.info("成功获取表单1定位元素<维度>位置:("
							+ this.initPostionSheetSingle.sheetIndex + ", "
							+ this.initPostionSheetSingle.rowIndex + ", "
							+ this.initPostionSheetSingle.columnIndex + ")");
					return 0;
				}
			}
		}
		// 执行到这里，说明初始化表单1失败
		this.initPostionSheetSingle = null;
		logger.error("错误：在个人评估表单中找不到<维度>单元格。");
		return -1;
	}

	private int initSheetSingleHeaderElements() {
		int i = 0;
		String columnChar = this.initPostionSheetSingle.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex,
					COLUMNARRAY[columnIndex + i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估结果表头元素失败：表单1中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				scoreColumnIndexOfSheetSingle = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在个人评估表单中没有找到<分数区间>表头");
				scoreColumnIndexOfSheetSingle = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex,
					COLUMNARRAY[columnIndex + i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估结果表头元素失败：表单1中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				healthColumnIndexOfSheetSingle = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在个人评估表单中没有找到<健康状态>表头");
				healthColumnIndexOfSheetSingle = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex,
					COLUMNARRAY[columnIndex + i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估结果表头元素失败：表单1中找不到<结果描述>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果描述")) {
				evaluationColumnIndexOfSheetSingle = COLUMNARRAY[columnIndex
						+ i];
				break;
			}
			if (value.equals("")) {
				logger.error("在个人评估表单中没有找到<结果描述>表头");
				evaluationColumnIndexOfSheetSingle = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex,
					COLUMNARRAY[columnIndex + i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析个人评估结果表头元素失败：表单1中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议") || value.equals("解释")) {
				suggestionColumnIndexOfSheetSingle = COLUMNARRAY[columnIndex
						+ i];
				break;
			}
			if (value.equals("")) {
				logger.error("在个人评估表单中没有找到<建议>或者<解释>表头");
				suggestionColumnIndexOfSheetSingle = "Z";
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 初始化表单1的定位元素
	 */
	private int preprocessSheetSingle() {
		if (this.initSheetSinglePosition() == -1) {
			logger.error("在个人评估表单中寻找定位元素失败。");
			return -1;
		}
		if (this.initSheetSingleHeaderElements() == -1) {
			logger.error("在个人评估表单中初始化表头元素失败。");
			return -1;
		}
		return 0;
	}

	/**
	 * // 初始化表单2的定位元素
	 */
	private int initSheetTeamPosition() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORTEAM;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];
				if (this.getCellValue(position).equals("维度")) {
					this.initPostionSheetTeam.sheetIndex = position.sheetIndex;
					this.initPostionSheetTeam.rowIndex = position.rowIndex;
					this.initPostionSheetTeam.columnIndex = position.columnIndex;
					logger.info("成功获取表单2定位元素<维度>位置:("
							+ this.initPostionSheetTeam.sheetIndex + ", "
							+ this.initPostionSheetTeam.rowIndex + ", "
							+ this.initPostionSheetTeam.columnIndex + ")");
					return 0;
				}
			}
		}
		// 执行到这里，说明初始化表单1失败
		this.initPostionSheetTeam = null;
		logger.error("错误：在团体评估表单中找不到<维度>单元格。");
		return -1;
	}

	private int initSheetTeamHeaderElements() {
		int i = 0;
		String columnChar = this.initPostionSheetTeam.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估结果表头元素失败：表单2中找不到<分数区间>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("分数区间")) {
				scoreColumnIndexOfSheetTeam = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在团体评估表单中没有找到<分数区间>表头");
				scoreColumnIndexOfSheetTeam = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估结果表头元素失败：表单2中找不到<健康状态>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("健康状态")) {
				healthColumnIndexOfSheetTeam = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在团体评估表单中没有找到<健康状态>表头");
				healthColumnIndexOfSheetTeam = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估结果表头元素失败：表单2中找不到<结果描述>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("结果描述")) {
				evaluationColumnIndexOfSheetTeam = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在团体评估表单中没有找到<结果描述>表头");
				evaluationColumnIndexOfSheetTeam = "Z";
				return -1;
			}
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("解析团体评估结果表头元素失败：表单2中找不到<建议>单元格，循环超过最大次数。");
				return -1;
			}
			if (value.equals("建议") || value.equals("解释")) {
				suggestionColumnIndexOfSheetTeam = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) {
				logger.error("在团体评估表单中没有找到<建议>或者<解释>表头");
				suggestionColumnIndexOfSheetTeam = "Z";
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 初始化表单2的定位元素
	 */
	private int preprocessSheetTeam() {
		if (this.initSheetTeamPosition() == -1) {
			logger.error("在团体评估表单中寻找定位元素失败。");
			return -1;
		}
		if (this.initSheetTeamHeaderElements() == -1) {
			logger.error("在团体评估表单中寻找定位元素失败。");
			return -1;
		}

		return 0;
	}

	/**
	 * 
	 * @return 选项个数，返回-1表示失败
	 */
	private int initOptions() {
		int i = 0;
		String columnChar = this.initPostionSheetItem.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;
		int num = 0;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("表单0中初始化题目选项失败：循环超过最大次数。");
				return -1;
			}
			Option option = new Option();
			if (num > 0 && value.equals("") == false
					&& value.equals("A") == false) {
				num++;
				option.index = value;
				option.name = this.getCellValue(SHEETINDEXFORITEM,
						this.initPostionSheetItem.rowIndex - 1,
						COLUMNARRAY[columnIndex + i]);

				options.add(option);
			}
			if (value.equals("A")) {
				num = 1;
				option.index = value;
				option.name = this.getCellValue(SHEETINDEXFORITEM,
						this.initPostionSheetItem.rowIndex - 1,
						COLUMNARRAY[columnIndex + i]);
				options.add(option);
			}
			if (value.equals(""))
				break;
		}
		return num;
	}

	private int initCategories() {
		int i = 0;

		String lieValue;
		String categoryValue;
		HashSet<String> categorySet = new HashSet<String>();
		for (i = 0; i < this.itemNum; i++) {
			lieValue = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					lieColumnIndexOfSheetItem);
			categoryValue = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					categoryColumnIndexOfSheetItem);

			if (lieValue.equals("是")) {
				continue;
			} else if (lieValue.equals("否")) {
				if (categoryValue.equals("")) {
					logger.error("获取维度个数失败：格式错误，非测谎题但未标明维度。题目编号为: " + (i + 1));
					return -1; // 不是测谎提，但是没有存放维度名称，错误
				}
				if (categorySet.contains(categoryValue))
					continue;
				categorySet.add(categoryValue);
			} else {
				logger.error("获取维度个数失败：格式错误，不能判断是否测谎题。题目编号为: " + (i + 1));
				return -1; // 出错
			}
		}

		Category tmp = new Category();
		tmp.id = 0;
		tmp.name = "总分";

		categories.add(tmp);

		for (Iterator<String> it = categorySet.iterator(); it.hasNext();) {
			Category category = new Category();
			category.id = categories.size() + 1;
			category.name = it.next();
			categories.add(category);
		}

		// System.out.println(Categories[0]);

		return categorySet.size();
	}

	/**
	 * 
	 * @return 题目个数
	 */
	private int initItems() {

		int i = 0;
		String value;
		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.initPostionSheetItem.columnIndex);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("表单0中初始化题目失败：循环超过最大次数。");
				return -1;
			}
			if (value.equals(""))
				break;
			QuizItem item = new QuizItem();
			item.setCategory(getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.categoryColumnIndexOfSheetItem));
			item.setQuestion(getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.questionColumnIndexOfSheetItem));
			if (getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.lieColumnIndexOfSheetItem).equals("是"))
				item.setLieFlag(true);
			else if (getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.lieColumnIndexOfSheetItem).equals("否"))
				item.setLieFlag(false);
			else {
				logger.error("表单0中初始化题目失败：无法判断题目" + i + 1 + "是否为测谎题。");
				return -1;
			}
			item.setId(Integer.parseInt(getCellValue(
					this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					this.initPostionSheetItem.columnIndex)));
			// item.setOptions(this.options);
			LinkedList<QuizItemOption> optionList = new LinkedList<QuizItemOption>();

			for (Iterator<Option> it = options.iterator(); it.hasNext();) {

				Option option = it.next();
				QuizItemOption quizItemOption = new QuizItemOption(
						option.index, option.name,
						Integer.parseInt(getCellValue(
								this.initPostionSheetItem.sheetIndex,
								this.initPostionSheetItem.rowIndex + i + 1,
								COLUMNARRAY[this.optionAColumnIndexOfSheetItem
										+ option.index.toLowerCase()
												.toCharArray()[0] - 'a'])));
				optionList.add(quizItemOption);
			}
			item.setOptions(optionList);
			item.setOptionsJson(Json.toJson(optionList));
			this.quizItems.add(item);
		}
		return i;
	}

	/**
	 * 处理第一个表单，读取具体的题目，写入quizitem列表
	 */
	private int processSheetItem() {

		// 下面三行有依赖关系，必须先确定Option，再确定题目个数，再确定维度个数
		this.optionNum = initOptions();
		if (this.optionNum == -1) {
			logger.error("解析题目表单失败：无效的选项信息.");
			return -1;
		}
		this.itemNum = initItems();
		if (this.itemNum == -1) {
			logger.error("解析题目表单失败：无效的题目信息.");
			return -1;
		}
		this.categoryNum = initCategories();
		if (this.categoryNum == -1) {
			logger.error("解析题目表单失败：无效的维度信息.");
			return -1;
		}

		logger.info("处理題目表单成功: 题目个数=" + this.itemNum + ", 选项个数="
				+ this.optionNum + ", 维度个数=" + this.categoryNum);
		return 0;
	}

	private int getCategoryIdByCategoryName(String name) {
		int id = -1;
		for (Iterator<Category> it = categories.iterator(); it.hasNext();) {
			Category category = it.next();
			if (category.name.equals(name))
				return category.id;
		}
		return id;
	}

	/**
	 * 处理第一个表单，解析个人评语列表
	 * 
	 * @return 返回个人评语表单的评语列表，返回null说明出错
	 */
	private LinkedList<QuizEvaluation> processSheetSingle() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();
		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex + i + 1,
					this.initPostionSheetSingle.columnIndex);

			if (value.equals("")) { // 到达最后一行，跳出循环
				break;
			}
			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setType("single");
			if (getCategoryIdByCategoryName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析个人评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryName(value));
			evaluation.setCategoryName(value);

			evaluation.setId(i); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置

			// 获取“分数区间”
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex + i + 1,
					this.scoreColumnIndexOfSheetSingle);
			if (value.equals("") || value.matches("[0-9]+-[0-9]+") == false) {
				logger.error("解析个人评估表单出错：第"
						+ this.initPostionSheetSingle.rowIndex + i + 1
						+ "行缺少分数区间");
				return null;
			}
			int min = Integer.parseInt(value.split("-")[0]);
			int max = Integer.parseInt(value.split("-")[1]);
			if (min > max) {
				logger.error("解析个人评估表单出错：第"
						+ this.initPostionSheetSingle.rowIndex + i + 1
						+ "行分数区间格式错误");
				return null;
			}
			evaluation.setMinScore(min);
			evaluation.setMaxScore(max);

			// 获取“健康状态”
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex + i + 1,
					this.healthColumnIndexOfSheetSingle);
			evaluation.setHealthStatus(value);

			// 获取“结果描述”
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex + i + 1,
					this.evaluationColumnIndexOfSheetSingle);
			evaluation.setEvaluation(value);

			// 获取“建议”
			value = getCellValue(this.initPostionSheetSingle.sheetIndex,
					this.initPostionSheetSingle.rowIndex + i + 1,
					this.suggestionColumnIndexOfSheetSingle);
			evaluation.setSuggestion(value);
			evaluations.add(evaluation);
		}

		logger.info("解析个人评语表单结束.");

		return evaluations;
	}

	/**
	 * 处理第一个表单，解析个人评语列表
	 * 
	 * @return
	 * 
	 * @return 返回团体评语表单的评语列表
	 */
	private LinkedList<QuizEvaluation> processSheetTeam() {

		LinkedList<QuizEvaluation> evaluations = new LinkedList<QuizEvaluation>();

		String value;

		for (int i = 0;; i++) {
			// 获取“维度”
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex + i + 1,
					this.initPostionSheetTeam.columnIndex);

			if (value.equals("")) { // 到达最后一行，跳出循环
				break;
			}
			QuizEvaluation evaluation = new QuizEvaluation();
			evaluation.setType("Team");
			if (getCategoryIdByCategoryName(value) < 0) {
				// 出错，该行维度不在第一页的维度列表中
				logger.error("解析团体评估表单出错：维度<" + value + ">未在题目表单中出现过.");
				return null;
			}
			evaluation.setCategoryId(getCategoryIdByCategoryName(value));
			evaluation.setCategoryName(value);

			evaluation.setId(i); // 设置ID，但是写入数据库时用不上，会重新设置这个ID
			evaluation.setQuizId(0); // 同上，不需要在这里设置

			// 获取“分数区间”
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex + i + 1,
					this.scoreColumnIndexOfSheetTeam);
			if (value.matches("[0-9]+-[0-9]+") == true) {
				int min = Integer.parseInt(value.split("-")[0]);
				int max = Integer.parseInt(value.split("-")[1]);
				if (min > max) {
					logger.error("解析团体评估表单出错：第"
							+ this.initPostionSheetTeam.rowIndex + i + 1
							+ "行分数区间格式错误");
					return null;
				}
				evaluation.setMinScore(min);
				evaluation.setMaxScore(max);
			} else {
				evaluation.setMinScore(0);
				evaluation.setMaxScore(999);
			}

			// 获取“健康状态”
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex + i + 1,
					this.healthColumnIndexOfSheetTeam);
			evaluation.setHealthStatus(value);

			// 获取“结果描述”
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex + i + 1,
					this.evaluationColumnIndexOfSheetTeam);
			evaluation.setEvaluation(value);

			// 获取“建议”
			value = getCellValue(this.initPostionSheetTeam.sheetIndex,
					this.initPostionSheetTeam.rowIndex + i + 1,
					this.suggestionColumnIndexOfSheetTeam);
			evaluation.setSuggestion(value);
			evaluations.add(evaluation);
		}

		logger.info("解析团体评语表单结束.");

		return evaluations;
	}

	/**
	 * 主处理流程
	 */
	public int process() {
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

		// Step2 : 处理题目表单，获取选项个数、维度个数、题目列表
		if (processSheetItem() == -1) {
			logger.error("导入问卷失败：未能成功解析题目表单");
			return -1;
		}

		LinkedList<QuizEvaluation> evaluationsSingle = processSheetSingle();
		if (evaluationsSingle == null) {
			logger.error("导入问卷失败：未能成功解析个人评估表单");
			return -1;
		}
		LinkedList<QuizEvaluation> evaluationsTeam = processSheetTeam();
		if (evaluationsTeam == null) {
			logger.error("导入问卷失败：未能成功解析团体评估表单");
			return -1;
		}

		for (Iterator<QuizEvaluation> it = evaluationsSingle.iterator(); it
				.hasNext();)
			this.quizEvaluations.add(it.next());
		for (Iterator<QuizEvaluation> it = evaluationsTeam.iterator(); it
				.hasNext();)
			this.quizEvaluations.add(it.next());
		logger.info("导入问卷成功: 导入题目" + this.quizItems.size() + "个；导入个人报告评语"
				+ evaluationsSingle.size() + "条；导入团体评语"
				+ evaluationsTeam.size() + "条；导入评语总数" + quizEvaluations.size()
				+ "条");
		return 0;

	}

	public static void main(String[] args) throws Exception {

		// QuizItemOption quizItemOption = new QuizItemOption();

		// String s = "13-100";
		// System.out.println(s.matches("[0-9]+-[0-9]+"));

		QuizImportHandler quizImportHandler = new QuizImportHandler(
				"D:\\quiz.xls");
		quizImportHandler.process();
		// quizImportHandler.initSheetPositions
		// System.out.println(quizImportHandler.getOptionNum());
		// System.out.println(quizImportHandler.getCategoryNum());
		return;

	}
}
