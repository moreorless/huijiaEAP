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
	public static File renderPdf(String url){
		String webdir = GlobalConfig.getContextValueAs(String.class, "web.dir");
		String phantomjsPath = webdir + File.separator + "tools";
		String phantomjs = phantomjsPath + File.separator + "phantomjs.exe";
		String rasterizejs = phantomjsPath + File.separator + "rasterize.js";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(new Date());
		String storePath = webdir + File.separator + "download" + File.separator + "report" + File.separator + dateStr;
		Files.createDirIfNoExists(storePath);
		
		String pdfFile = storePath  + File.separator + System.currentTimeMillis() + ".pdf";
		
		// command : phantomjs rasterize.js 'url' xxx.pdf
		String cmd = "cmd /c " + phantomjs + " " + rasterizejs + " \"" + url + "\" " + pdfFile + " A4";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		File file = new File(pdfFile);
		if(file.exists()){
			logger.info("render pdf file. url = " + url + ", pdf = " + pdfFile);
			return file;
		}
		
		logger.error("render pdf file failed. url = " + url);
		return null;
	}
	
	public static void main(String[] args){
		GlobalConfig.addContextValue("web.dir", "E:\\dev\\workspace\\git_repositories\\huijiaEAP\\WebContent");
		String url = "http://www.baidu.com";
		File f = PdfUtil.renderPdf(url);
		if(f == null) {
			System.out.println("render file failed");
		}else{
			System.out.println(f.getAbsolutePath());
		}
	}
}
