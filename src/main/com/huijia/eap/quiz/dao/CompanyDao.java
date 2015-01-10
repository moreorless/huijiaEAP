package com.huijia.eap.quiz.dao;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(name="companyDao", fields={"dataSource"})
public class CompanyDao extends NutDao{

}
