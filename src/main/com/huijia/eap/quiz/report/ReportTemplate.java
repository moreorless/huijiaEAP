package com.huijia.eap.quiz.report;

import java.io.File;


import org.apache.log4j.Logger;
import org.nutz.lang.Xmls;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 报表模板
 * @author leon
 *
 */
public class ReportTemplate {

	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 模板文件
	 */
	private File tpFile;
	
	public File getTpFile() {
		return tpFile;
	}

	private Element cover;
	private Element preface;
	private Element body;
	
	/**
	 * 数据提供者类
	 */
	private String dataProvider;

	private String title;
	
	public ReportTemplate(File tpFile){
		this.tpFile = tpFile;
		this.load();
	}
	
	
	public void load(){
		try {
			if(!this.tpFile.exists()) return;
			
			Document document = Xmls.xml(tpFile);
			Element reportEle = document.getDocumentElement();
			this.dataProvider = Xmls.getAttr(reportEle, "dataprovider");
			this.cover = Xmls.firstChild(reportEle, "cover");
			this.preface = Xmls.firstChild(reportEle, "preface");
			this.body = Xmls.firstChild(reportEle, "body");
			this.title = Xmls.get(cover, "title");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("解析报表模板错误：", e);
		}
	}


	public Element getCover() {
		return cover;
	}

	public Element getPreface() {
		return preface;
	}

	public Element getBody() {
		return body;
	}
	
	public String getTitle(){
		return title;
	}

	public String getDataProvider() {
		return dataProvider;
	}
	
	
}
