package com.huijia.eap.quiz.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;


import com.huijia.eap.quiz.data.NormResultBean;
import com.huijia.eap.quiz.data.QuizResult;

@IocBean
public class ForcedQuizService {

	@Inject
	private QuizResultService quizResultService;
	
	/**
	 * 获取迫选题的答题结果
	 * @param userId
	 * @param quizId
	 * @return
	 */
	public List<NormResultBean> getAnswerResult(long userId, long quizId){
		List<NormResultBean> result = new ArrayList<>();
		List<QuizResult> list = quizResultService.getQuizResult(userId, quizId);
		if(list == null || list.size() < 1) return result;
		
		QuizResult _quizResult = list.get(0);
		String scoreJson = _quizResult.getScoreJson();
		Map _map = Json.fromJson(Map.class, scoreJson);
		Iterator<Map> iter = _map.values().iterator();
		while(iter.hasNext()){
			String _json = Json.toJson(iter.next());
			NormResultBean bean = Json.fromJson(NormResultBean.class, _json);
			result.add(bean);
		}
		return result;
	}
	
	

	
}
