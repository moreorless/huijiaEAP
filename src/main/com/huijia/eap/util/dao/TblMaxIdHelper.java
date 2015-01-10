package com.huijia.eap.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Entity;

import com.huijia.eap.commons.data.TblIds;

/**
 * 无重复自增主键助手
 *
 */
public abstract class TblMaxIdHelper {
	private static Logger logger = Logger.getLogger(TblMaxIdHelper.class);
	
	private static Map<String, Object> lockMap = new HashMap<String, Object>();
	
	private static synchronized Object lock(String tblName) { //针对每一张表获取锁，由于表的数量是有限的，因此全部放在内存中可以承受
		Object lock = lockMap.get(tblName);
		if (lock == null) {
			lock = new Object();
			lockMap.put(tblName, lock);
		}
		
		return lock;
	}
	
	/**
	 * 获取可使用ID，不更新
	 * @param dao
	 * @param tblName 数据库表名
	 * @return
	 */
	public static final long getTblMaxId(Dao dao, String tblName) {
		if (tblName == null)
			return -1;
		
		synchronized (lock(tblName)) {
			TblIds tblid = dao.fetch(TblIds.class, tblName);
			if (tblid == null) {
				tblid = new TblIds();
				tblid.setName(tblName);
				tblid.setMaxid(1);
				dao.insert(tblid);
			}
				
			return tblid.getMaxid();
		}
	}
	
	/**
	 * 获取可使用ID，不更新
	 * @param dao
	 * @param entity 代表POJO与数据库表对应关系的Entity对象
	 * @return
	 */
	public static final long getTblMaxId(Dao dao, Entity<?> entity) {
		String tblName = entity.getTableName();
		return getTblMaxId(dao, tblName);
	}

	/**
	 * 获取可使用ID，不更新
	 * @param dao
	 * @param clazz 与数据库表对应的POJO类对象
	 * @return
	 */
	public static final long getTblMaxId(Dao dao, Class<?> clazz) {
		return getTblMaxId(dao, dao.getEntity(clazz));
	}
	
	/**
	 * 更新ID
	 * @param dao
	 * @param tblName 数据库表名
	 */
	public static final void updateTblMaxId(Dao dao, String tblName) {
		if (tblName == null)
			return;
		
		synchronized(lock(tblName)) {
			TblIds tblid = dao.fetch(TblIds.class, tblName);
			tblid.setMaxid(tblid.getMaxid() + 1);
			dao.update(tblid);
		}
	}
	
	/**
	 * 更新ID
	 * @param dao
	 * @param entity 代表POJO与数据库表对应关系的Entity对象
	 */
	public static final void updateTblMaxId(Dao dao, Entity<?> entity) {
		updateTblMaxId(dao, entity.getTableName());
	}
	
	/**
	 * 更新ID
	 * @param dao
	 * @param clazz 与数据库表对应的POJO类对象
	 */
	public static final void updateTblMaxId(Dao dao, Class<?> clazz) {
		updateTblMaxId(dao, dao.getEntity(clazz));
	}
	
	/**
	 * 获取可使用ID，同时更新
	 * @param dao
	 * @param tblName 数据库表名
	 * @return
	 */
	public static final long getTblMaxIdWithUpdate(Dao dao, String tblName) {
		if (tblName == null)
			return -1;
		
		synchronized(lock(tblName)) {
			TblIds tblid = dao.fetch(TblIds.class,tblName);
			boolean insert = false;
			if (tblid == null) {
				tblid = new TblIds();
				tblid.setName(tblName);
				tblid.setMaxid(1);
				insert = true;
			}
			long maxid = tblid.getMaxid();
			
			tblid.setMaxid(maxid + 1);
			if (insert)
				dao.insert(tblid);
			else
				dao.update(tblid);
			return maxid;
		}
	}

	/**
	 * 获取可使用ID，同时更新
	 * @param dao
	 * @param entity 代表POJO与数据库表对应关系的Entity对象
	 * @return
	 */
	public static final long getTblMaxIdWithUpdate(Dao dao, Entity<?> entity) {
		String tblName = entity.getTableName();
		return getTblMaxIdWithUpdate(dao, tblName);
	}
	
	/**
	 * 获取可使用ID，同时更新
	 * @param dao
	 * @param clazz 与数据库表对应的POJO类对象
	 * @return
	 */
	public static final long getTblMaxIdWithUpdate(Dao dao, Class<?> clazz) {
		return getTblMaxIdWithUpdate(dao, dao.getEntity(clazz));
	}
	
	/**
	 * 直接通过查询数据库,获取可使用ID,同时更新
	 * @param tableName 要获取的表名
	 * @return
	 */
	public static final long geTblMaxIdWithUpdate(String tableName){
		if (tableName == null)
			return -1;
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		synchronized (lock(tableName)) {
			try{
				conn = DaoUtil.me().getConnection();
				stmt = conn.prepareStatement("select maxid from sys_tblids where name = ?");
				stmt.setString(1, tableName);
				rs = stmt.executeQuery();
				if(rs.next()){
					long id = rs.getLong(1);
					stmt = conn.prepareStatement("update sys_tblids set maxid = ? where name = ?");
					stmt.setLong(1, id+1);
					stmt.setString(2, tableName);
					stmt.execute();
					return id;
				}else{
					stmt = conn.prepareStatement("insert into sys_tblids (name,maxid) values(2,?)");
					stmt.setString(1, tableName);
					stmt.execute();
					return 1;
				}
			}catch(Exception ex){
				logger.error(ex.getMessage(),ex);
			}finally{
				DaoUtil.me().close(conn);
			}
		}
		
		return 0;
	}
}
