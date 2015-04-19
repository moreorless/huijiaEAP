package com.huijia.eap.quiz.report.provider;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizService;


/**
 * 数据变量提供器
 * @author leon
 *
 */
@IocBean
public class DataProvider {
	@Inject
	private QuizService quizService;
	@Inject
	private UserService userService;
	
	public String getData(String variableName, QuizResult result){
		Quiz quiz = quizService.fetch(result.getQuizId());
		User user = userService.fetch(result.getUserId());
		
		switch (variableName) {
		case "quiz.name":
			return quiz.getName();
		case "user.name":
			return user.getRealname();
		case "user.gender":
			if(user.getGender() == 0) return "女";
			return "男";
		case "user.age":
			return String.valueOf(user.getAge());
		case "test.date":
			return getTestDate(result);
			
		default:
			break;
		}
		return "NaN";
	}
	// 测试日期
	private String getTestDate(QuizResult result){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(result.getTimestamp());
		return calendar.get(Calendar.YEAR) + "年" + 
			(calendar.get(Calendar.MONTH) + 1) + "月" +
			calendar.get(Calendar.DAY_OF_MONTH) + "日";
	}
	
}
