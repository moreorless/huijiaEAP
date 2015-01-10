/**
 
 * Author: liunan
 * Created: 2011-11-25
 */
package com.huijia.eap.schedule;

import java.util.Calendar;
import java.util.Date;

import org.quartz.DateBuilder;

public class CronScheduleIterator implements ScheduleIterator {
	
	private Calendar start;
	private String expression;

	public CronScheduleIterator(Calendar start, String expression) {
		this.start = start;
		this.expression = expression;
	}
	
	public static final CronScheduleIterator dailyAtHourAndMinute (int hour, int minute) {
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);

        String cronExpression = String.format("0 %d %d ? * *", minute, hour);
		Calendar start = Calendar.getInstance();
		start.set(Calendar.MINUTE, minute);
		start.set(Calendar.HOUR_OF_DAY, hour);
		return new CronScheduleIterator(start, cronExpression);
	}
	
	public static final CronScheduleIterator weeklyOnDayAndHourAndMinute(int dayOfWeek, int hour, int minute) {
		DateBuilder.validateDayOfWeek(dayOfWeek);
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);
		
        String cronExpression = String.format("0 %d %d ? * %d", minute, hour, dayOfWeek);
        Calendar start = Calendar.getInstance();
		start.set(Calendar.MINUTE, minute);
		start.set(Calendar.HOUR_OF_DAY, hour);
		start.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return new CronScheduleIterator(start, cronExpression);
	}
	
	public static final CronScheduleIterator monthlyOnDayAndHourAndMinute(int dayOfMonth, int hour, int minute) {
        DateBuilder.validateDayOfMonth(dayOfMonth);
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);

        String cronExpression = String.format("0 %d %d %d * ?", minute, hour, dayOfMonth);
		Calendar start = Calendar.getInstance();
		start.set(Calendar.MINUTE, minute);
		start.set(Calendar.HOUR_OF_DAY, hour);
		start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return new CronScheduleIterator(start, cronExpression);
	}
	
	public static final CronScheduleIterator quarterlyOnDayAndHourAndMinute(int dayOfMonth, int hour, int minute) {
        DateBuilder.validateDayOfMonth(dayOfMonth);
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);

		Calendar start = Calendar.getInstance();
		start.set(Calendar.MINUTE, minute);
		start.set(Calendar.HOUR_OF_DAY, hour);
		start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String cronExpression = String.format("0 %d %d %d %d/3 ?", minute, hour, dayOfMonth, start.get(Calendar.MONTH + 1));
		return new CronScheduleIterator(start, cronExpression);
	}
	
	public static final CronScheduleIterator yearlyOnDayAndHourAndMinute(int dayOfMonth, int hour, int minute) {
        DateBuilder.validateDayOfMonth(dayOfMonth);
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);

		Calendar start = Calendar.getInstance();
		start.set(Calendar.MINUTE, minute);
		start.set(Calendar.HOUR_OF_DAY, hour);
		start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String cronExpression = String.format("0 %d %d %d %d *", minute, hour, dayOfMonth, start.get(Calendar.MONTH + 1));
		return new CronScheduleIterator(start, cronExpression);
	}

	@Override
	public Date next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getFirstTime() {
		return start.getTime();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
