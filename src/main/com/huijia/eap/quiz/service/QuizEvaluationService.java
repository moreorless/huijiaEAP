package com.huijia.eap.quiz.service;

import java.util.Iterator;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizEvaluationDao;
import com.huijia.eap.quiz.data.QuizEvaluation;


@IocBean
public class QuizEvaluationService extends TblIdsEntityService<QuizEvaluation>{

	
	@Inject("refer:quizEvaluationDao")
	public void setQuizEvaluationDao(Dao dao) {
		setDao(dao);
	}
	
	public QuizEvaluation insert(QuizEvaluation quizEvaluation, long l) {
		quizEvaluation.setId(getTblMaxIdWithUpdate());
		quizEvaluation.setQuizId(l);
		return this.dao().insert(quizEvaluation);
	}
	
	public void update(QuizEvaluation quizEvaluation) {
		//this.dao().update(quizEvaluation);
	}
	
	public List<QuizEvaluation> fetchAll(){
		return super.query(null, null);
	}
	
	public void deleteByQuizId(long quizId) {
		((QuizEvaluationDao) this.dao()).deleteByQuizId(quizId);
	}
	
	public List<QuizEvaluation> fetchListByQuizId(long quizId) {
		return ((QuizEvaluationDao) this.dao()).fetchListByQuizId(quizId);
	}
	
	/**
	 * 分页返回所有列表
	 */
	public Pager<QuizEvaluation> paging(Condition condition, Pager<QuizEvaluation> pager) {
		List<QuizEvaluation> list = query(condition, this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(list);
		
		return pager;
	}
	
	/**
	 * 根据答题结果获个人取评价
	 */
	public QuizEvaluation getPersonEvaluation(long quizId, long categoryId, int score){
		List<QuizEvaluation> list = this.query(Cnd.where("quizId", "=", quizId).and("categoryId", "=", categoryId)
				.and("type", "=", "singleMain"), null);
		if(list == null) return null;
		Iterator<QuizEvaluation> iter = list.iterator();
		while(iter.hasNext()){
			QuizEvaluation evaluation = iter.next();
			if(evaluation.getMinScore() <= score && evaluation.getMaxScore() >= score) return evaluation;
		}
		return null;
	}
	
}
