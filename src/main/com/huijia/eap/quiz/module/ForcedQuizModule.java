package com.huijia.eap.quiz.module;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 迫选题答题处理
 * @author leon
 *
 */
@IocBean
@InjectName
@AuthBy(check = false)
@At("/quiz/forced")
public class ForcedQuizModule {

	@Inject
	private QuizService quizService;

	@Inject
	private QuizItemService quizItemService;
	
	@Inject
	private QuizCategoryService quizCategoryService;
	/**
	 * 提交答案(迫选题)
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.answerok")
	public void answer(HttpServletRequest request,
			@Param("quizId") long quizId, @Param("answerJson") String answerJson) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		User user = Auths.getUser(request);

		// 计算并存储答题结果
		Map<String, String> _answer = (Map<String, String>) Json
				.fromJson(answerJson);
		
		
		List<QuizCategory> categoryList = quizCategoryService.getCategoryList(quizId);
		
//		Map
//		Iterator<String> iter = _answer.keySet().iterator();
//		while(iter.hasNext()){
//			long categoryId = Long.parseLong(iter.next());
//			
//		}
		
	}
}
