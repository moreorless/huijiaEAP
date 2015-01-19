package com.huijia.eap.quiz.dao;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name="quizEvaluationDao", fields={"dataSource"})
public class QuizEvaluationDao extends NutDao{

}
