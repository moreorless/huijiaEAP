package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizItemRelationDao;
import com.huijia.eap.quiz.data.QuizItemRelation;

@IocBean
public class QuizItemRelationService extends
		TblIdsEntityService<QuizItemRelation> {

	@Inject("refer:quizItemRelationDao")
	public void setQuizItemRelationDao(Dao dao) {
		setDao(dao);
	}

	public QuizItemRelation insert(QuizItemRelation quizItemRelation) {
		return this.dao().insert(quizItemRelation);
	}

	public List<QuizItemRelation> fetchListByQuizId(long quizId) {
		return ((QuizItemRelationDao) this.dao()).fetchListByQuizId(quizId);
	}

	public void deleteByQuizId(long quizId) {
		((QuizItemRelationDao) this.dao()).deleteByQuizId(quizId);
	}
}
