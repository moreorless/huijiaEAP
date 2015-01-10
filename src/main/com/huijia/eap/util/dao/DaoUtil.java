package com.huijia.eap.util.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.nutz.ioc.Ioc;


public class DaoUtil {
	
	private Ioc ioc;
	
	private static DaoUtil me = new DaoUtil();
	
	private DaoUtil() {}
	
	public final static DaoUtil me() {
		return me;
	}
	
	public void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}
	
	public DataSource getDataSource() {
		return ioc.get(DataSource.class, "dataSource");
	}
	
	/**
	 * 获取本地数据库连接
	 * @return
	 */
	public Connection getConnection() {
		DataSource ds = getDataSource();
		Connection conn = null;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;		
	}
	
	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
}