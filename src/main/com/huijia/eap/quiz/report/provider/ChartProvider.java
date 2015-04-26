package com.huijia.eap.quiz.report.provider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 图表数据提供器
 * @author leon
 *
 */
@IocBean
public class ChartProvider {
	private Logger logger = Logger.getLogger(this.getClass());

	@Inject
	QuizService quizService;
	
	/**
	 * 生成统计图，并返回统计图url
	 * @param dataprovider
	 * @param quizKey
	 * @return
	 */
	public String genChart(String dataProvider, String quizKey, Quiz quiz, User user, List<QuizResult> resultList){
		
		if(resultList == null || resultList.size() == 0){
			logger.error("生成图片错误, resultList为空");
			return GlobalConfig.getContextValueAs(String.class, "web.dir") + 
					File.separator + "images" + File.separator + "error_404.png";
		}
		
		QuizResult result = null;
		if(quizKey != null && quizKey.trim().length() > 0){
			Quiz subQuiz = quizService.getQuizByTag(quizKey);
			if(subQuiz == null){
				logger.error("生成图片错误, key not found : " + quizKey);
				return GlobalConfig.getContextValueAs(String.class, "web.dir") + 
						File.separator + "images" + File.separator + "error_404.png";
			}else{
				for(QuizResult _result : resultList){
					if(_result.getQuizId() == subQuiz.getId()){
						result = _result;
						break;		
					}
				}
			}
		}else{
			result = resultList.get(0);
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateStr = df.format(new Date());
		
		String targetDir = GlobalConfig.getContextValueAs(String.class, "webinf.dir") + File.separator +
				"report" + File.separator + "charts" + File.separator + dateStr;
		Files.createDirIfNoExists(targetDir);
		String fileName = UUID.randomUUID().toString() + ".png";
		String chartImgPath = targetDir + File.separator + fileName;
		
		String options = ChartDataFactory.me().getChartOptions(dataProvider, result);
		
		String cmd = "cmd /c phantomjs.exe echarts\\echarts-convert.js -infile " + options + " -outfile " + chartImgPath
				+ " -width 500 -height 400";
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
	

	
}
