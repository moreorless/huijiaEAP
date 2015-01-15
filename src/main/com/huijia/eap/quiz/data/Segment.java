package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("segment")
public class Segment {

	@Column
	@Id(auto=false)
	private long id;
	
	
	
}
