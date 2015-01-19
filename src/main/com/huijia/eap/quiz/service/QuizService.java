package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizItem;

@IocBean
public class QuizService extends TblIdsEntityService<Quiz>{
	
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
	 * 生成模拟数据
	 * @return
	 */
	public Quiz genSampleQuiz(){
		Quiz quiz = new Quiz();
		quiz.setId(1);
		quiz.setIcon("default.png");
		quiz.setName("员工情绪倾向测评");
		quiz.setDescription("本测评主要帮助您了解自己在心理健康各个维度上的表现状况。"
				+ "分为六个维度：积极心态维度、情绪管理维度、行为表现维度、生理症状维度、社会支持维度和自我防御维度");

		// 维度
		String[] categorys = new String[]{
				"积极心态维度", "情绪管理维度", "行为表现维度", "生理症状维度", "社会支持维度", "自我防御维度"
		};
		
		// TODO: 添加评分体系
		
		// 添加题目
		for(int i = 0; i < 40; i++){
			QuizItem quizItem = new QuizItem();
			quizItem.setId(i);
			//quizItem.setCategory(categorys[i%6]);
			quizItem.setLieFlag(false);
			quizItem.setQuestion("命格相同的人不一定会有同样的结果，因为环境和自我改进的差异。");
			quizItem.addOption("A", "非常不符合", 6);
			quizItem.addOption("B", "有点不符合", 7);
			quizItem.addOption("C", "不确定", 8);
			quizItem.addOption("D", "比较符合", 9);
			quizItem.addOption("E", "非常符合", 10);
		}
		
		return quiz;
	}
	
	
}
