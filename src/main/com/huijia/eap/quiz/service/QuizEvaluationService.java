package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizEvaluationDao;
import com.huijia.eap.quiz.dao.QuizItemRelationDao;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItemRelation;


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
		List<QuizEvaluation> users = query(condition, this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);
		
		return pager;
	}
	
}
