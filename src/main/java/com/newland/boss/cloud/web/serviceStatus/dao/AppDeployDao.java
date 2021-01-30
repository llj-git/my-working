package com.newland.boss.cloud.web.serviceStatus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.serviceStatus.entity.AppDeployInfo;
import com.newland.boss.cloud.web.util.DbUtil;
import com.newland.sri.ccp.common.util.StringUtils;

public class AppDeployDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(AppDeployDao.class);

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
			logger.error("", e);
		}
		return list;
	}

	public List<AppDeployInfo> getAppDeployByCondition(AppDeployInfo appDeployInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT IP,APP_INSTANCE_NAME,APP_NAME FROM APP_DEPLOY_INFO WHERE APP_STATUS = 0 AND GROUP_TYPE=0");
		if (StringUtils.isNotEmpty(appDeployInfo.getAppName())) {
			builder.append(" and ");
			builder.append(" APP_NAME='").append(appDeployInfo.getAppName()).append("'");
		}
		if (StringUtils.isNotEmpty(appDeployInfo.getIp())) {
			builder.append(" and ");
			builder.append(" IP='").append(appDeployInfo.getIp()).append("'");
		}
		ResultSet rs = DbUtil.executeQuery(builder.toString());
		List<AppDeployInfo> results = new ArrayList<>();
		AppDeployInfo info = null;
		try {
			while (rs.next()) {
				info = new AppDeployInfo();
				info.setIp(rs.getString("IP"));
				info.setAppStanceName(rs.getString("APP_INSTANCE_NAME"));
				info.setAppName(rs.getString("APP_NAME"));
				results.add(info);
			}
		} catch (SQLException e) {
			logger.error(builder.toString(), e);
		}
		return results;
	}

}
