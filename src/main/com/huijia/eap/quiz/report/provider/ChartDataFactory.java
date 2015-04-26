package com.huijia.eap.quiz.report.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizService;

@IocBean
public class ChartDataFactory {

	private static ChartDataFactory _instance = new ChartDataFactory();
	
	
	@Inject
	private QuizService quizService;
	
	private ChartDataFactory(){}
	
	public static ChartDataFactory me(){
		return _instance;
	}
	
	/**
	 * 返回options文件路径
	 * @param dataprovider
	 * @param quizKey
	 * @return
	 */
	public String getChartOptions(String dataprovider, QuizResult result){
//		return GlobalConfig.getContextValueAs(String.class, "web.dir") + File.separator
//				+ "tools" + File.separator + "echarts" + File.separator + "option";
		switch (dataprovider) {
		case "stat_by_category":
			return statByCategory(result);

		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * 按维度统计，各维度得分
	 * @param quizKey
	 * @return
	 */
	public String statByCategory(QuizResult result){
		long userId = result.getUserId();
		long quizId = result.getQuizId();
		String urlpath = GlobalConfig.getContextValueAs(String.class, "app.root.url") + "/quiz/report/chart/statByCategory"
				+ "?userId=" + userId + "&quizId=" + quizId;
		String saveFile = GlobalConfig.getContextValueAs(String.class, "web.dir") + File.separator
				+ "temp" + File.separator + userId + "_" + quizId + ".option";
		try {
			Files.createNewFile(new File(saveFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		URL url;
		try {
			url = new URL(urlpath);
		
			URLConnection conn = url.openConnection();  
			InputStream inStream = conn.getInputStream();  
			FileOutputStream fs = new FileOutputStream(saveFile);  
			
			int byteread = 0; 
			
			byte[] buffer = new byte[1204];  
			while ((byteread = inStream.read(buffer)) != -1) {  
				fs.write(buffer, 0, byteread);  
			}  
			fs.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveFile;
	}
}
