package com.huijia.eap.quiz.service;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.QuizAnswerLog;

import java.util.List;

@IocBean(fields={"dao"})
public class QuizAnswerLogService extends TblIdsEntityService<QuizAnswerLog>{
	public QuizAnswerLog insert(QuizAnswerLog log){
		return this.dao().insert(log);
	}
	
	/**
	 * 根据用户id, 试卷id获取答题历史记录
	 * @param userId
	 * @param quizId
	 * @return
	 */
	public List<QuizAnswerLog> getHistory(long userId, long quizId){
		List<QuizAnswerLog> list = this.query(Cnd.where("userId", "=", userId)
				.and("quizId", "=", quizId), null);
		return list;
	}
	
	/**
	 * 根据用户id获取答题历史记录
	 * @param userId
	 * @return
	 */
	public List<QuizAnswerLog> getHistory(long userId){
		List<QuizAnswerLog> list = this.query(Cnd.where("userId", "=", userId), null);
		return list;
	}
	
	public int deleteByQuizId(long quizId){
		return this.dao().clear(this.getEntityClass(), Cnd.where("quizId", "=", quizId));
	}
}
