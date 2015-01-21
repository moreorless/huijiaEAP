package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizEvaluation;

@IocBean(name = "quizEvaluationDao", fields = { "dataSource" })
public class QuizEvaluationDao extends NutDao {
	public void deleteByQuizId(long quizId) {

		this.clear(QuizEvaluation.class, Cnd.where("quizid", "=", quizId));
	}
	
	public List<QuizEvaluation> fetchListByQuizId(long quizId) {
		String condition = "quizid=" + quizId;
		return this.query(QuizEvaluation.class, Cnd.wrap(condition), null);
	}
}
