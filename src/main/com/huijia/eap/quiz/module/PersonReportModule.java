package com.huijia.eap.quiz.module;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizEvaluationService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizResultService;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 个人报告控制类
 * @author leon
 *
 */
@IocBean
@InjectName
@AuthBy(login=false)
@At("/report/person")
public class PersonReportModule {
	private Logger logger = Logger.getLogger(this.getClass());

	@Inject
	private QuizService quizService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private QuizItemService quizItemService;
	
	@Inject
	private QuizResultService quizResultService;
	
	@Inject
	private QuizCategoryService quizCategoryService;
	
	@Inject
	private QuizEvaluationService quizEvaluationService;
	/**
	 * 个人心理分析报告
	 */
	@At("/mental_checkup")
	@Ok("jsp:jsp.report.person.mental_checkup")
	public void mentalCheckup(HttpServletRequest request, @Param("quizId") long quizId, @Param("userId") long userId){
		User user = userService.fetch(userId);
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		preProcess(request, user, quiz);
		
		QuizResult result = quizResultService.getQuizResult(userId, quizId).get(0);
		Map<String, Integer> scoreMap = result.getScoreMap();
		List<QuizCategory> categoryList = quizCategoryService.getByQuizId(quizId);
		List<String> categoryNames = new LinkedList<String>();
		List<Integer> scoreList = new LinkedList<Integer>();
		List<QuizEvaluation> evaluationList = new LinkedList<>();
		
		Iterator<QuizCategory> iter = categoryList.iterator();
		while(iter.hasNext()){
			QuizCategory category = iter.next();
			String categoryId = String.valueOf(category.getId());
			if(scoreMap.containsKey(categoryId)){
				int score = scoreMap.get(categoryId);
				scoreList.add(score);
				categoryNames.add(category.getName());
				
				QuizEvaluation eva = quizEvaluationService.getPersonEvaluation(result.getQuizId(), category.getId(), score);
				evaluationList.add(eva);
			}
		}
		request.setAttribute("categorieNames", categoryNames);		// 各维度名称
		request.setAttribute("scoreArray", scoreList);				// 各维度得分
		request.setAttribute("quizResult", result);
		request.setAttribute("evaluationList", evaluationList);     // 各维度评价信息
		
		// 获取评价信息
		QuizCategory category = quizCategoryService.getTotalCategory(result.getQuizId());
		if(category == null){
			logger.error("获取总分的categoryId失败!! quizId = " + result.getQuizId());
		}
		QuizEvaluation total_eva = quizEvaluationService.getPersonEvaluation(result.getQuizId(), category.getId(), result.getScore());
		request.setAttribute("total_evaluation", total_eva);		// 总分评价
		
		
	}
	
	/**
	 * 沟通风格与冲突处理
	 */
	@At("/communicate_conflict")
	@Ok("jsp:jsp.report.person.communicate_conflict")
	public void communicateConflict(HttpServletRequest request, @Param("quizId") long quizId, @Param("userId") long userId){
		User user = userService.fetch(userId);
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		preProcess(request, user, quiz);
		
		List<Quiz> quizList = new ArrayList<Quiz>();
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_STANDALONE) {
			quizList.add(quiz);
		} else if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			quizList.addAll(quiz.getChildList());
		}
		request.setAttribute("quizlist", quizList);
		
		List<QuizResult> resultlist = quizResultService.getQuizResult(user.getUserId(), quiz.getId());
		for(int i= 0; i < resultlist.size(); i++){
			QuizResult result = resultlist.get(i);
			Map<String, Integer> scoreMap = result.getScoreMap();
			List<QuizCategory> categoryList = quizCategoryService.getByQuizId(result.getQuizId());
			List<String> categoryNames = new LinkedList<String>();
			List<Integer> scoreList = new LinkedList<Integer>();
			Iterator<QuizCategory> iter = categoryList.iterator();
			while(iter.hasNext()){
				QuizCategory category = iter.next();
				String categoryId = String.valueOf(category.getId());
				if(scoreMap.containsKey(categoryId)){
					int score = scoreMap.get(categoryId);
					scoreList.add(score);
					categoryNames.add(category.getName());
				}
			}
			request.setAttribute("categorieNames_" + i, categoryNames);		// 各维度名称
			request.setAttribute("scoreArray_" + i, scoreList);				// 各维度得分
		}
		
	}
	

	
	/**
	 * 情绪管理倾向
	 */
	@At("/emotion_management")
	@Ok("jsp:jsp.report.person.emotion_management")
	public void emotionManagement(HttpServletRequest request, @Param("quizId") long quizId, @Param("userId") long userId){
		User user = userService.fetch(userId);
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		preProcess(request, user, quiz);
	}
	
	/**
	 * 儿童归因形态风格分析
	 */
	@At("/child_attribution")
	@Ok("jsp:jsp.report.person.child_attribution")
	public void childAttribution(HttpServletRequest request, @Param("quizId") long quizId, @Param("userId") long userId){
		User user = userService.fetch(userId);
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		preProcess(request, user, quiz);
		
		QuizResult result = quizResultService.getQuizResult(userId, quizId).get(0);
		String answerStr = result.getAnswer();
		Map<String, String> answerMap = (Map<String, String>)Json.fromJson(answerStr);
		
		double pmb = 0.0, pmg = 0.0, psb = 0.0, psg = 0.0, pvb = 0.0, pvg = 0.0;
		List<QuizCategory> categoryList = quizCategoryService.getCategoryList(quizId);
		for(String quizItemId : answerMap.keySet()){
			QuizItem quizItem = quizItemService.fetch(Long.parseLong(quizItemId));
			String answer = answerMap.get(quizItemId);
			QuizItemOption option = quizItem.getOption(answer);
			switch (option.getCategoryName()) {
			case "PMB":
				pmb += option.getValue();
				break;
			case "PMG":
				pmg += option.getValue();
				break;
			case "PSB":
				psb += option.getValue();
				break;
			case "PSG":
				psg += option.getValue();
				break;
			case "PVB":
				pvb += option.getValue();
				break;
			case "PVG":
				pvg += option.getValue();
				break;
			default:
				break;
			}
		}
		
		double scoreB = pmb + pvb + psb;
		double scoreG = pmg + pvg + psg;
		double scoreG_B = scoreG - scoreB;
		request.setAttribute("PMB", pmb);
		request.setAttribute("PVB", pvb);
		request.setAttribute("PSB", psb);
		request.setAttribute("PMG", pmg);
		request.setAttribute("PVG", pvg);
		request.setAttribute("PSG", psg);
		request.setAttribute("scoreB", scoreB);
		request.setAttribute("scoreG", scoreG);
		request.setAttribute("scoreG_B", scoreG_B);
		request.setAttribute("HoB", pmb + pvb);
		
	}
	
	
	/**
	 * 预处理报告中使用的一些通用数据，如姓名、年龄等
	 */
	private void preProcess(HttpServletRequest request, User user, Quiz quiz){
		request.setAttribute("user", user);
		request.setAttribute("quiz", quiz);
		
		List<QuizResult> resultlist = quizResultService.getQuizResult(user.getUserId(), quiz.getId());
		request.setAttribute("resultlist", resultlist);
		
		if(resultlist != null && resultlist.size() > 0){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(resultlist.get(0).getTimestamp());
			String testDate = calendar.get(Calendar.YEAR) + "年" + 
				(calendar.get(Calendar.MONTH) + 1) + "月" +
				calendar.get(Calendar.DAY_OF_MONTH) + "日";
			
			request.setAttribute("testDate", testDate);
		}
			
	}
}
