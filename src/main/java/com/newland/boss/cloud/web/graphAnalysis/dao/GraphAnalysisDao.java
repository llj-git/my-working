package com.newland.boss.cloud.web.graphAnalysis.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.newland.boss.cloud.web.balanceLibrary.dao.BaseDao;
import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.graphAnalysis.entity.Metrics;
import com.newland.boss.cloud.web.util.DbUtil;

public class GraphAnalysisDao extends BaseDao {

	private static final Logger LOGGER = LogManager.getLogger(GraphAnalysisDao.class);

	public List<String> queryHostName() {
		String querySql = "select distinct host_name from metrics_detail";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<String> list = new ArrayList<>();
		try {
			while (rs.next()) {
				list.add(rs.getString("host_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	public List<String> querymetricApp() {
		String querySql = "select distinct app from metrics_dict";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<String> list = new ArrayList<>();
		try {
			while (rs.next()) {
				list.add(rs.getString("app"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Metrics> queryMetricsList() throws Exception {
		String sql = "select * from metrics_dict";
		ResultSet rs = DbUtil.executeQuery(sql);
		List<Metrics> list = new ArrayList<>();
		Metrics metrics = null;
		while (rs.next()) {
			metrics = new Metrics();
			metrics.setApp(rs.getString("APP"));
			metrics.setMetrics(rs.getString("METRICS"));
			metrics.setMetricsName(rs.getString("METRICS_NAME"));
			metrics.setMetricsDesc(rs.getString("METRICS_DESC"));
			list.add(metrics);
		}
		return list;
	}

	public List<MetricsDetail> queryDetailData(MetricsDetail metrics) {
		SimpleDateFormat beginFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat endFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginDate = "";
		String endDate = "";

		StringBuffer sb = new StringBuffer();
		sb.append("select APP_INSTANCE_NAME,T,MEAN from metrics_detail");
		sb.append(" where 1=1");

		if (StringUtils.isNotEmpty(metrics.getAppInstanceName())) {
			sb.append(" and app_instance_name like '%" + metrics.getAppInstanceName() + "%'");
		}

		if (StringUtils.isNotEmpty(metrics.getHostName())) {
			sb.append(" and host_name in (" + metrics.getHostName().trim() + ")");
		}

		if (StringUtils.isNotEmpty(metrics.getFileName())) {
			sb.append(" and file_name in (" + metrics.getFileName().trim() + ")");
		}

		if (null != metrics.getBeginDate() && null == metrics.getEndDate()) {
			beginDate = beginFormat.format(metrics.getBeginDate());
			sb.append(" and t >= to_date('" + beginDate + "','yyyy-MM-dd hh24:mi:ss')");
		}

		if (null != metrics.getEndDate() && null == metrics.getBeginDate()) {
			endDate = endFormat.format(metrics.getEndDate());
			sb.append(" and t <= to_date('" + endDate + "','yyyy-MM-dd hh24:mi:ss')");
		}

		if (null != metrics.getEndDate() && null != metrics.getBeginDate()) {
			beginDate = beginFormat.format(metrics.getBeginDate());
			endDate = endFormat.format(metrics.getEndDate());
			sb.append(" and t between to_date('" + beginDate + "','yyyy-MM-dd hh24:mi:ss') " + "and to_date('" + endDate
					+ "','yyyy-MM-dd hh24:mi:ss')");
		}
		sb.append(" order by T");

		LOGGER.info("sql:" + sb.toString());
		System.out.println("sql:" + sb.toString());
		ResultSet rs = DbUtil.executeQuery(sb.toString());
		List<MetricsDetail> list = new ArrayList<>();

		MetricsDetail meDetail = null;
		try {
			while (rs.next()) {
				meDetail = new MetricsDetail();
				meDetail.setAppInstanceName(rs.getString("APP_INSTANCE_NAME"));
				meDetail.setT(rs.getDate("T"));
				meDetail.setTime(timeFormat.format(meDetail.getT()));
				meDetail.setMean(rs.getInt("MEAN"));
				list.add(meDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
