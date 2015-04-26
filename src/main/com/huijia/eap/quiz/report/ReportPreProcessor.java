package com.huijia.eap.quiz.report;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.report.provider.DataProvider;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 报表模板预处理
 * @author leon
 *
 */
@IocBean
public class ReportPreProcessor {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Inject
	private DataProvider dataProvider;
	@Inject
	private QuizService quizService;
	
	public ReportPreProcessor(){
	}
	
	public File process(File reportTemplate, Quiz quiz, User user, List<QuizResult> resultList){
		if(!reportTemplate.exists()) return null;
		
		// 创建临时文件
		String fileName = GlobalConfig.getContextValueAs(String.class, "web.dir") + File.separator
				+ "temp" + File.separator + System.currentTimeMillis() + ".report";
		File tmpReport = new File(fileName);
		try {
			Files.createNewFile(tmpReport);
		} catch (IOException e1) {
			logger.error("Create File Error, File = " + fileName, e1);
			return null;
		}
		
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try{
			
			InputStreamReader isr = new InputStreamReader(new FileInputStream(reportTemplate));
			 reader = new BufferedReader(isr);
			
			writer = new BufferedWriter(new FileWriter(tmpReport));
			String line = null;
			try{
				while((line = reader.readLine()) != null){
					String newLine = parseLine(line, quiz, user, resultList);
					writer.write(newLine);
				}
				
				writer.flush();
			}catch(Exception e) {
				logger.error("preprocess error, line is " + line, e);
			}
		}catch (Exception e) {
			logger.error("preprocess error", e);
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return tmpReport;
	}
	
	/**
	 * 解析模板中的变量值
	 * @param line
	 * @param result
	 * @return
	 */
	public String parseLine(String line, Quiz quiz, User user, List<QuizResult> resultLists){
		
		int varIndex = line.indexOf("$parseVar(");
		int bufferStart = 0;
		StringBuffer buffer = new StringBuffer();
		while(varIndex != -1){
			buffer.append(line.substring(bufferStart, varIndex));
			
			int startAt = line.indexOf("(", varIndex) + 1;
			int endAt = line.indexOf(")", startAt);
			if(startAt == -1 || endAt == -1) {
				logger.error("报告变量解析错误, " + line);
				break;
			}
			
			String variableParam = line.substring(startAt, endAt);
			
			String varValue = "NaN";
			QuizResult quizResult = null;
			// 解析变量
			// 变量规则 $parseVar(varname, param1, param2 | quiztag)
			if(variableParam.indexOf("|") != -1){
				String tagName = variableParam.split("\\|")[1].trim();
				Quiz subQuiz = quizService.getQuizByTag(tagName);
				if(subQuiz == null){
					logger.error("报表模板解析错误 variable = " + variableParam);
				}else{
					for(QuizResult _result : resultLists){
						if(_result.getQuizId() == subQuiz.getId()){
							quizResult = _result;
							break;
						}
					}
					variableParam = variableParam.split("\\|")[0].trim();
					varValue = dataProvider.getData(variableParam, quiz, user, quizResult);
				}
			}else{
				quizResult = resultLists.get(0);
				varValue = dataProvider.getData(variableParam, quiz, user, quizResult);
			}
			
			buffer.append(varValue);
			
			bufferStart = endAt + 1;
			varIndex = line.indexOf("$parseVar(", endAt + 1);
		}
		buffer.append(line.substring(bufferStart));
		
		return buffer.toString();
	}
	
}
