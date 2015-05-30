package com.huijia.eap.quiz.module;

import java.util.Map;

import org.jaxen.function.FalseFunction;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.quiz.service.ReportTemple;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/report")
public class ReportModule {

	@At
	@Ok("jsp:jsp.report.main")
	public void main(){
		
	}
	
	@At
	@Ok("json")
	@Fail("json")
	public Map<String, String> listTemple(){
		return ReportTemple.getTemple();
	}
	
	@At
	@Ok("jsp:jsp.quiz.report.demo")
	@AuthBy(login=false)
	public void demo(){
		
	}
	

	
}
