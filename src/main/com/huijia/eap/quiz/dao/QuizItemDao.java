package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizItem;

@IocBean(name = "quizItemDao", fields = { "dataSource" })
public class QuizItemDao extends NutDao {
	
	public List<QuizItem> fetchListByQuizId(long quizId) {
		return this.query(QuizItem.class, Cnd.where("quizid", "=", quizId).asc("id"));
	}

	public void deleteByQuizId(long quizId) {

		this.clear(QuizItem.class, Cnd.where("quizid", "=", quizId));
	}
}
