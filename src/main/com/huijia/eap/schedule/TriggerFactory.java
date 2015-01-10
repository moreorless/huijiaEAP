package com.huijia.eap.schedule;

import java.text.ParseException;
import java.util.Calendar;

import org.quartz.CronScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public abstract class TriggerFactory {
	
	public static final String getTriggerName(String taskName) {
		return taskName == null ? null : taskName + "-trigger";
	}

	/**
	 * 创建触发器
	 * 
	 * <ul>
	 * <li>当使用DefaultScheduleIterator时，如果时间间隔为月和年，所生成的触发器将会不完全准确，月使用30天，年使用365天</li>
	 * <li>对按月和年运行，或更复杂运行方式的，建议使用CronScheduleIterator，借助强大的Cron表达式来实现</li>
	 * </ul>
	 * 
	 * @param iterator 如果为空或为未知的类型，则返回仅执行一次的触发器
	 * @return
	 */
	public static final Trigger buildTrigger(String taskName, String group, ScheduleIterator iterator) {
		
		TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger();
		if (group != null && group.trim().length() > 0)
			builder.withIdentity(getTriggerName(taskName), group);
		else
			builder.withIdentity(getTriggerName(taskName));
		
		if (iterator instanceof DefaultScheduleIterator) {
			DefaultScheduleIterator dsi = (DefaultScheduleIterator) iterator;
			
			SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule();
			if (dsi.getRepeat() == 0)
				schedule.repeatForever();
			else if (dsi.getRepeat() > 0)
				schedule.withRepeatCount(dsi.getRepeat() - 1);
			switch (dsi.getIntervalUnit()) {
			case Calendar.MILLISECOND:
				schedule.withIntervalInMilliseconds(dsi.getInterval());
				break;
			case Calendar.SECOND:
				schedule.withIntervalInSeconds(dsi.getInterval());
				break;
			case Calendar.MINUTE:
				schedule.withIntervalInMinutes(dsi.getInterval());
				break;
			case Calendar.HOUR:
			case Calendar.HOUR_OF_DAY:
				schedule.withIntervalInHours(dsi.getInterval());
				break;
			case Calendar.DAY_OF_MONTH:
			case Calendar.DAY_OF_WEEK:
			case Calendar.DAY_OF_WEEK_IN_MONTH:
			case Calendar.DAY_OF_YEAR:
				schedule.withIntervalInHours(dsi.getInterval() * 24);
				break;
			case Calendar.WEEK_OF_MONTH:
			case Calendar.WEEK_OF_YEAR:
				schedule.withIntervalInHours(dsi.getInterval() * 7 * 24);
				break;
			case Calendar.MONTH:
				schedule.withIntervalInHours(dsi.getInterval() * 24 * 30);
				break;
			case Calendar.YEAR:
				schedule.withIntervalInHours(dsi.getInterval() * 24 * 365);
				break;
			}
			
			return builder.startAt(dsi.getFirstTime()).withSchedule(schedule).build();
		} else if (iterator instanceof CronScheduleIterator) {
			CronScheduleIterator csi = (CronScheduleIterator) iterator;
			try {
				CronScheduleBuilder schedule = CronScheduleBuilder.cronSchedule(csi.getExpression());
				return builder.startAt(csi.getFirstTime()).withSchedule(schedule).build();
			} catch (ParseException e) {
			}
		}
		
		return builder.build();
	}
	
}
