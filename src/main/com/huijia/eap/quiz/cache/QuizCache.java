package com.huijia.eap.quiz.cache;

import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 试题缓存
 * @author leon
 *
 */
@IocBean
public class QuizCache {
	private static QuizCache _instance = new QuizCache();
	
	private Map<Long, Quiz> _cache = new HashMap<Long, Quiz>();
	
	@Inject
	private QuizService quizService;
	
	private QuizCache(){};
	public static QuizCache me(){
		return _instance;
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
}
