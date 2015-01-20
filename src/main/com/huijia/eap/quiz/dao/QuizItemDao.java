package com.huijia.eap.quiz.dao;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.QuizItem;

import java.util.List;

@IocBean(name="quizItemDao", fields={"dataSource"})
public class QuizItemDao extends NutDao{
	public List<QuizItem> getItemsByQuizId(long quizId){
		return this.query(QuizItem.class, Cnd.where("quizid", "=", quizId));
	}
}
