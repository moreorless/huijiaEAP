package com.huijia.eap.quiz.module;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizResultService;
import com.huijia.eap.quiz.service.QuizService;

@IocBean
@InjectName
@AuthBy(login = false)
@At("/quiz/report/chart")
public class ReportChartModule {
	@Inject
	private QuizService quizService;
	@Inject
	private QuizResultService quizResultService;
	@Inject
	private QuizCategoryService quizCategoryService;
	
	/**
	 * 按维度统计的横向柱状图
	 * @param request
	 * @param userId
	 * @param quizId
	 */
	@At
	@Ok("jsp:jsp.quiz.report.chart.stat_by_category")
	public void statByCategory(HttpServletRequest request, @Param("userId") long userId, @Param("quizId") long quizId){
		QuizResult result = quizResultService.getQuizResult(userId, quizId).get(0);
		Map<String, Integer> scoreMap = result.getScoreMap();
		List<QuizCategory> categoryList = quizCategoryService.getByQuizId(quizId);
		List<String> categoryNames = new LinkedList<String>();
		List<Integer> scoreList = new LinkedList<Integer>();
		Iterator<QuizCategory> iter = categoryList.iterator();
		while(iter.hasNext()){
			QuizCategory category = iter.next();
			categoryNames.add(category.getName());
			int score = scoreMap.get(String.valueOf(category.getId()));
			scoreList.add(score);
		}
		request.setAttribute("categorieNames", categoryNames);
		request.setAttribute("scoreArray", scoreList);
	}
	
	/**
	 * 按维度统计的雷达图
	 * @param request
	 * @param userId
	 * @param quizId
	 */
	@At
	@Ok("jsp:jsp.quiz.report.chart.radar_by_category")
	public void radarByCategory(HttpServletRequest request, @Param("userId") long userId, @Param("quizId") long quizId){
		QuizResult result = quizResultService.getQuizResult(userId, quizId).get(0);
		Map<String, Integer> scoreMap = result.getScoreMap();
		List<QuizCategory> categoryList = QuizCache.me().getCategoryList(quizId);
		List<String> categoryNames = new LinkedList<String>();
		List<Integer> scoreList = new LinkedList<Integer>();
		Iterator<QuizCategory> iter = categoryList.iterator();
		while(iter.hasNext()){
			QuizCategory category = iter.next();
			categoryNames.add(category.getName());
			int score = scoreMap.get(String.valueOf(category.getId()));
			scoreList.add(score);
		}
		request.setAttribute("categorieNames", categoryNames);
		request.setAttribute("scoreArray", scoreList);
	}
}
