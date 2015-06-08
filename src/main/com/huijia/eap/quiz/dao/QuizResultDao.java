package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.data.Segment;

@IocBean(name = "quizResultDao", fields = { "dataSource" })
public class QuizResultDao extends NutDao {

	public List<QuizResult> fetchTestedList(long segmentId, long quizId) {
		return this.query(
				QuizResult.class,
				Cnd.where("segmentid", "=", segmentId).and("quizid", "=",
						quizId));
	}

	public List<QuizResult> fetchValidList(long segmentId, long quizId) {
		return this.query(
				QuizResult.class,
				Cnd.where("segmentid", "=", segmentId)
						.and("quizid", "=", quizId).and("valid", "=", 1));
	}
}
