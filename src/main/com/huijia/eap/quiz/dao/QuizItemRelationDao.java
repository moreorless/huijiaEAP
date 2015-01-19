package com.huijia.eap.quiz.dao;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizItemRelation;

@IocBean(name = "quizItemRelationDao", fields = { "dataSource" })
public class QuizItemRelationDao extends NutDao {

	public void deleteByQuizId(long quizId) {
		this.clear(QuizItemRelation.class, Cnd.where("quizid", "=", quizId));
	}

}
