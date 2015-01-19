package com.huijia.eap.quiz.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
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

}
