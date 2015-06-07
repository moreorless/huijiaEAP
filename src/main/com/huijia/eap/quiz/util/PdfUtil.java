package com.huijia.eap.quiz.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;

public class PdfUtil {
	private static Logger logger = Logger.getLogger(PdfUtil.class); 
	/**
	 * 访问url，生成pdf文件
	 * @param url
	 * @return
	 */
	/**
	 * @param url
	 * @return
	 */
	public static File renderPdf(String url, String filePath){
		String webdir = GlobalConfig.getContextValueAs(String.class, "web.dir");
		String phantomjsPath = webdir + File.separator + "tools";
		String phantomjs = phantomjsPath + File.separator + "phantomjs.exe";
		String rasterizejs = phantomjsPath + File.separator + "rasterize.js";
		
		
		// command : phantomjs rasterize.js 'url' xxx.pdf
		String cmd = "cmd /c " + phantomjs + " " + rasterizejs + " \"" + url + "\" " + filePath + " A4";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		File file = new File(filePath);
		if(file.exists()){
			logger.info("render pdf file. url = " + url + ", pdf = " + filePath);
			return file;
		}
		
		logger.error("render pdf file failed. url = " + url);
		return null;
	}
	
	public static void main(String[] args){
		GlobalConfig.addContextValue("web.dir", "E:\\dev\\workspace\\git_repositories\\huijiaEAP\\WebContent");
		String url = "http://www.baidu.com";
		String filePath = "test.pdf";
		File f = PdfUtil.renderPdf(url, filePath);
		if(f == null) {
			System.out.println("render file failed");
		}else{
			System.out.println(f.getAbsolutePath());
		}
	}
}
