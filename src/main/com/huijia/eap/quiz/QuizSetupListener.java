package com.huijia.eap.quiz;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.SetupListener;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.report.provider.ChartDataFactory;
import com.huijia.eap.quiz.service.handler.SMSHandler;
import com.huijia.eap.quiz.task.ReportClearTask;
import com.huijia.eap.schedule.DefaultScheduleIterator;
import com.huijia.eap.schedule.Scheduler;

public class QuizSetupListener implements SetupListener{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void init(NutConfig config) {
		logger.info("QuizSetupListener init start");
		Ioc ioc = config.getIoc();
		
		// 加载单例类 QuizCache, ChartDataFactory
		ioc.get(QuizCache.class);
		ioc.get(ChartDataFactory.class);
		
		// 初始化短信服务
		initSMSService();
		
		// 启动报表临时文件清理任务，每天执行一次
		Scheduler.getDefault().schedule(new ReportClearTask("ReportClearTask"), 
						new DefaultScheduleIterator(Calendar.getInstance(), Calendar.HOUR, 24, 0));
	}
	
	
	private void initSMSService(){
		//添加短信服务变量
		SMSHandler SMSService = new SMSHandler("1", 1);
		SMSService.initSMSService();
		GlobalConfig.addContextValue("SMS", SMSService);
		
	}
	

	@Override
	public void destroy(NutConfig config) {
		Scheduler.getDefault().removeSchedulerTask("ReportClearTask");
	}

}
