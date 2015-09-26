package com.huijia.eap.quiz.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;




import com.huijia.eap.quiz.data.AnswerBean;
import com.huijia.eap.quiz.data.NormResultBean;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizResult;

@IocBean
public class ForcedQuizService {

	@Inject
	private QuizResultService quizResultService;
	
	@Inject
	private QuizItemService quizItemService;
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
	
	/**
	 * 获取原始答案列表
	 * @param userId
	 * @param quizId
	 * @return
	 */
	public List<AnswerBean> getAnswerBean(long userId, long quizId){
		List<AnswerBean> result = new ArrayList<>();
		List<QuizResult> list = quizResultService.getQuizResult(userId, quizId);
		if(list == null || list.size() < 1) return result;
		
		QuizResult _quizResult = list.get(0);
		String answer = _quizResult.getAnswer();
		List _list = Json.fromJson(List.class, answer);
		Iterator<Map> iter = _list.iterator();
		while(iter.hasNext()){
			String _json = Json.toJson(iter.next());
			AnswerBean bean = Json.fromJson(AnswerBean.class, _json);
			result.add(bean);
		}
		return result;
	}
	
	/**
	 * 获取社会赞许性均分
	 * @return
	 */
	public double getApprovalAverScore(long userId, long quizId){
		List<AnswerBean> answerList = this.getAnswerBean(userId, quizId);
		Iterator<AnswerBean> iter = answerList.iterator();
		
		double sum = 0;
		int count = 0;
		while(iter.hasNext()){
			AnswerBean bean = iter.next();
			QuizItem quizItem = quizItemService.fetch(bean.getQuestionId());
			if(quizItem.isLieFlag()){
				count++;
				sum += bean.getScore();
			}
		}
		
		if(count == 0) return 0;
		return sum / count;
	}
}
