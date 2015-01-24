package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizItem;

@IocBean(name = "quizDao", fields = { "dataSource" })
public class QuizDao extends NutDao {
	public List<Quiz> fetchListByParentId(long parentId) {
		return this.query(Quiz.class, Cnd.where("parentId", "=", parentId));
	}

	public void deleteByParentId(long parentId) {

		this.clear(Quiz.class, Cnd.where("parentId", "=", parentId));
	}
}
