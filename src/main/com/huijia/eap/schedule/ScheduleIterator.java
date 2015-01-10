package com.huijia.eap.schedule;

import java.util.Date;

/**
 * 定义了调度任务时间迭代器接口，时间迭代器用于定义调度任务的执行时间、次数和执行的频率
 * @author liunan
 */
public interface ScheduleIterator
{

	/**
	 * 返回调度任务下一次执行的时间
	 * @return 返回下一次执行的时间
	 */
	public Date next();
	
	/**
	 * 返回第一次执行的时间
	 * @return 返回第一次执行的时间
	 */
	public Date getFirstTime();
}
