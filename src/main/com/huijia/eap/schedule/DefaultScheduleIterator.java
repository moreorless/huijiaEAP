package com.huijia.eap.schedule;

import java.util.Calendar;
import java.util.Date;

/**
 * 缺省的调度任务的时间迭代器类，用于定义调度任务的开始时间，执行次数和执行频率
 * 
 */
public class DefaultScheduleIterator implements ScheduleIterator {
	private Calendar startTime;
	private Calendar nextTime; // 任务启动时间
	private int repeat; // 任务执行次数
	private int intervalUnit; // 每次执行间隔时间的类型，对应时间单位
	private int interval; // 每次执行间隔的时间值，单位由intervalUnit定义
	private int executed = 0; // 已经执行的次数

	/**
	 * 构造迭代器
	 * 
	 * @param nextTime
	 *            ,开始执行的时间
	 * @param intervalUnit
	 *            ,每次执行间隔时间的类型，对应时间单位
	 * @param interval,每次执行间隔的时间值，单位由intervalUnit定义
	 * @param repeat
	 *            ,此调度任务的执行次数，0为循环执行
	 */
	public DefaultScheduleIterator(Calendar startTime, int intervalUnit,
			int interval, int repeat) {
		this.startTime = startTime;
		this.nextTime = Calendar.getInstance();
		this.nextTime.setTime(startTime.getTime());
		this.intervalUnit = intervalUnit;
		this.interval = interval;
		this.repeat = repeat;
		if (repeat < 0) {
			this.repeat = 0;
		}
		/*
		// /Modified by Sunyh on 2006-1-5
		if (nextTime.getTime().before(new Date())) {
			if (repeat == 0 || interval == 0) {
				nextTime.setTime(new Date());
			} else {
				int duration = interval * repeat;
				Calendar temcalendar = Calendar.getInstance();
				temcalendar.setTimeInMillis(nextTime.getTimeInMillis());
				nextTime.add(intervalUnit, duration);
				if (nextTime.getTime().before(new Date())) {
					this.repeat = -1;
				} else {
					int sub1 = Calendar.getInstance().compareTo(temcalendar);
					int sub2 = nextTime.compareTo(Calendar.getInstance());
					this.repeat = (sub2 * repeat) / (sub1 + sub2);
					if (this.repeat == 0) {
						this.repeat = -1;
					}
				}
			}

		}*/
	}

	/**
	 * 返回下一次执行时间
	 * 
	 * @return,下次执行任务的时间
	 */
	public Date next() {
		if (repeat < 0) {
			return null;
		}
		// 如果不是循环执行并且执行次数达到预定次数，退出
		if (repeat != 0 && executed >= repeat) {
			return null;
		}

		// 计算下次执行的时间
		nextTime.add(this.intervalUnit, interval);
		if (repeat != 0) {
			executed++;
		}
		return nextTime.getTime();
	}

	/**
	 * 返回第一次执行时间
	 */
	public Date getFirstTime() {
		if (repeat == -1) {
			return null;
		}
		if (repeat != 0) {
			executed++;
		}
		return startTime.getTime();
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getIntervalUnit() {
		return intervalUnit;
	}

	public void setIntervalUnit(int intervalUnit) {
		this.intervalUnit = intervalUnit;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getExecuted() {
		return executed;
	}

	public void setExecuted(int executed) {
		this.executed = executed;
	}

}
