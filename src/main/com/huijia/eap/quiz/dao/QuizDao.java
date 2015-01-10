package com.huijia.eap.quiz.dao;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name="quizDao", fields={"dataSource"})
public class QuizDao extends NutDao{

}
