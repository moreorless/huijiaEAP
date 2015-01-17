package com.huijia.eap.quiz.QuizImportService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.util.excel.ExcelParser;

public class QuizImportHandler {

	// private Logger logger = Logger.getLogger(this.getClass());
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
		public String index;
		public String name;
		public int value;
	}

	class Category {
		public int id;
		public String name;
	}

	private LinkedList<Option> options = new LinkedList<Option>();
	private LinkedList<Category> categories = new LinkedList<Category>();
	private LinkedList<QuizItem> quizItems = new LinkedList<QuizItem>();
	private LinkedList<QuizEvaluation> quizEvaluations = new LinkedList<QuizEvaluation>();

	// Sheet0（题目列表）的初始元素存放位置：“题目编号”的位置
	private CellPosition initPostionSheetItem = new CellPosition();
	// Sheet1（个人报告评语）的初始元素存放位置：“类别”的位置
	private CellPosition initPostionSheetSingle = new CellPosition();
	// Sheet2（团体报告评语）的初始元素存放位置：“类别”的位置
	private CellPosition initPostionSheetTeam = new CellPosition();

	private int itemNum = 0; // 题目个数
	private int optionNum = 0; // 选项个数
	private int cateryNum = 0;// 维度个数

	private String lieIndex; // 测谎题所在列号
	private String categoryIndex; // 维度所在列号

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
		return;
	}

	private int getItemNum() {

		int i = 0;
		String value;
		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i + 1,
					initPostionSheetItem.columnIndex);
			if (i == ITEMMAX)// 循环一直没有停止，出错
				return 0;
			if (value.equals(""))
				break;
		}
		return i;
	}

	private int getOptionNum() {
		int i = 0;
		String columnChar = this.initPostionSheetItem.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;
		int num = 0;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX)// 循环一直没有停止，出错
				return 0;

			if (num > 0 && value.equals("") == false
					&& value.equals("A") == false) {
				num++;
			}
			if (value.equals("A")) {
				num = 1;
			}
			if (value.equals(""))
				break;
		}
		return num;
	}

	private int getCategoryNum() {
		int i = 0;
		String columnChar = this.initPostionSheetItem.columnIndex;
		int columnIndex = columnChar.toLowerCase().toCharArray()[0] - 'a';

		String value;

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX)// 循环一直没有停止，出错
				return 0;
			if (value.equals("是否测谎题")) {
				lieIndex = COLUMNARRAY[columnIndex + i];
				break;
			}
			if (value.equals("")) // 没有找到测谎列
				return 0;
		}

		for (i = 0;; i++) {
			value = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex, COLUMNARRAY[columnIndex
							+ i]);
			if (i == ITEMMAX)// 循环一直没有停止，出错
				return 0;
			if (value.equals("维度")) {
				categoryIndex = COLUMNARRAY[columnIndex + i];
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
					this.initPostionSheetItem.rowIndex + i+1, lieIndex);
			categoryValue = getCellValue(this.initPostionSheetItem.sheetIndex,
					this.initPostionSheetItem.rowIndex + i+1, categoryIndex);

			if (lieValue.equals("是")) {
				continue;
			} else if (lieValue.equals("否")) {
				if (categoryValue.equals(""))
					return 0; // 不是测谎提，但是没有存放维度名称，错误
				if (categorySet.contains(categoryValue))
					continue;
				categorySet.add(categoryValue);
			} else
				return 0; // 出错
		}

		for (Iterator<String> it = categorySet.iterator(); it.hasNext();) {
			System.out.println(it.next());
		}

		return categorySet.size();
	}

	/**
	 * 初始化三个表单的定位元素存放位置
	 */
	public void initSheetPositions() {
		this.initSheet0Position();
		this.initSheet1Position();
		this.initSheet2Position();
		this.itemNum = getItemNum();
		this.optionNum = getOptionNum();
	}

	private String getCellValue(int sheetIndex, int rowIndex, String columnIndex) {

		try {
			return excel.getCellStringValue(sheetIndex, rowIndex, columnIndex);
		} catch (IOException e) {
			System.out.println("Error: cannot Get Excel Cell of (" + sheetIndex
					+ ", " + rowIndex + ", " + columnIndex + ")");
			return null;
		}
	}

	private String getCellValue(CellPosition position) {

		try {
			return excel.getCellStringValue(position.sheetIndex,
					position.rowIndex, position.columnIndex);
		} catch (IOException e) {
			System.out.println("Error: cannot Get Excel Cell of ("
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

	public static void main(String[] args) throws Exception {

		// QuizItemOption quizItemOption = new QuizItemOption();
		QuizImportHandler quizImportHandler = new QuizImportHandler(
				"D:\\quiz.xls");
		quizImportHandler.initSheetPositions();
		System.out.println(quizImportHandler.getOptionNum());
		System.out.println(quizImportHandler.getCategoryNum());
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
