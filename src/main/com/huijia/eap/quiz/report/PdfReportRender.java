package com.huijia.eap.quiz.report;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Xmls;
import org.nutz.lang.util.Disks;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfReportRender implements ReportRender{
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected PdfHelper helper = new PdfHelper();
	
	private BaseFont basefont;
	private Font chapterFont;

	public PdfReportRender() throws ReportRenderException, DocumentException, IOException{
		initFont();
	}
	
	
	private void initFont() throws DocumentException, IOException{
		basefont = BaseFont.createFont( "simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		chapterFont = new Font(basefont, 20, Font.BOLD);
	}
	
	@Override
	public void render(String dest, File tpFile) throws IOException {
		
		if(tpFile == null || !tpFile.exists()) return;
		ReportTemplate template = new ReportTemplate(tpFile);
		
		File pdfFile = new File(Disks.normalize(dest));
		if (!pdfFile.exists())
			Files.createNewFile(pdfFile);
		
		try {
			// 创建 PDF 文档
			Document doc = new Document();
			
			PdfWriter writer = PdfWriter.getInstance(doc, Streams.fileOut(dest));
			
			doc.open();
			doc.addCreator("北京会佳心语健康科技有限公司");
			doc.addAuthor("北京会佳心语健康科技有限公司");
			doc.addTitle(template.getTitle());
			
			// 创建封面
			renderCover(doc, template);
			doc.newPage();
			// 前言
			renderPreface(doc, template);
			doc.newPage();
			// 报告正文
			renderBody(doc, template);

			// 关闭 PDF 文档
			doc.close();
		}
		catch (DocumentException e) {
			throw Lang.wrapThrow(e, IOException.class);
		}
		
	}
	
	
	public void renderCover(Document doc, ReportTemplate template){
		List<org.w3c.dom.Element> children = Xmls.children(template.getCover());
		Iterator<org.w3c.dom.Element> iter = children.iterator();
		while(iter.hasNext()){
			org.w3c.dom.Element ele = iter.next();
			try {
				renderToPdf(doc, ele);
			} catch (DocumentException e) {
				logger.error("渲染pdf内容失败, " + ele.getTagName() + " : " + ele.getNodeValue(), e);
			}
		}
		
	}
	public void renderPreface(Document doc, ReportTemplate template){
		List<org.w3c.dom.Element> children = Xmls.children(template.getPreface());
		Iterator<org.w3c.dom.Element> iter = children.iterator();
		while(iter.hasNext()){
			org.w3c.dom.Element ele = iter.next();
			try {
				renderToPdf(doc, ele);
			} catch (DocumentException e) {
				logger.error("渲染pdf内容失败, " + ele.getTagName() + " : " + ele.getNodeValue(), e);
			}
		}
		
	}
	public void renderBody(Document doc, ReportTemplate template){
		List<org.w3c.dom.Element> children = Xmls.children(template.getBody());
		Iterator<org.w3c.dom.Element> iter = children.iterator();
		while(iter.hasNext()){
			org.w3c.dom.Element ele = iter.next();
			try {
				renderToPdf(doc, ele);
			} catch (DocumentException e) {
				logger.error("渲染pdf内容失败, " + ele.getTagName() + " : " + ele.getNodeValue(), e);
			}
		}
	}
	
	public void renderToPdf(Document doc, org.w3c.dom.Element element) throws DocumentException{
		String tagname = element.getTagName();
		Chunk chunk = null;
		
		switch (tagname) {
		case "title":
			String title = element.getFirstChild().getNodeValue();
			title = title.replace("\\n", "\n");

			chunk = new Chunk(title, chapterFont);
			Paragraph chapterP = new Paragraph(chunk);
			chapterP.setAlignment(Element.ALIGN_CENTER);
			doc.add(chapterP);
			break;
		case "p":
			renderParagraph(doc, element);
			break;
		case "h1":
			renderHeader(doc, element, 14);
			break;
		case "h2":
			renderHeader(doc, element, 13);
			break;
		case "h3":
			renderHeader(doc, element, 12);
			break;
		case "h4":
			renderHeader(doc, element, 11);
			break;
		case "br":
			doc.add(new Paragraph(Chunk.NEWLINE));
			break;
		default:
			break;
		}
	}


	private void renderParagraph(Document doc, org.w3c.dom.Element element)
			throws DocumentException {
		Chunk chunk;
		String content = element.getFirstChild().getNodeValue();
		
		Font paragraphFont = new Font(basefont, 12, Font.NORMAL);
		String fontsize = Xmls.getAttr(element, "fontsize");
		if(fontsize != null){
			paragraphFont.setSize(Float.parseFloat(fontsize));
		}
		
		chunk = new Chunk(content, paragraphFont);
		Paragraph p = new Paragraph(chunk);
		p.setFirstLineIndent(20);
		
		String leftindent = Xmls.getAttr(element, "leftindent");
		if(leftindent != null){
			p.setIndentationLeft(Float.parseFloat(leftindent));
		}
		
		doc.add(p);
		logger.debug("添加元素 p : " + content);
	}
	
	private void renderHeader(Document doc, org.w3c.dom.Element element, int fontSize) throws DocumentException{
		String content = element.getFirstChild().getNodeValue();
		Font headerFont = new Font(basefont, fontSize, Font.BOLD);
		Chunk chunk = new Chunk(content, headerFont);
		Paragraph chapterP = new Paragraph(chunk);
		
		String leftindent = Xmls.getAttr(element, "leftindent");
		if(leftindent != null){
			chapterP.setIndentationLeft(Float.parseFloat(leftindent));
		}
		doc.add(chapterP);

		// 暂时不作为章节处理，以后优化为章节，可以生成书签
//		String tagName = element.getTagName().toLowerCase();
//		int level = Integer.parseInt(tagName.substring(1));
//		Chapter chapter = new Chapter(chapterP, level);
//		chapter.setNumberDepth(level);
//		chapter.setBookmarkTitle(content);
//		doc.add(chapter);
		
		logger.debug("添加元素" + element.getTagName() + " : " + content);
	}
	
	public static void main(String[] args) throws IOException, ReportRenderException, DocumentException{
		String dest = "d:\\huijia.pdf";
		String reportTemplate = System.getProperty("user.dir") + File.separator
				+ "WebContent" + File.separator + "WEB-INF" + File.separator + "_conf"
				+ File.separator + "report" + File.separator + "person" + File.separator
				+ "communicate_conflict.report";
		
		PdfReportRender render = new PdfReportRender();
		render.render(dest, new File(reportTemplate));
	}

}
