package com.newland.boss.cloud.web.balanceLibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseDao {

	private static final Logger LOGGER = LogManager.getLogger(BaseDao.class);
	private static final String SQL_EXCEPTION = "SQLException ";

	/**
	 * 关闭资源
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	protected void closeResource(Connection conn, Statement ps) {
		closeResource(ps);
		closeResource(conn);
	}

	/**
	 * 关闭资源
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	protected void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
		closeResource(rs);
		closeResource(ps);
		closeResource(conn);
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 */
	protected void closeResource(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error(SQL_EXCEPTION, e);
			}
		}
	}

	/**
	 * 关闭对象
	 * 
	 * @param ps
	 */
	protected void closeResource(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				LOGGER.error(SQL_EXCEPTION, e);
			}
		}
	}

	/**
	 * 关闭预处理对象
	 * 
	 * @param ps
	 */
	protected void closeResource(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				LOGGER.error(SQL_EXCEPTION, e);
			}
		}
	}

	/**
	 * 关闭结果集指针
	 * 
	 * @param rs
	 */
	private void closeResource(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.error(SQL_EXCEPTION, e);
			}
		}
	}
}
