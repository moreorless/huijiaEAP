package com.huijia.eap.quiz.service;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.SegmentDao;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.data.SegmentQuizRelation;

@IocBean
public class SegmentService extends TblIdsEntityService<Segment> {

	private static final long MAXSEGMENTSIZE = 9999999;

	@Inject("refer:segmentDao")
	public void setSegmentDao(Dao dao) {
		setDao(dao);
	}

	@Inject
	private CompanyService companyService;

	@Inject
	private UserService userService;

	@Inject
	private QuizService quizService;

	@Inject
	private SegmentQuizRelationService segmentQuizRelationService;

	/**
	 * 采用最简单的方法分配号段 即寻找当前企业号段中的最大值，在此基础上继续分配，剩余空间不够的话返回错误
	 * 
	 * @return -1表示分配失败;成功返回新的startId
	 * 
	 */
	private long simpleAllocateSegment(long companyId, long size) {
		long currentEndId = getMaxEndIdByCompanyId(companyId);
		if (MAXSEGMENTSIZE < currentEndId + size) {
			return -1;
		}
		return currentEndId + 1;
	}

	/**
	 * 采用链表方法分配号段 STEP 1， 链表法排序。取出当前企业的所有号段信息，采用链表法，依次将各个号段的起始结束位置插入链表 STEP 2.
	 * 遍历链表，寻找合适的其实位置，寻找失败返回错误
	 * 
	 * @return -1表示分配失败;成功返回新的startId
	 * 
	 */
	private long linkAllocateSegment(long companyId, long size) {

		// 该方法 暂未实现
		return -1;
	}

	public Segment fetchSegmentById(long id) {
		Segment segment = this.fetch(id);

		segment.setMyQuizList((LinkedList<Quiz>) quizService
				.fetchQuizListBySegmentId(id));

		return segment;
	}

	/**
	 * 生成用户名
	 * 
	 * @param segment
	 * @return
	 */
	private String generageUserName(long companyId, long startId, long offset) {
		Company company = companyService.fetch(companyId);
		return company.getCode() + "N" + startId + offset;
	}

	public Segment insert(Segment segment) {
		segment.setId(getTblMaxIdWithUpdate());
		/*
		 * 1-4位表示企业  第5位表示身份：S代表高级用户，N代表普通该用户  第6至12字段是用户号：用户号段可以重复使用
		 * ，且可回收，全用数字，不用字母
		 */
		long startId = simpleAllocateSegment(segment.getCompanyId(),
				segment.getSize());
		if (startId == -1) {
			startId = linkAllocateSegment(segment.getCompanyId(),
					segment.getSize());
			if (startId == -1)
				return null;
		}
		segment.setStartId(startId);
		segment.setEndId(startId + segment.getSize());
		segment = this.dao().insert(segment);

		for (int i = 0; i < segment.getSize(); i++) {
			User user = new User();
			String name = generageUserName(segment.getCompanyId(),
					segment.getStartId(), i);
			user.setName(name);
			user.setPassword(segment.getInitPassword());
			user.setCompanyId(segment.getCompanyId());
			user.setSegmentId(segment.getId());
			userService.insert(user);
		}
		return segment;
	}

	public void update(Segment segment) {
		this.dao().update(segment);
	}

	public void deleteBySegmentId(long segmentId) {
		userService.deleteBySegmentId(segmentId);
		((SegmentDao) this.dao()).deleteBySegmentId(segmentId);
	}

	/**
	 * 分页返回所有用户列表
	 */
	public Pager<Segment> paging(Condition condition, Pager<Segment> pager) {
		List<Segment> users = query(condition,
				this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);

		return pager;
	}

	public long getMaxEndIdByCompanyId(long companyId) {

		return ((SegmentDao) this.dao()).getMaxEndIdByCompanyId(companyId);
	}

}