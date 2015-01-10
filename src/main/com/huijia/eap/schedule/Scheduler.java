package com.huijia.eap.schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.nutz.mvc.NutConfig;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.PropertySettingJobFactory;
import org.quartz.utils.Key;

import com.huijia.eap.SetupListener;

/**
 * 调度器类，用于对定时任务进行调度<br/>
 * 一个调度器可以负责对多个调度任务的调度
 * 
 * <p>
 * 调度器包含分组的概念，同一类型或相似的任务应该放在同一个调度分组中，以便于获取和管理，也可以尽可能的避免调度任务名称的重复可能性。<br/>
 * 如果没有明确的指定，则调度任务会被放入一个缺省的分组中。
 * </p>
 */
public class Scheduler implements SetupListener
{
	private static Logger logger = Logger.getLogger(Scheduler.class);
	
	private static final List<Scheduler> schedulers = new ArrayList<Scheduler>();
	
	public static final String SCHEDULER_TASK_KEY = "task";

	private static final Scheduler me = new Scheduler(null);
	
	@Deprecated
	class SchedulerTimerTask extends TimerTask
	{
		private SchedulerTask schedulerTask;

		private ScheduleIterator iterator;

		public SchedulerTimerTask(SchedulerTask schedulerTask,
				ScheduleIterator iterator)
		{
			this.schedulerTask = schedulerTask;
			this.iterator = iterator;
		}

		public void run()
		{
			Thread runThread = new Thread(this.schedulerTask.name)
			{
				public void run()
				{
					try
					{
						schedulerTask.run();
					}
					catch (Exception e)
					{
						logger.info("schedulerTask.run: "+e.getMessage(), e);
					}

				}
			};
			runThread.start();
			reschedule(schedulerTask, iterator);
		}
	}

	private String name; //调度控制器名称
	
	private org.quartz.Scheduler timer;
	
	public String getName() {
		return name;
	}

	/**
	 * 获取默认的调度控制器
	 * @return
	 */
	public static final Scheduler getDefault() {
		if (logger.isDebugEnabled())
			logger.debug("fetching DEFAULT scheduler...");
		return me;
	}
	
	/**
	 * 创建一个新的调度控制器<br/>
	 * <b>需要自己保存这个控制器的引用</b>
	 * <p>
	 * 	调度器的配置文件中必须配置调度器的名称，且名称必须保证唯一，即不在通过{@link #existSchedulerNames()获得的名称列表中}
	 * </p>
	 * @param config 调度器使用的配置文件对象，如果未指定配置文件将抛出异常
	 * @return
	 */
	public static final Scheduler createScheduler(File config) {
		if (config == null || config.isDirectory() || !config.exists())
			throw new IllegalArgumentException("MUST assign the config file to create a new Scheduler instance.");
		return new Scheduler(config);
	}
	
	/**
	 * 获取当前所有现存调度器的名称集合
	 * @return
	 */
	public static final Set<String> existSchedulerNames() {
		Set<String> names = new LinkedHashSet<String>();
		for (Scheduler scheduler : schedulers) {
			if (scheduler != null) {
				names.add(scheduler.getName());
			}
		}
		
		return names;
	}

	private Scheduler(File config) {
		try {
			if (config == null)
				timer = StdSchedulerFactory.getDefaultScheduler();
			else
				timer = new StdSchedulerFactory(config.getAbsolutePath()).getScheduler();
			
			if (existSchedulerNames().contains(timer.getSchedulerName()))
				throw new IllegalArgumentException("Scheduler[" + timer.getSchedulerName() + "] already exist and started, CHANGE your own scheduler's name.");
			
			timer.setJobFactory(new PropertySettingJobFactory());
			
			timer.getListenerManager().addJobListener(new SchedulerJobListener(), EverythingMatcher.allJobs());
			timer.start();
			this.name = timer.getSchedulerName();
		} catch (SchedulerException e) {
			logger.error("Creating Scheduler[" + name + "] failed, caused by: ", e);
		}
		
		schedulers.add(this);
	}
	
	/**
	 * 销毁本控制器
	 */
	public void shutdown() {
		try {
			if (timer.isStarted()) {
				removeAllSchedulerTask();
				if (timer != null) {
					timer.shutdown(true);
					schedulers.remove(this);
				}
			}
		} catch (SchedulerException e) {
			logger.error("Shutting down Scheduler[" + name + "] failed, caused by: ", e);
		}
	}

