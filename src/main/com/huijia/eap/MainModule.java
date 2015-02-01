package com.huijia.eap;

import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.LoadingBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;

import com.huijia.eap.annotation.JstlLocalization;
import com.huijia.eap.commons.mvc.ioc.ComboIocProvider;
import com.huijia.eap.commons.mvc.view.ExceptionViewMaker;

@LoadingBy(CupidNutLoading.class)
@Modules(scanPackage=true)
@Fail("error")
@IocBy(type = ComboIocProvider.class, args = {	//配置Ioc容器
		"*org.nutz.ioc.loader.json.JsonLoader", "ioc/dao.js", "ioc/service.js",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader","com.huijia.eap",
		"*org.nutz.ioc.loader.xml.XmlIocLoader", "ioc/controller.xml", "ioc/setuplistener.xml"} 
		)
@Views(ExceptionViewMaker.class)
@SetupBy(value = MainModuleSetup.class)
@JstlLocalization("../i18n")
@ChainBy(args="chain.js")
public class MainModule {

}
