package com.huijia.eap.quiz.report;

import java.io.File;
import java.io.IOException;


public interface ReportRender {
	
	void render(String dest, File tpFile) throws IOException;
	
}
