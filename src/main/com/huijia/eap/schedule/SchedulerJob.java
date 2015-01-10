package com.huijia.eap.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
public class SchedulerJob implements Job {

	SchedulerTask task;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		if (task != null)
			task.run(context);
		
	}

	public void setTask(Object task) {
		this.task = (SchedulerTask) task;
	}

}
