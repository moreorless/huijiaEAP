package com.huijia.eap.quiz.module;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.service.CompanyService;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/company")
public class CompanyModule {
	
	@Inject
	private CompanyService companyService;
	
	@At
	@Ok("jsp:jsp.company.list")
	public Pager<Company> list(HttpServletRequest request, @Param("..") Pager<Company> pager) {
		return companyService.paging(null, pager);
	}
}
