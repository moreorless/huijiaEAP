package com.huijia.eap.schedule;

import java.io.Serializable;

import org.quartz.JobExecutionContext;

/**
 * 定时调度任务抽象类，所有的调度任务需要继续这个类
 * 调度任务抽象类，所有的调度任务需继承自此类
 */
public abstract class SchedulerTask implements Serializable {
	
	private static final long serialVersionUID = -2133372212759997113L;

	public static enum State {
		/** 初始状态，准备中 */VIRGIN, /** 等待调度 */SCHEDULED, /** 已暂停 */ PAUSED, /** 已取消 */ CANCELLED, /** 正在运行 */ RUNNING, /** 已结束 */ FINISHED, /** 等待下次运行 */ WAITING
	}
	
	protected String name;// 此调度任务的名称，调度器Scheduler依据此名称定位调度任务，一个Scheduler中不允许加入同名的调度任务
	final Byte lock = 0;

//	int state = VIRGIN;
//	public static final int VIRGIN = 0;
//	public static final int SCHEDULED = 1;
//	public static final int CANCELLED = 2;
	
	State state = State.VIRGIN;

	protected Boolean active = Boolean.FALSE;

	/**
	 * 构造函数，创建一个调度任务
	 */
	public SchedulerTask(String name) {
		this.name = name;
	}

	/**
	 * 该调度任务被调度器调用时所要执行的方法
	 */
	public abstract void run();
	
	public void run(JobExecutionContext context) {
		run();
	}

	/**
	 * 撤销此调度任务
	 * 
	 * @return 返回执行结果，true执行成功，false执行失败
	 */
	public abstract boolean cancel();

	public String getName() {
		return name;
	}

	public synchronized boolean isActive() {
		return active;
	}
	
	public State getState() {
		return this.state;
	}

}