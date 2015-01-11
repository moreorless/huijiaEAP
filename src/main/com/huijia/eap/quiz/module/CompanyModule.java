package com.huijia.eap.quiz.module;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.huijia.eap.annotation.AuthBy;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/company")
public class CompanyModule {
	@At
	@Ok("jsp:jsp.company.list")
	public void list() {
		
	}
}
