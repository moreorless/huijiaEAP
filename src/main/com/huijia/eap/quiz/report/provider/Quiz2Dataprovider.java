package com.huijia.eap.quiz.report.provider;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizEvaluationService;

/**
 * 沟通风格与冲突处理试卷的数据提供
 * @author leon
 *
 */
@IocBean
public class Quiz2Dataprovider extends CommonDataProvider {
	
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
				return super.getData(variableParam, quiz, user, result);
		}
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
