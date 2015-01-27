package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.Segment;

@IocBean(name = "segmentDao", fields = { "dataSource" })
public class SegmentDao extends NutDao {

	public long getMaxEndIdByCompanyId(long companyId) {
		List<Segment> list = (List<Segment>) this.query(Segment.class,
				Cnd.where("companyid", "=", companyId));

		long result = 0;
		for (Segment segment : list) {
			if (segment.getEndId() > result)
				result = segment.getEndId();
		}
		return result;
	}

	public void deleteBySegmentId(long segmentId) {
		this.clear(Segment.class, Cnd.where("id", "=", segmentId));
	}
}
