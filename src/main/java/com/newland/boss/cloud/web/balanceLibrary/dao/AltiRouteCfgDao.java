package com.newland.boss.cloud.web.balanceLibrary.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newland.boss.cloud.web.balanceLibrary.entity.AltiRouteCfg;
import com.newland.boss.cloud.web.util.DBPoolConnection;

public class AltiRouteCfgDao extends BaseDao {

	private static final Logger LOGGER = LogManager.getLogger(AltiRouteCfgDao.class);
	private static final String SQL_EXCEPTION = "SQLException ";

	/**
	 * 查询出所有的路由表规则
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<AltiRouteCfg> loadRouteCfgList() throws SQLException {
		String sql = "select * from alti_route_cfg order by db_name asc";
		List<AltiRouteCfg> routeLists = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DBPoolConnection.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				AltiRouteCfg routeCfg = new AltiRouteCfg();
				routeCfg.setDbName(rs.getString("DB_NAME"));
				routeCfg.setBeginPartitionId(rs.getInt("BEGIN_PARTITION_ID"));
				routeCfg.setEndPartitionId(rs.getInt("END_PARTITION_ID"));
				routeCfg.setPartitionDesc(rs.getString("PARTITION_DESC"));
				routeLists.add(routeCfg);
			}
		} catch (SQLException e) {
			LOGGER.error(SQL_EXCEPTION, e);
		} finally {
			closeResource(conn, ps, rs);
		}
		return routeLists;
	}

}
