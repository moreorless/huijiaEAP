package com.huijia.eap.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.quiz.data.QuizResult;

@IocBean
public class QuizResultService extends TblIdsEntityService<QuizResult>{

	@Inject
	private UserService userService;
	
	@Inject("refer:quizResultDao")
	public void setQuizResultDao(Dao dao) {
		setDao(dao);
	}
	
	public QuizResult insert(QuizResult quizResult){
		quizResult.setId(getTblMaxIdWithUpdate() + 1);
		return this.dao().insert(quizResult);
	}
	
	/**
	 * 根据试卷id删除
	 * @param quizId
	 * @return
	 */
	public int deleteByQuizId(long quizId) {
		return this.dao().clear(getEntityClass(), Cnd.where("quizId", "=", quizId));
	}
	
	public List<QuizResult> getQuizResult(long userId, long quizId){
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		
		if(quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT){
			for(Quiz _quiz : quiz.getChildList()){
				List<QuizResult> _rList = getQuizResult(userId, _quiz.getId());
				resultList.addAll(_rList);
			}
			return resultList;
		}
		
		// 如果同一用户 对同一问卷，回答了多次，取最近一次
		List<QuizResult> tmpList = this.dao().query(getEntityClass(), Cnd.where("quizId", "=", quizId).and("userId", "=", userId).desc("timestamp"));
		if(tmpList.size() > 0){
			resultList.add(tmpList.get(0));
		}
		return resultList;
	}
	
	public List<QuizResult> storeResult(long userId, Quiz quiz, Map<Long, String> answerMap){
		List<QuizResult> resultList = new ArrayList<QuizResult>();
		if(quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for(Quiz _quiz : quiz.getChildList()){
				List<QuizResult> _rList = storeResult(userId, _quiz, answerMap);
				resultList.addAll(_rList);
			}
			return resultList;
		}
		
		// 总分
		int totalScore = 0;
		// 测谎题总分
		int lieScore = 0;
		
		List<QuizItem> quizItems = quiz.getItems();
		Map<Long, String> quizAnswer = new HashMap<Long, String>();
		Iterator<QuizItem> qIter = quizItems.iterator();
		
		Map<Integer, Integer> scoreByCategory = new HashMap<>();
		// 计算每个题目的分数
		while(qIter.hasNext()){
			QuizItem qItem = qIter.next();
			
			// 该题的答案序号
			String answerIndex = answerMap.get(qItem.getId());
			quizAnswer.put(qItem.getId(), answerIndex);
			QuizItemOption qOption = qItem.getOption(answerIndex);
			// 该答案对应的分值
			int optScore = qOption.getValue();
			String categoryName = qOption.getCategoryName();
			QuizCategory category = quiz.getCategoryByName(categoryName);
			int categoryId = category.getId();
			
			// 如果是测谎题目
			if(qItem.isLieFlag()){
				lieScore += optScore;
				continue;
			}
			
			// 按分类统计分数
			int _score = optScore;
			if(scoreByCategory.containsKey(categoryId)){
				_score = scoreByCategory.get(categoryId) + optScore;
			}
			scoreByCategory.put(categoryId, _score);
			
			// 加入总分
			totalScore += optScore;
		}
		
		// 取分数最高的分类
		int maxCategoryScore = 0;
		int maxCategoryId = 0;
		for(int categoryId : scoreByCategory.keySet()){
			int _score = scoreByCategory.get(categoryId);
			if(_score > maxCategoryScore){
				maxCategoryId = categoryId;
				maxCategoryScore = _score;
			}
		}
		
		User user = userService.fetch(userId);
		QuizResult quizResult = new QuizResult();
		quizResult.setId(getTblMaxIdWithUpdate());
		quizResult.setQuizId(quiz.getId());
		quizResult.setUserId(userId);
		quizResult.setCompanyId(user.getCompanyId());
		quizResult.setTimestamp(System.currentTimeMillis());
		quizResult.setAnswer(Json.toJson(quizAnswer));
		quizResult.setScore(totalScore);
		quizResult.setScoreJson(Json.toJson(scoreByCategory));
		quizResult.setCategoryId(maxCategoryId);
		quizResult.setCategoryName(quiz.getCategoryById(maxCategoryId).getName());
		
		// 存在测谎题，校验有效性
		if(lieScore > 0 || quiz.getLieBorder() > 0){
			if(lieScore > quiz.getLieBorder()) {
				quizResult.setValid(false);
			}
		}
		
		quizResult = this.dao().insert(quizResult);
		
		resultList.add(quizResult);
		return resultList;
	}
	
}
