package com.newland.boss.cloud.web.balanceLibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.util.DBPoolConnection;

public class SQLDMLDao extends BaseDao {

	private static final Logger LOGGER = LogManager.getLogger(SQLDMLDao.class);
	private static final String SQL_EXCEPTION = "SQLException | JsonProcessingException ";
	private static final String CONNECTION = "DB_NAME";
	private static final String RESULT_DESC = "RESULT_DESC";

	
	public String queryPoolResult(String sql, Integer threadNumber) throws SQLException {
		LOGGER.info("查询线程:" + threadNumber + ",querySql:" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONArray array = new JSONArray();
		try {
			conn = DBPoolConnection.getConnection(threadNumber);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 获取列数
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				JSONObject jsonObj = new JSONObject(new LinkedHashMap());
				// 遍历每一列
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					jsonObj.put(columnName, value != null ? value : "");
				}
				array.add(jsonObj);
			}
		} catch (Exception e) {
			LOGGER.error("查询数据库失败", e.getMessage());
		} finally {
			closeResource(conn, ps, rs);
		}

		if (array.isEmpty()) {
			return null;
		}
		return array.toString();
	}

	/**
	 * 根据SQL语句得出相应的结果
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> sqlQueryList(String querySql, Integer threadNumber) throws SQLException {

		LOGGER.info("查询线程:" + threadNumber + ",querySql:" + querySql);
		String sql = querySql;
		// 表的列名称以{"列名":"字段类型"}储存
		Map<String, String> tableColumnNameMap = new LinkedHashMap<>();
		List<String> dataLists = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DBPoolConnection.getConnection(threadNumber);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 查出列名称
			ResultSetMetaData data = rs.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				tableColumnNameMap.put(data.getColumnName(i), data.getColumnTypeName(i));
			}
			while (rs.next()) {
				Map<String, String> rowList = new LinkedHashMap<>();
				rowList.put(CONNECTION, DBPoolConnection.getDBNameList(threadNumber));
				for (Entry<String, String> m : tableColumnNameMap.entrySet()) {
					if (m.getValue().equals("DATE")) {
						rowList.put(m.getKey(), rs.getString(m.getKey()).replaceAll("\\.0", ""));
						continue;
					}
					rowList.put(m.getKey(), rs.getString(m.getKey()));
				}
				dataLists.add(JacksonHelper.writeBean2Json(rowList));
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error(SQL_EXCEPTION, e);
			throw new SQLException(e.getMessage());
		} finally {
			closeResource(conn, ps, rs);
		}
		return dataLists;
	}

	/**
	 * 根据SQL语句删除对应数据
	 * 
	 * @throws SQLException
	 */
	public List<String> deleteSQL(String querySql, Integer threadNumber) throws SQLException {
		LOGGER.info("删除线程:" + threadNumber + ",delSql:" + querySql);
		List<String> dataLists = new ArrayList<>();
		Map<String, String> rowList = new LinkedHashMap<>();
		rowList.put(CONNECTION, DBPoolConnection.getDBNameList(threadNumber));
		String sql = querySql;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBPoolConnection.getConnection(threadNumber);
			ps = conn.prepareStatement(sql);
			int counter = ps.executeUpdate();
			if (counter > 0) {
				rowList.put(RESULT_DESC, "已删除" + counter + "条记录");
			} else {
				rowList.put(RESULT_DESC, "未查到可以删除的记录");
			}
			dataLists.add(JacksonHelper.writeBean2Json(rowList));
			return dataLists;
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error(SQL_EXCEPTION, e);
			throw new SQLException(e.getMessage());
		} finally {
			closeResource(conn, ps, null);
		}
	}

	/**
	 * 根据SQL语句更新
	 */
	public List<String> updateSQL(String querySql, Integer threadNumber) throws SQLException {
		LOGGER.info("更新线程:" + threadNumber + ",updSql:" + querySql);
		List<String> dataLists = new ArrayList<>();
		Map<String, String> rowList = new LinkedHashMap<>();
		rowList.put(CONNECTION, DBPoolConnection.getDBNameList(threadNumber));

		String sql = querySql;

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBPoolConnection.getConnection(threadNumber);
			ps = conn.prepareStatement(sql);
			int counter = ps.executeUpdate();
			if (counter > 0) {
				rowList.put(RESULT_DESC, "已更新" + counter + "条记录");
			} else {
				rowList.put(RESULT_DESC, "未查到可以更新的记录");
			}
			dataLists.add(JacksonHelper.writeBean2Json(rowList));
			return dataLists;
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error(SQL_EXCEPTION, e);
			throw new SQLException(e.getMessage());
		} finally {
			closeResource(conn, ps, null);
		}
	}

	/**
	 * 批量插入数据
	 */
	public List<String> insertSQL(String[] querySql, Integer threadNumber) throws SQLException {
		LOGGER.info("批量插入条数------" + querySql.length);
		List<String> dataLists = new ArrayList<>();
		Map<String, String> rowList = new LinkedHashMap<>();
		rowList.put(CONNECTION, DBPoolConnection.getDBNameList(threadNumber));

		Connection conn = null;
		Statement ps = null;
		try {
			conn = DBPoolConnection.getConnection(threadNumber);
			ps = conn.createStatement();
			// 设置手动提交
			conn.setAutoCommit(false);

			for (String sql : querySql) {
				// 添加到批次
				ps.addBatch(sql);
			}
			ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);

			rowList.put(RESULT_DESC, "已插入" + querySql.length + "条记录");
			dataLists.add(JacksonHelper.writeBean2Json(rowList));
			return dataLists;
		} catch (SQLException | JsonProcessingException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			LOGGER.error(SQL_EXCEPTION, e);
			throw new SQLException(e.getMessage());
		} finally {
			closeResource(conn, ps);
		}
	}

}
