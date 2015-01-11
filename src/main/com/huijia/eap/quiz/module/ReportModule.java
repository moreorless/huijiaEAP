package com.huijia.eap.quiz.module;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.huijia.eap.annotation.AuthBy;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/report")
public class ReportModule {

	@At
	@Ok("jsp:jsp.report.main")
	public void main(){
		
	}
}
