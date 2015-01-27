package com.huijia.eap.quiz.dao;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name = "quizResultDao", fields = { "dataSource" })
public class QuizResultDao extends NutDao{

}
