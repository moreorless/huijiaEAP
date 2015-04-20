package com.huijia.eap.quiz.report.provider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;

/**
 * 图表数据提供器
 * @author leon
 *
 */
public class ChartProvider {
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 生成统计图，并返回统计图url
	 * @param dataprovider
	 * @param quizKey
	 * @return
	 */
	public String genChart(String dataProvider, String quizKey){
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateStr = df.format(new Date());
		
		String targetDir = GlobalConfig.getContextValueAs(String.class, "webinf.dir") + File.separator +
				"report" + File.separator + "charts" + File.separator + dateStr;
		Files.createDirIfNoExists(targetDir);
		String fileName = UUID.randomUUID().toString() + ".png";
		String chartImgPath = targetDir + File.separator + fileName;
		
		String options = getChartOptions(dataProvider, quizKey);
		
		String cmd = "cmd /c phantomjs.exe echarts\\echarts-convert.js -infile " + options + " -outfile " + chartImgPath;
		String contextPath = GlobalConfig.getContextValueAs(String.class, "web.dir") + File.separator + "tools";
		try {
			Process process = Runtime.getRuntime().exec(cmd, null, new File(contextPath));
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("生成统计图出错 dataProvider=" + dataProvider + ", quizKey=" + quizKey, e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("生成统计图出错 dataProvider=" + dataProvider + ", quizKey=" + quizKey, e);
		}
		
		return chartImgPath;
	}
	
	/**
	 * 返回options文件路径
	 * @param dataprovider
	 * @param quizKey
	 * @return
	 */
	public String getChartOptions(String dataprovider, String quizKey){
		return GlobalConfig.getContextValueAs(String.class, "web.dir") + File.separator
				+ "tools" + File.separator + "echarts" + File.separator + "option";
	}
	
}
