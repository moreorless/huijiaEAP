package com.huijia.eap.util;

import java.io.*;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;


/**
 * 读写XML文件的工具类
 * 
 * <pre>
 * XmlUtil util = new XmlUtil();
 * File file = new File("sample.xml");
 * util.read(file);
 * Element root = util.getRootElement();
 * </pre>
 * 
 * @author wenhua
 */
public class XmlUtil {
	private Logger logger=Logger.getLogger(this.getClass().getName());
	
	private Document document = null;
	private org.xml.sax.XMLFilter xmlFilter = null;
	private String xmlEncoding = "UTF-8";
	
	/**
	 * 设置写入XML文件时使用的编码格式，缺省为UTF-8
	 * @param encoding
	 */
	public void setXmlEncoding(String encoding) {
		xmlEncoding = encoding;
	}

	/**
	 * 写入XML文件时创建文档
	 * @param strRoot	根元素名称
	 */
	public void createDocument(String strRoot) {
		if (document != null)
			document = null;

		Element root = new Element(strRoot);
		document = new Document(root);
	}

	/**
	 * 获取XML文件根元素，如果还没有读取文件或没有创建文档，则返回null
	 * @return
	 */
	public Element getRootElement() {
		if (document != null) {
			return document.getRootElement();
		} else
			return null;
	}

	/**
	 * 设置根节点，必须首先读取XML文件或创建文档(createDocument)
	 * @param element
	 */
	public void setRootElement(Element element) {
		if (document != null) {
			document.setRootElement(element);
		}
	}
	
	/**
	 * 设置过滤器
	 * @param xmlFilter
	 */
	public void setXMLFilter(org.xml.sax.XMLFilter xmlFilter) {
		this.xmlFilter = xmlFilter;

	}

	/**
	 * 读取XML文件
	 * @param inputStream XML文件输入流
	 * @return
	 * @throws JDOMException
	 */
	public boolean read(InputStream inputStream) throws JDOMException {
		SAXBuilder builder = null;
		builder = new SAXBuilder();
		if (xmlFilter != null)
			builder.setXMLFilter(xmlFilter);

		builder.setExpandEntities(true);
		if (inputStream == null)
			return false;
		try {
			document = builder.build(inputStream);
		} catch (java.io.IOException e) {
			logger.info("Read xml error: ",e);
			xmlFilter = null;			
			return false;
		}

		return true;
	}

	/**
	 * 读取XML文件
	 * @param strFile XML文件路径
	 * @return
	 * @throws JDOMException
	 */
	public boolean read(String strFile) throws JDOMException {
		SAXBuilder builder = null;
		builder = new SAXBuilder();
		if (xmlFilter != null)
			builder.setXMLFilter(xmlFilter);
		builder.setExpandEntities(true);		
		if (document != null)
			document = null;

		try {
			document = builder.build(new File(strFile));
		} catch (java.io.IOException e) {
			logger.info("Read xml error: ",e);
			xmlFilter = null;
			return false;
		}

		return true;

	}

	/**
	 * 读取XML文件
	 * @param file XML文件对象
	 * @return
	 * @throws JDOMException
	 */
	public boolean read(File file) throws JDOMException {
		SAXBuilder builder = null;
		builder = new SAXBuilder();
		if (xmlFilter != null)
			builder.setXMLFilter(xmlFilter);
		builder.setExpandEntities(true);
		if (document != null)
			document = null;

		try {
			document = builder.build(file);
		} catch (java.io.IOException e) {
			logger.info("Read xml error: ",e);
			xmlFilter = null;
			return false;
		}

		return true;

	}
    
	/**
	 * 读取加密的XML文件
	 * @param file XML文件对象
	 * @param pass 密码
	 * @return
	 * @throws JDOMException
	 */
	public boolean readEncryptFile(File file, String pass) throws JDOMException
	{
		byte[] xmlstr=CryptoUtil.decryptFileToString(file,pass);
		if (xmlstr==null)
			return read(file);
		
		ByteArrayInputStream reader=new ByteArrayInputStream(xmlstr);
		return read(reader);
	}
	
	/**
	 * 读取XML文件
	 * @param reader XML文件字符流reader
	 * @return
	 * @throws JDOMException
	 */
	public boolean read(StringReader reader) throws JDOMException {
		SAXBuilder builder = null;
		builder = new SAXBuilder();
		if (xmlFilter != null)
			builder.setXMLFilter(xmlFilter);
		builder.setExpandEntities(true);
		if (document != null)
			document = null;

		try {
			document = builder.build(reader);
		} catch (java.io.IOException e) {
			logger.info("Read xml error: ",e);
			xmlFilter = null;
			reader.close();
			return false;
		}
		reader.close();
		return true;

	}

	/**
	 * 写入XML文件
	 * @param outputStream XML文件输出流
	 * @throws IOException
	 */
	public void write(OutputStream outputStream) throws IOException {
		XMLOutputter outputter = new XMLOutputter();
		Format outfort = Format.getPrettyFormat();
		outfort.setEncoding(xmlEncoding);
		outputter.setFormat(outfort);
		if (document != null)
			outputter.output(document, outputStream);
		
	}

	/**
	 * 写入XML文件
	 * @param strFile XML文件路径
	 * @throws IOException
	 */
	public void write(String strFile) throws IOException {
		XMLOutputter outputter = new XMLOutputter();
		Format outfort = Format.getPrettyFormat();
		outfort.setEncoding(xmlEncoding);
		outputter.setFormat(outfort);
		FileOutputStream fos = new FileOutputStream(strFile);
		outputter.output(document,fos );
		fos.close();
	}

	/**
	 * 写入XML文件
	 * @param file XML文件对象
	 * @throws IOException
	 */
	public void write(File file) throws IOException {
		XMLOutputter outputter = new XMLOutputter();
		Format outfort = Format.getPrettyFormat();
		outfort.setEncoding(xmlEncoding);
		outputter.setFormat(outfort);
		FileOutputStream fos = new FileOutputStream(file);
		outputter.output(document, fos);
		fos.close();
	}

	/**
	 * 写入XML文件
	 * @param writer XML文件字符流writer
	 * @throws IOException
	 */
	public void write(StringWriter writer) throws IOException {
		XMLOutputter outputter = new XMLOutputter();
		Format outfort = Format.getPrettyFormat();
		outfort.setEncoding(xmlEncoding);
		outputter.setFormat(outfort);
		outputter.output(document, writer);
		if (writer != null)
			writer.close();
	}

}
