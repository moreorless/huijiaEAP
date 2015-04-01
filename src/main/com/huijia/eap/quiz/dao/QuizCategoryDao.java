package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizCategory;

@IocBean(name = "quizCategoryDao", fields = { "dataSource" })
public class QuizCategoryDao extends NutDao {

	public List<QuizCategory> fetchListByParentId(long parentId) {
		return this.query(QuizCategory.class,
				Cnd.where("parentId", "=", parentId));
	}

	public List<QuizCategory> fetchListByQuizId(long quizId) {
		return this.query(QuizCategory.class, Cnd.where("quizId", "=", quizId));
	}

	public void deleteByQuizId(long quizId) {

		this.clear(QuizCategory.class, Cnd.where("quizId", "=", quizId));
	}
}
