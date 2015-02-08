package com.huijia.eap.quiz.task;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.nutz.lang.Files;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.schedule.SchedulerTask;

public class ReportClearTask extends SchedulerTask{

	Logger logger = Logger.getLogger(this.getClass());
	public ReportClearTask(String taskName) {
		super(taskName);
	}

	@Override
	public void run() {
		logger.info("删除前一天的报表临时文件");
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(date);
		
		// 临时文件存放目录webContent/download/report/yyyy-MM-dd/
		String webdir = GlobalConfig.getContextValueAs(String.class, "web.dir");
		String reportPath = webdir + File.separator + "download" + File.separator + "report" + File.separator + dateStr;
		Files.deleteDir(new File(reportPath));
		
	}

	@Override
	public boolean cancel() {
		return false;
	}

}
