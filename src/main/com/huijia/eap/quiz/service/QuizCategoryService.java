package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.QuizCategoryDao;
import com.huijia.eap.quiz.data.QuizCategory;

@IocBean
public class QuizCategoryService extends TblIdsEntityService<QuizCategory> {

	@Inject("refer:quizCategoryDao")
	public void setQuizCategoryDao(Dao dao) {
		setDao(dao);
	}

	public QuizCategory insert(QuizCategory quizCategory) {
		//quizCategory.setId((int) (getTblMaxIdWithUpdate() + quizCategory.getId()));
		return this.dao().insert(quizCategory);
	}

	public int getMaxId() {
		return (int) getTblMaxId() + 1;
	}
	
	public void updateMaxId(){
		updateTblMaxId();
	}

	public List<QuizCategory> fetchAll() {
		return super.query(null, null);
	}

	public void deleteByQuizId(long quizId) {
		((QuizCategoryDao) this.dao()).deleteByQuizId(quizId);
	}
	
	public List<QuizCategory> getByQuizId(long quizId){
		return super.query(Cnd.where("quizId", "=", quizId), null);
	}
	
	/**
	 * 获取总分的categoryId
	 * @param quizId
	 * @return
	 */
	public QuizCategory getTotalCategory(long quizId){
		List<QuizCategory> list = super.query(Cnd.where("quizId", "=", quizId)
				.and("name", "=", "总分"), null);
		if(list ==null || list.size() == 0) return null;
		return list.get(0);
	}
	/**
	 * 获取维度列表（剔除总分）
	 * @param quizId
	 * @return
	 */
	public List<QuizCategory> getCategoryList(long quizId){
		List<QuizCategory> list = super.query(Cnd.where("quizId", "=", quizId)
				.and("name", "<>", "总分"), null);
		return list;
	}
	
	/**
	 * 根据父维度id获取维度列表
	 * @param quizId
	 * @param parentLevel
	 * @return
	 */
	public List<QuizCategory> getBypLevel(long quizId, int parentLevel){
		List<QuizCategory> list = super.query(Cnd.where("quizId", "=", quizId).and("parentid", "=", parentLevel)
				.and("name", "<>", "总分"), null);
		return list;
	}
	
}