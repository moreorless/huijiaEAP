package com.huijia.eap.quiz.report.render;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;


public interface ReportRender {
	
	void render(String dest, File tpFile) throws IOException;
	
}
