package com.huijia.eap.quiz.report.provider;

import java.util.Calendar;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizEvaluationService;


/**
 * 数据变量提供器
 * @author leon
 *
 */
@IocBean
public class DataProvider {
	@Inject
	private QuizEvaluationService quizEvaluationService;
	
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
		case "category.maxscore.name":
			return getCategoryName(result);
		case "category.maxscore.score":
			return getCategoryScore(result);
		case "category.feature":
			return getCategoryFeature(result);
		case "category.evaluation":
			return getCategoryEvaluation(result);
		case "category.suggestion":
			return getCategorySuggestion(result);
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
	/**
	 * 获取主维度名称
	 * @param result
	 * @return
	 */
	private String getCategoryName(QuizResult result){
		return result.getCategoryName();
	}
	/**
	 * 获取主维度得分
	 * @param result
	 * @return
	 */
	private String getCategoryScore(QuizResult result){
		int score = result.getScoreMap().get(String.valueOf(result.getCategoryId()));
		return String.valueOf(score);
	}
	private String getCategoryFeature(QuizResult result){
		QuizEvaluation eva = getEvaluation(result);
		if(eva == null) return "NaN";
		return eva.getFeature();
	}
	private String getCategoryEvaluation(QuizResult result){
		QuizEvaluation eva = getEvaluation(result);
		if(eva == null) return "NaN";
		return eva.getEvaluation();
	}
	private String getCategorySuggestion(QuizResult result){
		QuizEvaluation eva = getEvaluation(result);
		if(eva == null) return "NaN";
		return eva.getSuggestion();
	}
	private QuizEvaluation getEvaluation(QuizResult result){
		QuizEvaluation eva = quizEvaluationService.fetch(Cnd.where("quizid", "=", result.getQuizId())
				.and("categoryid", "=", result.getCategoryId()));
		return eva;
	}
}
