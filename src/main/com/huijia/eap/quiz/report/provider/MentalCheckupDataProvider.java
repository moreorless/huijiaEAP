package com.huijia.eap.quiz.report.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizEvaluationService;

@IocBean
public class MentalCheckupDataProvider extends CommonDataProvider {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Inject
	private QuizEvaluationService quizEvaluationService;
	@Inject
	private QuizCategoryService quizCategoryService;
	
	/**
	 * 存放评价对象的缓存，提高效率
	 */
	private Map<String, QuizEvaluation> evaMap = new HashMap<>();
	
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
		
		// 遍历各维度获取值
		if(variableParam.startsWith("category")){
			return getCategoryData(variableParam, quiz, user, result);
		}
		
		switch (variableParam) {
			case "lie.score":
				return getLieScore(result);
			case "total.score":
				return getTotalScore(result);
			case "total.healthStatus":
				String status = "NaN";
				QuizEvaluation eva = getTotalQuizEvaluation(result);
				if(eva != null) status = eva.getHealthStatus();
				return status;
			case "total.evaluation":
				String evaluation = "NaN";
				eva = getTotalQuizEvaluation(result);
				if(eva != null) evaluation = eva.getEvaluation();
				return evaluation;
			case "total.suggestion":
				String suggestion = "NaN";
				eva = getTotalQuizEvaluation(result);
				if(eva != null) suggestion = eva.getSuggestion();
				return suggestion;
			default:
				return super.getData(variableParam, quiz, user, result);
		}
	}
	
	/**
	 * 获取维度的参数
	 * @param variableParam
	 * @param quiz
	 * @param user
	 * @param result
	 * @return
	 */
	private String getCategoryData(String variableParam, Quiz quiz, User user, QuizResult result){
		// 提取序号
		int pre = variableParam.indexOf("[");
		int next = variableParam.indexOf("]");
		int index = Integer.valueOf(variableParam.substring(pre + 1, next));
		long quizId = result.getQuizId();
		List<QuizCategory> categoryList = QuizCache.me().getCategoryList(quizId);
		if(index < 1 || index > categoryList.size()) return "NaN";
		QuizCategory category = categoryList.get(index - 1);
		
		QuizEvaluation evaluation = getQuizEvaluation(category.getId(), result);
		if(variableParam.startsWith("category.name")){
			return category.getName();
		}else if(variableParam.startsWith("category.score")){
			int score = result.getScoreMap().get(String.valueOf(category.getId()));
			return String.valueOf(score);
		}else if(variableParam.startsWith("category.healthStatus")){
			return evaluation.getHealthStatus();
		}else if(variableParam.startsWith("category.evaluation")){
			return evaluation.getEvaluation();
		}else if(variableParam.startsWith("category.suggestion")){
			return evaluation.getSuggestion();
		}
		return "NaN";
	}
	
	/**
	 * 测谎题总分
	 * @param result
	 * @return
	 */
	private String getLieScore(QuizResult result){
		return String.valueOf(result.getLieScore());
	}
	/**
	 * 总分
	 * @param result
	 * @return
	 */
	private String getTotalScore(QuizResult result){
		return String.valueOf(result.getScore());
	}
	/**
	 * 获取总分的评价
	 * @param result
	 * @return
	 */
	private QuizEvaluation getTotalQuizEvaluation(QuizResult result){
		String key = result.getId() + "_total";
		
		if(evaMap.containsKey(key)) return evaMap.get(key);
		
		QuizCategory category = quizCategoryService.getTotalCategory(result.getQuizId());
		if(category == null){
			logger.error("获取总分的categoryId失败!! quizId = " + result.getQuizId());
		}
		QuizEvaluation eva = quizEvaluationService.getPersonEvaluation(result.getQuizId(), category.getId(), result.getScore());
		// 写入缓存
		evaMap.put(key, eva);
		return eva;
	}
	/**
	 * 获取指定维度的评价
	 * @param categoryId
	 * @param result
	 * @return
	 */
	private QuizEvaluation getQuizEvaluation(int categoryId, QuizResult result){
		String key = result.getId() + "_" + categoryId;
		if(evaMap.containsKey(key)) return evaMap.get(key);
		
		Map<String, Integer> scoreMap = result.getScoreMap();
		int score = scoreMap.get(String.valueOf(categoryId));
		QuizEvaluation eva = quizEvaluationService.getPersonEvaluation(result.getQuizId(), categoryId, score);
		evaMap.put(key, eva);
		return eva;
	}
	
}
