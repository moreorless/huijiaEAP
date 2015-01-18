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
		public String index; 	//ABCDE
		public String name;		//符合/不太符合...
		public String value;	// 5/10/15
	}

	class Category {
		public int id;			//维度编号，从1开始
		public String name;		//维度名称
	}
	
	
	//需要返回的数据
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
	
	// Sheet1（个人报告评语）的初始元素存放位置：“类别”的位置
	private CellPosition initPostionSheetSingle = new CellPosition();
	
	// Sheet2（团体报告评语）的初始元素存放位置：“类别”的位置
	private CellPosition initPostionSheetTeam = new CellPosition();

	/**
	 * // 初始化表单0的定位元素
	 */
	private void initSheet0Position() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORITEM;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];
				System.out.println(this.getCellValue(position) + ": "
						+ position.sheetIndex + ", " + position.rowIndex + ", "
						+ position.columnIndex);
				if (this.getCellValue(position).equals("题目编号")) {
					this.initPostionSheetItem.sheetIndex = position.sheetIndex;
					this.initPostionSheetItem.rowIndex = position.rowIndex;
					this.initPostionSheetItem.columnIndex = position.columnIndex;
					return;
				}
			}
		}
		// 执行到这里，说明初始化表单0失败
		this.initPostionSheetItem = null;
		logger.error("错误：在题目表单中找不到<题目编号>单元格。");
		return;
	}

	/**
	 * // 初始化表单1的定位元素
	 */
	private void initSheet1Position() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORSINGLE;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];
				if (this.getCellValue(position).equals("类别")) {
					this.initPostionSheetSingle.sheetIndex = position.sheetIndex;
					this.initPostionSheetSingle.rowIndex = position.rowIndex;
					this.initPostionSheetSingle.columnIndex = position.columnIndex;
					return;
				}
			}
		}
		// 执行到这里，说明初始化表单1失败
		this.initPostionSheetSingle = null;
		logger.error("错误：在个人评估表单中找不到<类别>单元格。");
		return;
	}

	/**
	 * // 初始化表单2的定位元素
	 */
	private void initSheet2Position() {
		CellPosition position = new CellPosition();
		position.sheetIndex = SHEETINDEXFORTEAM;

		for (int i = 1; i < rowIndexForSearch; i++) {
			for (int j = 0; j < columnIndexForSearch.length; j++) {
				position.rowIndex = i;
				position.columnIndex = columnIndexForSearch[j];
				if (this.getCellValue(position).equals("类别")) {
					this.initPostionSheetTeam.sheetIndex = position.sheetIndex;
					this.initPostionSheetTeam.rowIndex = position.rowIndex;
					this.initPostionSheetTeam.columnIndex = position.columnIndex;
					return;
				}
			}
		}
		// 执行到这里，说明初始化表单1失败
		this.initPostionSheetTeam = null;
		logger.error("错误：在团体评估表单中找不到<类别>单元格。");
		return;
	}

	/**
	 * 
	 * @return 选项个数
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
				return 0;
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
				return 0;
			}
			if (value.equals("是否测谎题")) {
				lieColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到测谎列
				return 0;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<维度>单元格，循环超过最大次数。");
				return 0;
			}
			if (value.equals("维度")) {
				categoryColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return 0;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<题目>单元格，循环超过最大次数。");
				return 0;
			}
			if (value.equals("题目")) {
				questionColumnIndexOfSheetItem = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return 0;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX) {
				// 循环一直没有停止，出错
				logger.error("获取维度个数失败：表单0中找不到<A>单元格，循环超过最大次数。");
				return 0;
			}
			if (value.equals("A")) {
				optionAColumnIndexOfSheetItem = columnIndex + i;
				break;
			}
			if (value.equals("")) // 没有找到维度列
				return 0;
		}

		String lieValue;
		String categoryValue;
		HashSet<String> categorySet = new HashSet<String>();
		for (i = 0; i < this.itemNum; i++) {
			lieValue = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1, lieColumnIndexOfSheetItem);
			categoryValue = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1, categoryColumnIndexOfSheetItem);

			if (lieValue.equals("是")) {
				continue;
			} else if (lieValue.equals("否")) {
				if (categoryValue.equals("")) {
					logger.error("获取维度个数失败：格式错误，非测谎题但未标明维度。题目编号为: " + (i + 1));
					return 0; // 不是测谎提，但是没有存放维度名称，错误
				}
				if (categorySet.contains(categoryValue))
					continue;
				categorySet.add(categoryValue);
			} else {
				logger.error("获取维度个数失败：格式错误，不能判断是否测谎题。题目编号为: " + (i + 1));
				return 0; // 出错
			}
		}

		i = 0;
		for (Iterator<String> it = categorySet.iterator(); it.hasNext();) {
			Category category = new Category();
			category.id = categories.size() + 1;
			category.name = it.next();
			categories.add(category);
		}

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
				return 0;
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
					this.initPostionSheetItem.rowIndex + i + 1, this.lieColumnIndexOfSheetItem)
					.equals("是"))
				item.setLieFlag(true);
			else if (getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1, this.lieColumnIndexOfSheetItem)
					.equals("否"))
				item.setLieFlag(false);
			else {
				logger.error("表单0中初始化题目失败：无法判断题目" + i + 1 + "是否为测谎题。");
				return 0;
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
	 * 初始化三个表单的定位元素存放位置
	 */
	public void initSheetPositions() {
		this.initSheet0Position();
		logger.debug("成功获取表单0定位元素<题目编号>位置:("
				+ this.initPostionSheetItem.sheetIndex + ", "
				+ this.initPostionSheetItem.rowIndex + ", "
				+ this.initPostionSheetItem.columnIndex + ")");
		this.initSheet1Position();
		logger.debug("成功获取表单1定位元素<类别>位置:("
				+ this.initPostionSheetSingle.sheetIndex + ", "
				+ this.initPostionSheetSingle.rowIndex + ", "
				+ this.initPostionSheetSingle.columnIndex + ")");
		this.initSheet2Position();
		logger.debug("成功获取表单2定位元素<类别>位置:("
				+ this.initPostionSheetTeam.sheetIndex + ", "
				+ this.initPostionSheetTeam.rowIndex + ", "
				+ this.initPostionSheetTeam.columnIndex + ")");
		this.optionNum = initOptions();
		this.categoryNum = initCategories();
		this.itemNum = initItems(); // 必须先initCategory

		logger.debug("初始化表单成功: 题目个数=" + this.itemNum + ", 选项个数="
				+ this.optionNum + ", 维度个数=" + this.categoryNum);
	}

	private String getCellValue(int sheetIndex, int rowIndex, String columnIndex) {

		try {
			return excel.getCellStringValue(sheetIndex, rowIndex, columnIndex);
		} catch (IOException e) {
			logger.error("Cannot Get Excel Cell of (" + sheetIndex + ", "
					+ rowIndex + ", " + columnIndex + ")");
			return null;
		}
	}

	private String getCellValue(CellPosition position) {

		try {
			return excel.getCellStringValue(position.sheetIndex,
					position.rowIndex, position.columnIndex);
		} catch (IOException e) {
			logger.error("Error: cannot Get Excel Cell of ("
					+ position.sheetIndex + ", " + position.rowIndex + ", "
					+ position.columnIndex + ")");
			return null;
		}
	}

	/**
	 * 处理第一个表单，读取具体的题目，写入quizitem列表
	 */
	private void processSheetItem() {

		// 获取optionList

	}

	/**
	 * excel主处理流程
	 */
	public void processExcel() {
		initSheetPositions();
		processSheetItem();
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

	public static void main(String[] args) throws Exception {

		// QuizItemOption quizItemOption = new QuizItemOption();
		QuizImportHandler quizImportHandler = new QuizImportHandler(
				"D:\\quiz.xls");
		quizImportHandler.initSheetPositions();
		// quizImportHandler.initSheetPositions
		// System.out.println(quizImportHandler.getOptionNum());
		// System.out.println(quizImportHandler.getCategoryNum());
		return;

		// ExcelParser excelParser = new ExcelParser("D:\\quiz.xls");
		// String value = excelParser.getCellStringValue(0, 3, "D");
		// System.out.println(value);
		// System.out.println("\tBefore modify:\t D3="
		// + excelParser.getCellStringValue(0, 3, "d"));
		// System.out.println("\tBefore modify:\t D4="
		// + excelParser.getCellStringValue(0, 4, "d"));
		// System.out.println("\tBefore modify:\t G6="
		// + excelParser.getCellStringValue(0, 6, "g"));

	}
}
