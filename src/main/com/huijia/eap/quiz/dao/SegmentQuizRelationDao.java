package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.SegmentQuizRelation;

@IocBean(name = "segmentQuizRelationDao", fields = { "dataSource" })
public class SegmentQuizRelationDao extends NutDao {

	public List<SegmentQuizRelation> fetchListBySegmentId(long segmentId) {
		return this.query(SegmentQuizRelation.class,
				Cnd.where("segmentid", "=", segmentId));
	}

	public void deleteByQuizId(long quizId) {
		this.clear(SegmentQuizRelation.class, Cnd.where("quizid", "=", quizId));
	}

	public void deleteBySegmentId(long segmentId) {
		this.clear(SegmentQuizRelation.class,
				Cnd.where("segmentid", "=", segmentId));
	}

}
