package com.huijia.eap.schedule;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.listeners.JobListenerSupport;

public class SchedulerJobListener extends JobListenerSupport {

	@Override
	public String getName() {
		return "SchedulerTaskStatusListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobDetail job = context.getJobDetail();
		SchedulerTask task = (SchedulerTask) job.getJobDataMap().get(Scheduler.SCHEDULER_TASK_KEY);
		task.state = SchedulerTask.State.RUNNING;
		
		try {
			if (context.getScheduler().checkExists(job.getKey()))
				context.getScheduler().addJob(job, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		JobDetail job = context.getJobDetail();
		SchedulerTask task = (SchedulerTask) job.getJobDataMap().get(Scheduler.SCHEDULER_TASK_KEY);
		if (context.getNextFireTime() == null)
			task.state = SchedulerTask.State.FINISHED;
		else
			task.state = SchedulerTask.State.WAITING;
		
		try {
			if (context.getScheduler().checkExists(job.getKey()))
				context.getScheduler().addJob(job, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