	/**
	 * 从调度器的默认分组中删除一个调度任务
	 *
	 * @param name 待删除的调度任务名称
	 */
	public void removeSchedulerTask(String name)
	{
		removeSchedulerTask(name, null);
	}
	
	/**
	 * 从调度器的指定分组中删除一个调度任务
	 * @param name 待删除的调度任务名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #removeSchedulerTask(String)}
	 */
	public void removeSchedulerTask(String name, String group) {
		SchedulerTask schedulerTask = getSchedulerTaskByName(SchedulerTask.class, name, group);
		if (schedulerTask == null)
			return;
		
		schedulerTask.cancel();
		JobKey key = (group != null && group.trim().length() > 0) ? new JobKey(name, group) : new JobKey(name);
		try {
			timer.deleteJob(key);
		} catch (SchedulerException e) {
			logger.error("Removing Task[" + name + ", " + key.getGroup() + "] from Scheduler[" + this.name + "] failed, caused by: ", e);
		}
		
	}

	/**
	 * 删除所有调度任务
	 */
	public void removeAllSchedulerTask()
	{	
		try {
			if (timer != null)
				timer.clear();
		} catch (SchedulerException e) {
			logger.error("Removing ALL Task from Scheduler[" + this.name + "] failed, caused by: ", e);
		}
	}

	/**
	 * 从调度器的默认分组中取得一个调度任务
	 *
	 * @param name
	 *            欲取得的调度任务的名称
	 * @return SchedulerTask 取得的调度任务
	 */
	public <T extends SchedulerTask> T getSchedulerTaskByName(Class<T> classOfT, String name)
	{
		return getSchedulerTaskByName(classOfT, name, null);
	}
	
	/**
	 * 从调度器的指定分组中取得一个调度任务
	 * @param name 欲取得的调度任务的名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #getSchedulerTaskByName(String)}
	 * @return
	 */
	public <T extends SchedulerTask> T getSchedulerTaskByName(Class<T> classOfT, String name, String group) {
		if (name != null) {
			JobKey key = (group != null && group.trim().length() > 0) ? new JobKey(name, group) : new JobKey(name);
			try {
				JobDetail job = timer.getJobDetail(key);
				return getSchedulerTask(classOfT, job);
			} catch (SchedulerException e) {
				logger.error("fetch schedule task[" + name + ", " + key.getGroup() + "] from Scheduler[" + this.name + "] failed, caused by: ", e);
			}
		}
		
		return null;
	}

	protected <T extends SchedulerTask> T getSchedulerTask(Class<T> classOfT, JobDetail job) {
		return job == null ? null : (T) job.getJobDataMap().get(SCHEDULER_TASK_KEY);
	}
	
	/**
	 * 调度一个调度任务
	 *
	 * @param schedulerTask
	 *            需被调度的调度任务
	 * @param iterator
	 *            调度任务的时间迭代器，用于设置调度任务开始的时间，执行的频率及执行的次数
	 * @param group 调度任务组名称，如果为null或空字符串，则放入缺省组
	 */
	public void schedule(SchedulerTask schedulerTask, ScheduleIterator iterator, String group) {
		/*initTimer();
		Date time = iterator.getFirstTime();

		if (time == null)
		{
			logger.info("can't schedule this task for the wrong iterator");
			return;
		}
		else
		{
			synchronized (schedulerTask.lock)
			{
				if (schedulerTask.state != SchedulerTask.VIRGIN)
				{
					throw new IllegalStateException("Task already scheduled "
							+ "or cancelled");
				}
				schedulerTask.state = SchedulerTask.SCHEDULED;
				SchedulerTimerTask timerTask = new SchedulerTimerTask(schedulerTask,iterator);
				timer.schedule(timerTask, time);
				scheduleTaskList.put(schedulerTask.name, schedulerTask);
			}
		}*/
		if (timer == null) {
			logger.error("schedule task failed, cauesd Scheduler did not initilized!");
		} else {
			synchronized (schedulerTask.lock) {
				if (schedulerTask.state != SchedulerTask.State.VIRGIN) {
					throw new IllegalStateException("schedule task [" + schedulerTask.name + "] already scheduled or cancelled");
				}
				schedulerTask.state = SchedulerTask.State.SCHEDULED;
				JobDataMap data = new JobDataMap();
				data.put(SCHEDULER_TASK_KEY, schedulerTask);
				JobBuilder builder = JobBuilder.newJob(SchedulerJob.class).usingJobData(data).storeDurably(true);
				if (group != null && group.trim().length() > 0)
					builder.withIdentity(schedulerTask.name, group);
				else
					builder.withIdentity(schedulerTask.name);
				JobDetail job = builder.build();
				Trigger trigger = TriggerFactory.buildTrigger(schedulerTask.name, group, iterator);
				try {
					timer.scheduleJob(job, trigger);
				} catch (SchedulerException e) {
					logger.error("Schedule Task[" + schedulerTask.name + ", " + job.getKey().getGroup() + "] into Scheduler[" + this.name + "] failed, caused by: ", e);
				}
			}
		}
	}

