package com.huijia.eap.admin;


import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.huijia.eap.annotation.AuthBy;

@IocBean
@InjectName
@AuthBy(check=false)
@At("/")
public class AdminModule {

	@At
	@Ok("jsp:jsp.admin.index")
	public void admin(){
	}
	
}
