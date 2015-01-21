package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizItemDao;
import com.huijia.eap.quiz.data.QuizItem;

@IocBean
public class QuizItemService extends TblIdsEntityService<QuizItem> {

	@Inject("refer:quizItemDao")
	public void setQuizItemDao(Dao dao) {
		setDao(dao);
	}

	public QuizItem insert(QuizItem quizItem) {
		quizItem.setId(getTblMaxIdWithUpdate());
		return this.dao().insert(quizItem);
	}

	public void update(QuizItem quizItem) {
		this.dao().update(quizItem);
	}

	public List<QuizItem> fetchAll() {
		return super.query(null, null);
	}

	public List<QuizItem> fetchListByQuizId(long quizId) {
		return ((QuizItemDao) this.dao()).fetchListByQuizId(quizId);
	}
	
	public void deleteByQuizId(long quizId) {
		((QuizItemDao) this.dao()).deleteByQuizId(quizId);
	}

	/**
	 * 分页返回所有列表
	 */
	public Pager<QuizItem> paging(Condition condition, Pager<QuizItem> pager) {
		List<QuizItem> users = query(condition,
				this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);

		return pager;
	}

}
