package com.huijia.eap.quiz.module;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
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
	
	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT ="edit";
	private final static String OPERATION_READ ="read";
	
	@Inject
	private CompanyService companyService;
	
	@At
	@Ok("forward:/company/list")
	@Chain("validate")
	public void add(@Param("..") Company company){
		companyService.insert(company);
	}
	
	@At
	@Ok("jsp:jsp.company.edit")
	public void prepare(HttpServletRequest request, @Param("id") long id, 
			@Param("operation") String operation) {
		Company company = new Company();
		if(OPERATION_EDIT.equals(operation)){
			company = companyService.fetch(id);
		}
		request.setAttribute("company", company);
	}
	
	@At
	@Ok("forward:/company/list")
	@Chain("validate")
	public void edit(@Param("..") Company company){
		companyService.update(company);
	}
	
	@At
	@Ok("jsp:jsp.company.list")
	public Pager<Company> list(HttpServletRequest request, @Param("..") Pager<Company> pager) {
		return companyService.paging(null, pager);
	}
}
