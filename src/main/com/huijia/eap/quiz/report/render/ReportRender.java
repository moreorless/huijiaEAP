package com.huijia.eap.quiz.report.render;

import java.io.File;
import java.io.IOException;


public interface ReportRender {
	
	void render(String dest, File tpFile) throws IOException;
	
}
