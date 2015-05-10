package com.huijia.eap.quiz.report.provider;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;

public interface DataProvider {

	/**
	 * 
	 * @param variableParam
	 * 		变量规则 $parseVar(varname, param1, param2 | quiztag)
	 * @param quiz
	 * @param user
	 * @param resultLists
	 * @return
	 */
	public String getData(String variableParam, Quiz quiz, User user, QuizResult result);
}
