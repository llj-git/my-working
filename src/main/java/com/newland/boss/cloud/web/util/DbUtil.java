package com.newland.boss.cloud.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 数据库工具类
 * 
 * @author ZJUNE
 *
 */
public class DbUtil {

	private static final Logger LOGGER = LogManager.getLogger(DbUtil.class);
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 */
	public static ResultSet executeQuery(String sql, Object... objs) {
		try {
			conn = DBPoolConnection.getConnection();
		} catch (SQLException e) {
			LOGGER.error("获取连接失败", e);
		}

		try {
			ps = conn.prepareStatement(sql);
			for (int k = 0; k < objs.length; k++) {
				ps.setObject(k + 1, objs[k]);
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			LOGGER.error("查询失败", e);
		}
		return rs;
	}

	/**
	 * 修改
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 */
	public static int executeUpdate(String sql, Object... obj) {
		int cnt = 0;
		try {
			conn = DBPoolConnection.getConnection();
		} catch (SQLException e) {
			LOGGER.error("获取连接失败", e);
		}

		try {
			ps = conn.prepareStatement(sql);
			for (int k = 0; k < obj.length; k++) {
				ps.setObject(k + 1, obj[k]);
			}
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("executeUpdate失败", e);
		}
		return cnt;
	}

	public static int executeUpdate(Connection connection, String sql, Object... obj) {
		int cnt = 0;
		try {
			ps = connection.prepareStatement(sql);
			for (int k = 0; k < obj.length; k++) {
				ps.setObject(k + 1, obj[k]);
			}
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("executeUpdate失败", e);
		}
		return cnt;
	}

	public static ResultSet executeQuery(Connection connection, String sql, Object... objs) {
		try {

			PreparedStatement ps = connection.prepareStatement(sql);
			for (int k = 0; k < objs.length; k++) {
				ps.setObject(k + 1, objs[k]);
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			LOGGER.error("查询失败", e);
		}
		return rs;
	}

}
