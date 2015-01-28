package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Segment;

@IocBean
public class CompanyService extends TblIdsEntityService<Company> {

	@Inject
	private UserService userService;

	@Inject
	private UserTempService userTempService;

	@Inject
	private SegmentQuizRelationService segmentQuizRelationService;

	@Inject
	private SegmentService segmentService;

	@Inject("refer:companyDao")
	public void setCompanyDao(Dao dao) {
		setDao(dao);
	}

	public Company insert(Company company) {
		company.setId(getTblMaxIdWithUpdate());
		return this.dao().insert(company);
	}

	public void update(Company company) {
		this.dao().update(company);
	}

	public void deleteByCompanyId(long id) {

		List<Segment> list = segmentService.fetchListByCompanyId(id);

		for (Segment segment : list) {
			segmentService.deleteBySegmentId(segment.getId());
		}

		this.delete(id);
	}

	/**
	 * 分页返回所有用户列表
	 */
	public Pager<Company> paging(Condition condition, Pager<Company> pager) {
		List<Company> users = query(condition,
				this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);

		return pager;
	}

}
