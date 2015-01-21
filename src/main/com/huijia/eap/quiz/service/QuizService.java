package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizItemDao;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizItem;

@IocBean
public class QuizService extends TblIdsEntityService<Quiz>{
	
	@Inject
	private QuizItemService quizItemService;
	
	@Inject
	private QuizEvaluationService quizEvaluationService;
	
	@Inject("refer:quizDao")
	public void setQuizDao(Dao dao) {
		setDao(dao);
	}
	
	public Quiz insert(Quiz quiz) {
		quiz.setId(getTblMaxIdWithUpdate());
		return this.dao().insert(quiz);
	}
	
	public void update(Quiz quiz) {
		this.dao().update(quiz);
	}
	
	public List<Quiz> fetchAll(){
		return super.query(null, null);
	}
	
	/**
	 * 分页返回所有列表
	 */
	public Pager<Quiz> paging(Condition condition, Pager<Quiz> pager) {
		List<Quiz> users = query(condition, this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);
		
		return pager;
	}
	
	/**
	 * 获取试卷的全部信息，包含题目、题目的选项
	 * @param id
	 * @return
	 */
	public Quiz fetchFullQuiz(long id){
		Quiz quiz = this.fetch(id);
		
		/**
		 * 获取题目
		 */
		List<QuizItem> itemList = quizItemService.getItemsByQuizId(id);
		quiz.setItems(itemList);
		
		/**
		 * 获取评分标准
		 */
		quiz.setEvaluations(quizEvaluationService.fetchListByQuizId(id));
		
		return quiz;
	}
	
}
