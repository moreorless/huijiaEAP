package com.huijia.eap.quiz.module;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.service.QuizService;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/quiz")
public class QuizModule {
	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT ="edit";
	private final static String OPERATION_READ ="read";
	
	@Inject
	private QuizService quizService;
	
	@At
	@Ok("jsp:jsp.quiz.list")
	public Pager<Quiz> list(HttpServletRequest request, @Param("..") Pager<Quiz> pager){
		return quizService.paging(null, pager);
	}
	
	@At
	@Ok("jsp:jsp.quiz.showtest")
	public void showtest(HttpServletRequest request){
		List<Quiz> list = quizService.fetchAll();
		request.setAttribute("quizlist", list);
	}

	@At
	@Ok("jsp:jsp.quiz.edit")
	public void prepare(HttpServletRequest request, @Param("id") long id, 
			@Param("operation") String operation) {
		Quiz quiz = new Quiz();
		if(OPERATION_EDIT.equals(operation)){
			quiz = quizService.fetch(id);
		}
		request.setAttribute("quiz", quiz);
	}
	
	@At
	@Ok("forward:/quiz/viewquiz?id=${obj.id}")
	@Chain("validate")
	@AdaptBy(type = UploadAdaptor.class)
	public Quiz add(@Param("..") Quiz quiz, @Param("quiz_file") File quizFile){
		
		//excel operation test
		quizService.excelTest();
		
		return quizService.insert(quiz);
	}
	
	
	
	@At
	@Ok("forward:/quiz/list")
	@Chain("validate")
	public void edit(@Param("..") Quiz quiz){
		quizService.update(quiz);
	}
	
	@At
	@Ok("forward:/quiz/list")
	public void delete(@Param("id") long id){
		quizService.delete(id);
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
