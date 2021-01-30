package com.newland.boss.cloud.web.bds.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.serviceStatus.entity.AppDeployInfo;
import com.newland.boss.cloud.web.util.DbUtil;

public class AppViewDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(AppViewDao.class);

	public List<String> getAppDeployInfo(String appName, String hostNamePrefix) {
		String querySql = "select APP_INSTANCE_NAME from app_deploy_info where app_status = 0 and app_name = ? and host_name like ?";
		ResultSet rs = DbUtil.executeQuery(querySql, appName, hostNamePrefix);
		List<String> list = new ArrayList<>();
		try {
			while (rs.next()) {
				list.add(rs.getString("APP_INSTANCE_NAME"));
			}
		} catch (SQLException e) {
			logger.error("", e);
		}

		return list;
	}

	public List<AppDeployInfo> getAppDeployInfos() {
		String querySql = "select IP,APP_INSTANCE_NAME from app_deploy_info where app_status = 0";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<AppDeployInfo> list = new ArrayList<>();
		AppDeployInfo appDeployInfo = null;
		try {
			while (rs.next()) {
				appDeployInfo = new AppDeployInfo();
				appDeployInfo.setIp(rs.getString("IP"));
				appDeployInfo.setAppStanceName(rs.getString("APP_INSTANCE_NAME"));
				list.add(appDeployInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
