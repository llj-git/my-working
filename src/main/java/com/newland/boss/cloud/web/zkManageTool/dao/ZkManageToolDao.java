package com.newland.boss.cloud.web.zkManageTool.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.util.DbUtil;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkCfg;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkPropMsg;

public class ZkManageToolDao extends BaseDao {

	public List<ZkCfg> queryZkCfgList() {
		String querySql = "select ZK_ID,ZK_NAME,ZK_HOST from zk_cfg";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<ZkCfg> list = new ArrayList<>();
		try {
			ZkCfg zkCfg = null;
			while (rs.next()) {
				zkCfg = new ZkCfg();
				zkCfg.setZkId(rs.getInt("ZK_ID"));
				zkCfg.setZkName(rs.getString("ZK_NAME"));
				zkCfg.setZkHost(rs.getString("ZK_HOST"));
				list.add(zkCfg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ZkPropMsg> queryZkPropMsgList(String zkPath) {
		String querySql = "select zk_id,zk_prop,zk_prop_name,zk_prop_desc from zk_prop_dict where zk_path=?";
		ResultSet rs = DbUtil.executeQuery(querySql, zkPath);
		List<ZkPropMsg> list = new ArrayList<>();
		try {
			ZkPropMsg zkProp = null;
			while (rs.next()) {
				zkProp = new ZkPropMsg();
				zkProp.setZkId(rs.getInt("zk_id"));
				zkProp.setZkProp(rs.getString("zk_prop"));
				zkProp.setZkPropName(rs.getString("zk_prop_name"));
				zkProp.setZkPropDesc(rs.getString("zk_prop_desc"));
				list.add(zkProp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
