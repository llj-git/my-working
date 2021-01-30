package com.newland.boss.cloud.web.balanceLibrary.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.util.DbUtil;

public class MetricsDetailDao extends BaseDao{

	private static final Logger LOGGER = LogManager.getLogger(MetricsDetailDao.class);
	private static final String SQL_EXCEPTION = "SQLException ";
	
	/**
	 * 查找所有应用实例
	 * @return
	 */
	public List<String> queryAllAppNames(){
		String querySql = "select distinct app_instance_name from metrics_detail";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<String> appNameList = new ArrayList<>();
		try {
			while (rs.next()) {
				appNameList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOGGER.info(e);
		}
		return appNameList;
	}
	
	/**
	 * 查找所有主机名
	 * @return
	 */
	public List<String> queryAllHostNames(){
		String querySql = "select distinct host_name from metrics_detail";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<String> hostNameList = new ArrayList<>();
		try {
			while (rs.next()) {
				hostNameList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOGGER.info(e);
		}
		return hostNameList;
	}
	
	/**
	 * 查找所有文件名
	 * @return
	 */
	public List<String> queryAllFileNames(){
		String querySql = "select distinct file_name from metrics_detail";
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<String> fileNameList = new ArrayList<>();
		try {
			while (rs.next()) {
				fileNameList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOGGER.info(e);
		}
		return fileNameList;
	}
	
	
	/**
	 * MetricsDetail
	 */
	public List<MetricsDetail> queryAllMetricsDetail(MetricsDetail metrics){
		
		SimpleDateFormat beginFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat endFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginDate = "";
		String endDate = "";
		
		String querySql = "select t.host_name, t.app_instance_name, t.file_name, t.t as t, t.count, t.max, t.mean, t.min, t.insert_time from metrics_detail t where 1=1";
		
		if (StringUtils.isNotEmpty(metrics.getAppInstanceName())) {
			querySql += " and t.app_instance_name = '" + metrics.getAppInstanceName() + "'";
		}
		
		if (StringUtils.isNotEmpty(metrics.getHostName())) {
			querySql += " and t.host_name = '" + metrics.getHostName() + "'";
		}
		
		if (StringUtils.isNotEmpty(metrics.getFileName())) {
			querySql += " and t.file_name = '" + metrics.getFileName() + "'";
		}
		
		if (null != metrics.getBeginDate() && null == metrics.getEndDate()) {
			beginDate = beginFormat.format(metrics.getBeginDate());
			querySql += " and t.t >= " + " to_date('" + beginDate + "','yyyy-MM-dd hh24:mi:ss') " ;
		}
		
		if (null != metrics.getEndDate() && null == metrics.getBeginDate()) {
			endDate = endFormat.format(metrics.getEndDate());
			querySql += " and t.t <= " + " to_date('" + endDate + "','yyyy-MM-dd hh24:mi:ss') " ;
		}
		
		if (null != metrics.getEndDate() && null != metrics.getBeginDate()) {
			beginDate = beginFormat.format(metrics.getBeginDate());
			endDate = endFormat.format(metrics.getEndDate());
			querySql += " and t.t between " + " to_date('" + beginDate + "','yyyy-MM-dd hh24:mi:ss') " 
						+ " and to_date(' " + endDate + "','yyyy-MM-dd hh24:mi:ss') ";
		}
				
		ResultSet rs = DbUtil.executeQuery(querySql);
		List<MetricsDetail> metricsList = new ArrayList<>();
		try {
			
			while (rs.next()) {
				MetricsDetail metricsDetail = new MetricsDetail();
				metricsDetail.setHostName(rs.getString(1));
				metricsDetail.setAppInstanceName(rs.getString(2));
				metricsDetail.setFileName(rs.getString(3));
				metricsDetail.setT(rs.getDate(4));
				metricsDetail.setTime(timeFormat.format(metricsDetail.getT()));
				metricsDetail.setCount(rs.getInt(5));
				metricsDetail.setMax(rs.getInt(6));
				metricsDetail.setMean(rs.getInt(7));
				metricsDetail.setMin(rs.getInt(8));
				metricsDetail.setInsertTime(rs.getDate(9));
				metricsList.add(metricsDetail);
			}
			
		} catch (SQLException e) {
			LOGGER.info(e);
		}
		
		return metricsList;
		
	}
	
	

}
