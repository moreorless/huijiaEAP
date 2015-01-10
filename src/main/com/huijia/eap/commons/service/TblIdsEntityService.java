package com.huijia.eap.commons.service;

import org.nutz.service.IdEntityService;

import com.huijia.eap.util.dao.TblMaxIdHelper;


public abstract class TblIdsEntityService<T> extends IdEntityService<T> {

	protected long getTblMaxId() {
		
		return TblMaxIdHelper.getTblMaxId(this.dao(), getEntity());

	}
	
	protected void updateTblMaxId() {
		
		TblMaxIdHelper.updateTblMaxId(this.dao(), getEntity());
		
	}
	
	/**
	 * 获取一个新的ID，并同时更新sys_tblids表中的记录
	 * @return 新的ID值
	 */
	protected long getTblMaxIdWithUpdate() {
		
		return TblMaxIdHelper.getTblMaxIdWithUpdate(this.dao(), getEntity());
		
	}
	
}
