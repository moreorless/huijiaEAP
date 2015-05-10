package com.huijia.eap.quiz.report.provider;

import java.util.Calendar;

import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;


/**
 * 基础数据变量提供器
 * @author leon
 *
 */
@IocBean
public class CommonDataProvider implements DataProvider{
	
	/**
	 * 
	 * @param variableParam
	 * 		变量规则 $parseVar(varname, param1, param2 | quiztag)
	 * @param quiz
	 * @param user
	 * @param resultLists
	 * @return
	 */
	public String getData(String variableParam, Quiz quiz, User user, QuizResult result){
		
		if(result == null) return "NaN";
		
		switch (variableParam) {
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
