package com.huijia.eap.quiz.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 试题缓存
 * @author leon
 *
 */
@IocBean(create="init")
public class QuizCache {
	private static QuizCache _instance = new QuizCache();
	
	private Map<Long, Quiz> _cache = new HashMap<Long, Quiz>();
	
	private Map<Long, List<QuizCategory>> _categoryMap = new HashMap<Long, List<QuizCategory>>();
	
	@Inject
	private QuizService quizService;
	@Inject
	private QuizCategoryService quizCategoryService;
	
	private QuizCache(){};
	public static QuizCache me(){
		return _instance;
	}
	
	public void init(){
		// 加载所有试卷至缓存
		List<Quiz> quizList = quizService.query(null, null);
		for(Quiz quiz : quizList){
			_cache.put(quiz.getId(), quizService.fetchFullQuiz(quiz.getId()));
		}
	}
	
	public Quiz getQuiz(long quizId){
		if(!_cache.containsKey(quizId)){
			// 若缓存不存在，从数据库中获取
			Quiz quiz = quizService.fetchFullQuiz(quizId);
			if(quiz == null) return null;
			_cache.put(quizId, quiz);
		}
		return _cache.get(quizId);
	}
	
	
	/**
	 * 更新缓存
	 * @param quiz
	 */
	public void update(Quiz quiz){
		if(quiz == null) return;
		_cache.put(quiz.getId(), quizService.fetchFullQuiz(quiz.getId()));
	}
	public void delete(long quizId){
		_cache.remove(quizId);
	}
	
	/**
	 * 获取试卷的维度列表
	 * @param quizId
	 * @return
	 */
	public List<QuizCategory> getCategoryList(long quizId){
		if(_categoryMap.containsKey(quizId)){
			return _categoryMap.get(quizId);
		}
		List<QuizCategory> list = quizCategoryService.getCategoryList(quizId);
		_categoryMap.put(quizId, list);
		return list;
	}
}
