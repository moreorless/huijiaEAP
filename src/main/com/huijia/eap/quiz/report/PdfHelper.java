package com.huijia.eap.quiz.report;

import java.io.File;
import java.io.IOException;

import org.nutz.lang.Lang;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfHelper {

	private PdfFonts fonts;

	public PdfHelper() throws ReportRenderException {
		try {
			fonts = new PdfFonts();
		}
		catch (Exception e) {
			throw Lang.wrapThrow(e, ReportRenderException.class);
		}
	}

	public Section section(int num, String title, int level) {
		Paragraph p = new Paragraph(title, fonts.getHeadingFont(level));
		p.setSpacingBefore(10);
		p.setSpacingAfter(10);
		return new Chapter(p, num);
	}

	public Section addSection(Section section, String title, int level, Anchor anchor) {
		Paragraph paragraph = null;
		if (anchor != null) {
			paragraph = new Paragraph("", fonts.getHeadingFont(level));
			paragraph.add(anchor);
		} else {
			paragraph = new Paragraph(title, fonts.getHeadingFont(level));
		}
		return section.addSection(paragraph);
	}

	public ListItem LI() {
		return new ListItem();
	}

	public List UL() {
		List ul = new List(false, 20);
		ul.setIndentationLeft(20);
		return ul;
	}

	public List OL() {
		List ul = new List(true, 20);
		ul.setIndentationLeft(20);
		return ul;
	}

	public Paragraph p() {
		return new Paragraph();
	}

	public Paragraph blank() {
		Paragraph p = p();
		p.add(new Chunk(" ", fonts.getNormalFont()));
		return p;
	}

	public Paragraph normal() {
		Paragraph p = new Paragraph();
		p.setIndentationLeft(16);
		p.setSpacingBefore(8);
		p.setSpacingAfter(8);
		return p;
	}

	

	public PdfPTable table(int columnCount) {
		try {
			PdfPTable table = new PdfPTable(columnCount);
			return table;
		}
		catch (Exception e) {
			throw Lang.wrapThrow(e);
		}
	}

	public PdfPCell cell() {
		PdfPCell cell = new PdfPCell();
		cell.setBorderWidth(1);
		cell.setBorderColor(new BaseColor(200, 200, 200));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		return cell;
	}

	public Chunk chunk(String text, Font font) {
		return new Chunk(text, font);
	}

	public Paragraph codeLine(String text) {
		Paragraph p = p();
		p.add(new Chunk(text, fonts.getCodeFont()));
		return p;
	}
	
	public Anchor anchor(String text, String href) {
		Anchor anchor = new Anchor(text, fonts.getAnchorFount());
		anchor.setReference(href);
		return anchor;
	}

	public String toX(File file) {
		try {
			int code = file.getCanonicalPath().toString().hashCode();
			if (code < 0)
				return "NUTZa"+ (code * -1);
			return "NUTZ"+code;
		}
		catch (IOException e) {
			e.printStackTrace();
			return ""+file.getAbsolutePath().hashCode();
		}
	}
	

	public Font font() {
		return new Font(fonts.getNormalFont());
	}

}