	/**
	 * 调度一个调度任务，并将此调度任务归入缺省分组
	 *
	 * @param schedulerTask
	 *            需被调度的调度任务
	 * @param iterator
	 *            调度任务的时间迭代器，用于设置调度任务开始的时间，执行的频率及执行的次数
	 */
	public void schedule(SchedulerTask schedulerTask, ScheduleIterator iterator)
	{
		schedule(schedulerTask, iterator, null);
	}
	
	/**
	 * 暂停默认分组中的一个已经运行的调度任务
	 * @param name 任务名称
	 */
	public void pauseSchedulerTask(String name) {
		pauseSchedulerTask(name, null);
	}
	
	/**
	 * 暂停指定分组中的一个已经运行的调度任务
	 * @param name 任务名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #pauseSchedulerTask(String)}
	 */
	public void pauseSchedulerTask(String name, String group) {
		if (name != null){
			SchedulerTask task = getSchedulerTaskByName(SchedulerTask.class, name, group);
			if (task != null) {
				synchronized (task.lock) {
					if (task.state != SchedulerTask.State.PAUSED) {
						JobKey key = (group != null && group.trim().length() > 0) ? new JobKey(name, group) : new JobKey(name);
						try {
							timer.pauseJob(key);
							task.state = SchedulerTask.State.PAUSED;
						} catch (SchedulerException e) {
							logger.error("Pause Task[" + name + ", " + key.getGroup() + "] in Scheduler[" + this.name + "] failed, caused by: ", e);
						}
					}
				}
			} else if (logger.isDebugEnabled()) {
				logger.debug("Pause Task[" + name + ", " + group + "] invalid, because there is no Task[" + name + ", " + group + "] in Scheduler[" + this.name + "].");
			}
		}
	}
	
	/**
	 * 恢复默认分组中的一个已经暂停运行的调度任务
	 * @param name 任务名称
	 */
	public void resumeSchedulerTask(String name) {
		resumeSchedulerTask(name, null);
	}
	
	/**
	 * 恢复指定分组中的一个已经暂停运行的调度任务
	 * @param name 任务名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #resumeSchedulerTask(String)}
	 */
	public void resumeSchedulerTask(String name, String group) {
		if (name != null) {
			SchedulerTask task = getSchedulerTaskByName(SchedulerTask.class, name, group);
			if (task != null) {
				synchronized (task.lock) {
					if (task.state == SchedulerTask.State.PAUSED) {
						JobKey key = (group != null && group.trim().length() > 0) ? new JobKey(name, group) : new JobKey(name);
						try {
							timer.resumeJob(key);
							task.state = SchedulerTask.State.SCHEDULED;
						} catch (SchedulerException e) {
							logger.error("Resume Task[" + name + ", " + key.getGroup() + "] in Scheduler[" + this.name + "] failed, caused by: ", e);
						}
					}
				}
			} else if (logger.isDebugEnabled()) {
				logger.debug("Resume Task[" + name + ", " + group + "] invalid, because there is no Task[" + name + ", " + "] in Scheduler[" + this.name + "].");
			}
		}
	}
	
	/**
	 * 从默认分组中获取所有的调度任务
	 * @param <T>
	 * @param classOfT 调度任务的具体实现类
	 * @return
	 */
	public <T extends SchedulerTask> List<T> getSchedulerTasks(Class<T> classOfT) {
		return getSchedulerTasks(classOfT, null);
	}
	
	/**
	 * 从指定分组中获取所有的调度任务
	 * @param <T>
	 * @param classOfT 调度任务的具体实现类
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link Scheduler#getSchedulerTasks(Class)}
	 * @return
	 */
	public <T extends SchedulerTask> List<T> getSchedulerTasks(Class<T> classOfT, String group) {
		List<T> tasks = new ArrayList<T>();
		try {
			GroupMatcher<JobKey> matcher = (group != null && group.trim().length() > 0) ? GroupMatcher.groupEquals(group) : GroupMatcher.groupEquals(Key.DEFAULT_GROUP);
			Set<JobKey> keys = timer.getJobKeys(matcher);
			if (keys != null) {
				for (JobKey key : keys) {
					tasks.add(getSchedulerTask(classOfT, timer.getJobDetail(key)));
				}
			}
		} catch (SchedulerException e) {
			logger.warn("Get tasks of group[" + (group == null ? "default" : group) + "] from Scheduler[" + name + "] failed, caused by:", e);
		}
		
		return tasks;
	}
	
