package com.huijia.eap.quiz.module;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
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
	
	/**
	 * 分页查看试卷列表
	 * @param request
	 * @param pager
	 * @return
	 */
	@At
	@Ok("jsp:jsp.quiz.list")
	public Pager<Quiz> list(HttpServletRequest request, @Param("..") Pager<Quiz> pager){
		return quizService.paging(null, pager);
	}
	
	/**
	 * 答题者看到的试卷列表
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.quizlist")
	public void showtest(HttpServletRequest request){
		List<Quiz> list = quizService.fetchAll();
		request.setAttribute("quizlist", list);
		
		User currentUser = Auths.getUser(request);
		request.setAttribute("user", currentUser);
	}

	/**
	 * 添加、编辑试卷的入口
	 * @param request
	 * @param id
	 * @param operation
	 */
	@At
	@Ok("jsp:jsp.quiz.edit")
	public void prepare(HttpServletRequest request, @Param("id") long id, 
			@Param("operation") String operation) {
		Quiz quiz = new Quiz();
		if(OPERATION_EDIT.equals(operation)){
			quiz = quizService.fetch(id);
		}
		request.setAttribute("quiz", quiz);
		
		// 从images/quiz/icons/目录加载可用的试卷图标
		String iconpath = GlobalConfig.getContextValue("web.dir") + File.separator + "images"
				+ File.separator + "quiz" + File.separator + "icons";
		File[] iconFiles = Files.files(new File(iconpath), null);
		List<String> iconNames = new ArrayList<String>();
		for(File f : iconFiles){
			iconNames.add(f.getName());
		}
		request.setAttribute("iconNames", iconNames);
	}
	
	/**
	 * 添加试卷
	 * @param request
	 * @param quiz
	 * @param tempFile
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public void add(HttpServletRequest request, @Param("..") Quiz quiz, @Param("quiz_file") TempFile tempFile){
		

		File quizFile = tempFile.getFile();
		try {
			Files.copyFile(quizFile, new File("D:\\" + quizFile.getName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		quiz = quizService.insert(quiz);
		request.setAttribute("quiz", quiz);
	}
	
	/**
	 * 添加试卷后，跳转到的试卷查看页面
	 * @param request
	 * @param id
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	public void viewquiz(HttpServletRequest request, @Param("id") long id){
		Quiz quiz = quizService.fetch(id);
		request.setAttribute("quiz", quiz);
	}
	

	/**
	 * 编辑试卷
	 * @param quiz
	 */

	@At
	@Ok("forward:/quiz/list")
	@Chain("validate")
	public void edit(@Param("..") Quiz quiz){
		quizService.update(quiz);
	}
	
	/**
	 * 删除试卷
	 * @param id
	 */
	@At
	@Ok("forward:/quiz/list")
	public void delete(@Param("id") long id){
		quizService.delete(id);
	}
	
	/**
	 * 答题测验页面
	 * @param request
	 * @param id
	 */
	@At
	@Ok("jsp:jsp.quiz.test.test")
	public void test(HttpServletRequest request, @Param("id") long id){
		Quiz quiz = quizService.genSampleQuiz();
		request.setAttribute("quiz", quiz);
	}

	/**
	 * 提交答案
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	public void answer(HttpServletRequest request){
		
	}
	
	/**
	 * 报表页面
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	public void report(){
		
	}
	
}
