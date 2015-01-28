package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.SegmentQuizRelationDao;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.SegmentQuizRelation;

@IocBean
public class SegmentQuizRelationService extends
		TblIdsEntityService<SegmentQuizRelation> {

	@Inject("refer:segmentQuizRelationDao")
	public void setSegmentQuizRelationDao(Dao dao) {
		setDao(dao);
	}

	public SegmentQuizRelation insert(SegmentQuizRelation segmentQuizRelation) {
		return this.dao().insert(segmentQuizRelation);
	}

	public List<SegmentQuizRelation> fetchListBySegmentId(long segmentId) {
		return ((SegmentQuizRelationDao) this.dao())
				.fetchListBySegmentId(segmentId);
	}

	public void deleteByQuizId(long quizId) {
		((SegmentQuizRelationDao) this.dao()).deleteByQuizId(quizId);
	}

	public void deleteBySegmentId(long segmentId) {
		((SegmentQuizRelationDao) this.dao()).deleteBySegmentId(segmentId);
	}

}