	/**
	 * 暂时保留，以向下兼容，应该使用{@link #getSchedulerTasks(Class, String)}代替
	 * @deprecated
	 * @return
	 */
	public Map<String, SchedulerTask> getScheduleTaskList() {
		Map<String, SchedulerTask> list = new HashMap<String, SchedulerTask>();
		try {
			List<String> groups = timer.getJobGroupNames();
			for (String group : groups) {
				List<SchedulerTask> tasks = getSchedulerTasks(SchedulerTask.class, group);
				for (SchedulerTask task : tasks) {
					list.put(task.getName(), task);
				}
			}
		} catch (SchedulerException e) {
		}
		
		return list;
	}
	
	protected Trigger getTrigger(String name, String group) {
		if (name != null) {
			TriggerKey key = (group != null && group.trim().length() > 0) ? new TriggerKey(TriggerFactory.getTriggerName(name), group) : new TriggerKey(TriggerFactory.getTriggerName(name));
			try {
				Trigger trigger = timer.getTrigger(key);
				return trigger;
			} catch (SchedulerException e) {
				logger.warn("Fetching Trigger of Task[" + name + ", " + key.getGroup() + "] from Scheduler[" + this.name + "] failed, caused by: ", e);
			}
		}
		return null;
	}
	
	/**
	 * 获得默认分组中一个调度任务的下一次运行时间<br/>
	 * 如果任务不会再运行则返回null
	 * @param name 任务名称
	 * @return 
	 */
	public Date getNextFireTime(String name) {
		return getNextFireTime(name, null);
	}
	
	/**
	 * 获得指定分组中一个调度任务的下一次运行时间
	 * @param name 任务名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #getNextFireTime(String)}
	 * @return
	 */
	public Date getNextFireTime(String name, String group) {
		Trigger trigger = getTrigger(name, group);
		
		return trigger == null ? null : trigger.getNextFireTime();
	}
	
	/**
	 * 判断默认分组中一个调度任务是否为永远重复运行的
	 * @param name 任务名称
	 * @return 如果任务并不存在与调度器中，则返回false
	 */
	public boolean isRepeatedForever(String name) {
		return isRepeatedForever(name, null);
	}
	
	/**
	 * 判断指定分组中一个调度任务是否为永远重复运行的
	 * @param name 任务名称
	 * @param group 分组名称，如果为null或空字符串，则作用等同于{@link #isRepeatedForever(String)}
	 * @return
	 */
	public boolean isRepeatedForever(String name, String group) {
		Trigger trigger = getTrigger(name, group);
		return trigger == null ? false : (trigger.getFinalFireTime() == null);
	}

	// 需要重复执行的任务再次被加载
	@Deprecated
	private void reschedule(SchedulerTask schedulerTask,
			ScheduleIterator iterator)
	{
		Date time = iterator.next();
		// 不需要再次执行，删除该调度任务
		if (time == null)
		{
			removeSchedulerTask(schedulerTask.name);
		}
		else
		{
			synchronized (schedulerTask.lock)
			{
				if (schedulerTask.state != SchedulerTask.State.CANCELLED)
				{
					SchedulerTimerTask timerTask = new SchedulerTimerTask(
							schedulerTask, iterator);
//					timer.schedule(timerTask, time);
				}
			}
		}
	}
	
	@Override
	public void init(NutConfig config) {
		//do nothing
	}

	@Override
	public void destroy(NutConfig config) {
		for (Scheduler scheduler : schedulers) {
			if (logger.isDebugEnabled())
				logger.debug("Destroying Scheduler[" + scheduler.name + "]...");

			scheduler.shutdown();
			
			if (logger.isDebugEnabled())
				logger.debug("Destroyed Scheduler[" + scheduler.name + "]");
		}
		
		try {
			Thread.sleep(1000); //Sleep for 1 second to let web server release cpu and make Quartz kill all threads
		} catch (InterruptedException e) {
			if (logger.isDebugEnabled())
				logger.debug(e);
		}
	}

}