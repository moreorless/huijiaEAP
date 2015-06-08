package com.huijia.eap.quiz.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.dao.QuizDao;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.SegmentQuizRelation;

@IocBean
public class QuizService extends TblIdsEntityService<Quiz> {

	@Inject
	private QuizItemService quizItemService;

	@Inject
	private QuizEvaluationService quizEvaluationService;

	@Inject
	private SegmentQuizRelationService segmentQuizRelationService;

	@Inject
	private QuizResultService quizResultService;

	@Inject
	private QuizAnswerLogService quizAnswerLogService;

	@Inject
	private QuizCategoryService quizCategoryService;

	@Inject("refer:quizDao")
	public void setQuizDao(Dao dao) {
		setDao(dao);
	}

	public Quiz insert(Quiz quiz) {
		quiz.setId(getTblMaxIdWithUpdate() + 1);
		return this.dao().insert(quiz);
	}

	public void update(Quiz quiz) {
		this.dao().update(quiz);
	}

	public List<Quiz> fetchAll() {
		return super.query(null, null);
	}

	public void deleteByQuizId(long id) {
		Quiz quiz = QuizCache.me().getQuiz(id);
		if (quiz == null)
			return;
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			Iterator<Quiz> iter = quiz.getChildList().iterator();
			while (iter.hasNext()) {
				Quiz _quiz = iter.next();
				deleteByQuizId(_quiz.getId());
			}
		}

		// Table: quiz_category
		quizCategoryService.deleteByQuizId(id);
		// Table: quiz_item
		quizItemService.deleteByQuizId(id);
		// Table: quiz_evaluation
		quizEvaluationService.deleteByQuizId(id);
		// Table: seg_quiz_relation
		segmentQuizRelationService.deleteByQuizId(id);
		// Table: quiz_result
		quizResultService.deleteByQuizId(id);
		// Table: quiz_answer_log
		quizAnswerLogService.deleteByQuizId(id);
		// Table: quiz
		this.delete(id);

		// 从缓存中删除
		QuizCache.me().delete(id);
	}

	/**
	 * 根据tag获取试卷
	 * 
	 * @param tag
	 * @return
	 */
	public Quiz getQuizByTag(String tag) {
		List<Quiz> list = super.query(Cnd.where("tag", "=", tag), null);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 显示多有可见的试卷 (独立试卷、复合试卷)
	 * 
	 * @return
	 */
	public List<Quiz> fetchDisplayQuizs() {
		return super.query(
				Cnd.where("type", "=", QuizConstant.QUIZ_TYPE_STANDALONE).or(
						"type", "=", QuizConstant.QUIZ_TYPE_PARENT), null);
	}

	public List<Quiz> fetchListByParentId(long parentId) {
		return ((QuizDao) this.dao()).fetchListByParentId(parentId);
	}

	public List<Quiz> fetchQuizListBySegmentId(long segmentId) {
		LinkedList<Quiz> quizList = new LinkedList<Quiz>();
		List<SegmentQuizRelation> list = segmentQuizRelationService
				.fetchListBySegmentId(segmentId);

		for (SegmentQuizRelation r : list) {
			Quiz q = this.fetch(r.getQuizId());
			quizList.add(q);
		}
		for (Quiz q : quizList) {
			q.setUserCountFinished(quizResultService.userFinishedCount(
					segmentId, q.getId()));
		}
		return quizList;
	}

	public void deleteByParentId(long parentId) {
		((QuizDao) this.dao()).deleteByParentId(parentId);
	}

	/**
	 * 分页返回所有列表
	 */
	public Pager<Quiz> paging(Condition condition, Pager<Quiz> pager) {
		List<Quiz> users = query(condition,
				this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);

		return pager;
	}

	/**
	 * 获取试卷的全部信息，包含题目、题目的选项
	 * 
	 * @param id
	 * @return
	 */
	public Quiz fetchFullQuiz(long id) {
		Quiz quiz = this.fetch(id);

		if (quiz == null)
			return null;
		// 如果是复合试卷，递归获取子试卷
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			List<Quiz> children = this.fetchListByParentId(id);
			for (Quiz _quiz : children) {
				quiz.getChildList().add(this.fetchFullQuiz(_quiz.getId()));
			}
		}

		// 获取题目
		List<QuizItem> itemList = quizItemService.getItemsByQuizId(id);
		quiz.setItems(itemList);

		// 获取评分标准
		quiz.setEvaluations(quizEvaluationService.fetchListByQuizId(id));

		return quiz;
	}

}
