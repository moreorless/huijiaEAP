package com.huijia.eap.quiz.module;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.service.QuizService;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/quiz")
public class QuizModule {
	
	@Inject
	private QuizService quizService;
	
	@At
	@Ok("jsp:jsp.quiz.admin")
	public void admin(){
		
	}
	
	@At
	@Ok("jsp:jsp.quiz.list")
	public void list(HttpServletRequest request){
		Quiz quiz = quizService.genSampleQuiz();
		
		List<Quiz> list = new LinkedList<Quiz>();
		list.add(quiz);
		list.add(quiz);
		list.add(quiz);
		list.add(quiz);
		request.setAttribute("quizlist", list);
	}
	
	@At
	@Ok("jsp:jsp.quiz.test")
	public void test(HttpServletRequest request, @Param("id") long id){
		Quiz quiz = quizService.genSampleQuiz();
		request.setAttribute("quiz", quiz);
	}
	
	@At
	@Ok("jsp:jsp.quiz.report")
	public void report(){
		
	}
	
}
