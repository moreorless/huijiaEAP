package com.huijia.eap.util.excel;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Excel 表格 操作类
 * 
 * @author jianglei
 * 
 */
public class ExcelParser {

	private String filepath;

	private HSSFWorkbook wb;

	/**
	 * 判断是否初始化成功
	 */
	private boolean initFlag = false;

	public boolean initSuccess() {
		return this.initFlag;
	}

	/**
	 * 批量提交的队列
	 */
	// private Queue<WritableCell> commitQueue = new LinkedList<>();

	public ExcelParser(String path) {
		this.filepath = path;

		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		try {

			fis = new FileInputStream(this.filepath);
			fs = new POIFSFileSystem(fis); // 利用poi读取excel文件流

			wb = new HSSFWorkbook(fs); // 读取excel工作簿
			this.initFlag = true;
		} catch (Exception e) {
			this.initFlag = false;
			// logger.error("读取excel文件出错, file = " + filepath, e);
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		} finally {
		}

	}

	/**
	 * @param sheetIndex
	 *            (表单)
	 * @param columnIndex
	 *            （栏）
	 * @param rowIndex
	 *            (行)
	 * @return value （单元格内容） example： input: rowIndex = 0, columnIndex =
	 *         "A"("a") output: cell
	 * @throws IOException
	 * 
	 */
	public HSSFCell getCell(int sheetIndex, int rowIndex, String columnIndex)
			throws IOException {

		HSSFSheet sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个、1表示第二个.....

		HSSFRow row = sheet.getRow(rowIndex - 1); // 取出sheet中的某一行数据

		if (row == null) {
			return null;
		}
		HSSFCell cell = row
				.getCell(columnIndex.toLowerCase().toCharArray()[0] - 'a'); // 获取该行中的一个单元格对象

		return cell;
	}

	public String getCellStringValue(int sheetIndex, int rowIndex,
			String columnIndex) throws IOException {

		String value = "";

		HSSFSheet sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个、1表示第二个.....

		HSSFRow row = sheet.getRow(rowIndex - 1); // 取出sheet中的某一行数据

		if (row != null) {
			HSSFCell cell = row
					.getCell(columnIndex.toLowerCase().toCharArray()[0] - 'a'); // 获取该行中的一个单元格对象
			if (cell == null) {
				return "";
			}

			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = Double.toString(cell.getNumericCellValue())
						.split("\\.")[0];// 一般的数据类型在excel中读出来都为float型
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = cell.getStringCellValue();
				break;
			default:
				value = cell.getStringCellValue();
			}
		}
		return value;
	}

	/**
	 * @param sheetIndex
	 *            (表单)
	 * @param columnIndex
	 *            （栏）
	 * @param rowIndex
	 *            (行)
	 * @return value （单元格内容） example： input: rowIndex = 0, columnIndex =
	 *         "A"("a") output: value = 0 (in excel)
	 * @throws IOException
	 * 
	 */
	public double getCellDoubleValue(int sheetIndex, int rowIndex,
			String columnIndex) throws IOException {

		double value = 0;

		HSSFSheet sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个、1表示第二个.....

		HSSFRow row = sheet.getRow(rowIndex - 1); // 取出sheet中的某一行数据

		if (row != null) {
			HSSFCell cell = row
					.getCell(columnIndex.toLowerCase().toCharArray()[0] - 'a'); // 获取该行中的一个单元格对象
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue();// 一般的数据类型在excel中读出来都为float型
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = 0;
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = 0;
				break;
			default:
				value = 0;
			}
		}

		return value;
	}

	public static void main(String[] args) {
		try {
			ExcelParser excelReader = new ExcelParser("d://test.xls");

			double value = excelReader.getCellDoubleValue(0, 3, "d");
			System.out.println("\tBefore modify:\t D3="
					+ excelReader.getCellStringValue(0, 3, "d"));
			System.out.println("\tBefore modify:\t D4="
					+ excelReader.getCellStringValue(0, 4, "d"));
			System.out.println("\tBefore modify:\t G6="
					+ excelReader.getCellStringValue(0, 6, "g"));

			// excelReader.setCellValue(0, 3, "d", 200.3);
			// excelReader.setCellValue(0, 4, "d", 10001.2);

			// excelReader.updateBatch();

			value = excelReader.getCellDoubleValue(0, 3, "d");

			System.out.println("\tAfter modify:\t" + value);
			System.out.println("\tAfter modify:\t D4="
					+ excelReader.getCellStringValue(0, 4, "d"));
			System.out.println("\tAfter modify:\t G6="
					+ excelReader.getCellStringValue(0, 6, "g"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
