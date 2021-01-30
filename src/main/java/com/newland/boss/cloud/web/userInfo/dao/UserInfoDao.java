package com.newland.boss.cloud.web.userInfo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.userInfo.entity.JdbcCfg;
import com.newland.boss.cloud.web.userInfo.entity.SyncTable;
import com.newland.boss.cloud.web.userInfo.entity.TableField;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.cloud.web.util.DbUtil;

public class UserInfoDao extends BaseDao {

	private static final Logger LOGGER = LogManager.getLogger(UserInfoDao.class);

	public JdbcCfg queryDBconfigByName(String name, Integer type) {
		JdbcCfg jdbcCfg = new JdbcCfg();
		String querySql = "select JDBC_URL,JDBC_USER,JDBC_PASSWORD,DRIVER_CLASS from alti_jdbc_cfg where type=? and DB_NAME=?";
		ResultSet rs = DbUtil.executeQuery(querySql, type, name);
		try {
			while (rs.next()) {
				jdbcCfg.setJdbcUrl(rs.getString("JDBC_URL"));
				jdbcCfg.setJdbcUser(rs.getString("JDBC_USER"));
				jdbcCfg.setJdbcPwd(rs.getString("JDBC_PASSWORD"));
				jdbcCfg.setDriverClass(rs.getString("DRIVER_CLASS"));
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return jdbcCfg;
	}

	public int updateData(String sql) throws Exception {
		LOGGER.info(sql);
		return DbUtil.executeUpdate(DBPoolConnection.getBillConnection(), sql);
	}

	public boolean addSyncData(String keyName, String keyValue, String tableName) throws Exception {
		String sql = "insert into special_user_sync(key_name,key_value,table_name,data_source,oper_date,insert_time)VALUES(?,?,?,?,to_number(to_char(SYSDATE,'yyyymmdd')),sysdate)";
		int i = DbUtil.executeUpdate(DBPoolConnection.getBillConnection(), sql, keyName, keyValue, tableName, 0);
		if (i > 0) {
			return true;
		}
		return false;
	}

	public String queryMainKey(String tableName) throws Exception {
		String sql = "select main_key from sync_table_key where table_name = ?";
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), sql, tableName);
		while (rs.next()) {
			return rs.getString("MAIN_KEY");
		}
		return null;
	}

	public String queryProductDesc(String productId, String userId) throws Exception {
		String sql = "select distinct b.DESCRIPTION from user_product a,ng_product_def b where b.product_id=? and user_id=?";
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), sql, productId, userId);
		while (rs.next()) {
			return rs.getString("DESCRIPTION");
		}
		return null;
	}

	/**
	 * 查询允许用户访问的表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SyncTable> loadAllSyncTable() throws Exception {
		String querySql = "select table_id,table_name,main_key from sync_table_key";
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), querySql);
		List<SyncTable> list = new ArrayList<>();
		try {
			SyncTable syncTable = null;
			while (rs.next()) {
				syncTable = new SyncTable();
				syncTable.setTableId(rs.getInt("TABLE_ID"));
				syncTable.setTableName(rs.getString("TABLE_NAME"));
				syncTable.setMainKey(rs.getString("MAIN_KEY"));
				list.add(syncTable);
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return list;
	}

	/**
	 * 
	 * 查询对应的数据 sync_table_key
	 * 
	 * @throws Exception
	 */
	public SyncTable querySyncTable(String tableName) throws Exception {
		String querySql = "select * from sync_table_key where table_name=?";
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), querySql, tableName);
		SyncTable syncTable = null;
		while (rs.next()) {
			syncTable = new SyncTable();
			syncTable.setTableId(rs.getInt("TABLE_ID"));
			syncTable.setTableName(rs.getString("TABLE_NAME"));
			syncTable.setMainKey(rs.getString("MAIN_KEY"));
			syncTable.setCondition(rs.getString("CONDITION"));
			syncTable.setConditionExt(rs.getString("CONDITION_EXT"));
		}
		return syncTable;
	}

	/**
	 * 查询表字段
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<TableField> getTableField() throws SQLException {
		String querySql = "select lower(table_name) as table_name,lower(column_name) as column_name from all_tab_cols where table_name in (select upper(table_name) from sync_table_key)";
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), querySql);
		List<TableField> list = new ArrayList<>();
		TableField tableField = null;
		while (rs.next()) {
			tableField = new TableField();
			tableField.setTableName(rs.getString("TABLE_NAME"));
			tableField.setField(rs.getString("COLUMN_NAME"));
			list.add(tableField);
		}
		return list;
	}
}
