package com.newland.boss.cloud.web.dbSwitch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.dbSwitch.entity.DataGuard;
import com.newland.boss.cloud.web.dbSwitch.entity.Script;
import com.newland.boss.cloud.web.util.DbUtil;

public class DBSwitchDao extends BaseDao {

	public List<DataGuard> queryAllDataGuard() {
		String querySql = "select * from DB_HOST_CFG order by db_name,is_main asc";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<DataGuard> dataGuardList = new ArrayList<>();
		try {
			DataGuard dataGuard = null;
			while (rs.next()) {
				dataGuard = new DataGuard();
				dataGuard.setDbName(rs.getString("DB_NAME"));
				dataGuard.setIsMain(rs.getInt("IS_MAIN"));
				dataGuard.setHostName(rs.getString("HOST_NAME"));
				dataGuard.setIp(rs.getString("IP"));
				dataGuard.setPname(rs.getString("PNAME"));
				dataGuard.setCommons(rs.getString("COMMONS"));
				dataGuard.setIsWrite(rs.getInt("IS_WRITE"));
				dataGuardList.add(dataGuard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataGuardList;
	}

	public DataGuard queryDataGuard(String hostName) {
		String querySql = "select * from DB_HOST_CFG where HOST_NAME=?";
		ResultSet rs = DbUtil.executeQuery(querySql, hostName);
		DataGuard dataGuard = null;
		try {
			while (rs.next()) {
				dataGuard = new DataGuard();
				dataGuard.setDbName(rs.getString("DB_NAME"));
				dataGuard.setIsMain(rs.getInt("IS_MAIN"));
				dataGuard.setHostName(rs.getString("HOST_NAME"));
				dataGuard.setIp(rs.getString("IP"));
				dataGuard.setPname(rs.getString("PNAME"));
				dataGuard.setCommons(rs.getString("COMMONS"));
				dataGuard.setIsWrite(rs.getInt("IS_WRITE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataGuard;
	}

	public int updateDataGuard(String hostName, Integer isWrite, String dbName, Connection conn) throws SQLException {
		String sql = "update DB_HOST_CFG set IS_WRITE=? where HOST_NAME=? and DB_NAME=?";
		int cnt = 0;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, isWrite);
		ps.setString(2, hostName);
		ps.setString(3, dbName);
		cnt = ps.executeUpdate();
		return cnt;
	}

	
	public List<Script> queryScript() throws SQLException {
		String querySql = "select * from switch_master_slave";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<Script> scriptList = new ArrayList<>();
		Script script = null;
		while (rs.next()) {
			script = new Script();
			script.setHostName(rs.getString("HOSTNAME"));
			script.setIp(rs.getString("IP"));
			script.setPath(rs.getString("PATH"));
			script.setFileName(rs.getString("SHELL_FILE"));
			script.setParam(rs.getString("SHELL_PARAM"));
			scriptList.add(script);
		}
		return scriptList;
	}
}
