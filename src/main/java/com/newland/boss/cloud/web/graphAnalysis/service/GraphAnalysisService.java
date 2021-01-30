package com.newland.boss.cloud.web.graphAnalysis.service;

import java.util.List;
import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.graphAnalysis.entity.Metrics;

public interface GraphAnalysisService {

	/**
	 * 主机名
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getHostName() throws Exception;

	/**
	 * 查询指标app名
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getMetricsApp() throws Exception;

	/**
	 * 根据app名字查询对应的指标
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Metrics> getMetricsList() throws Exception;

	/**
	 * 查询详细数据
	 * 
	 * @param appInstanceName
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<MetricsDetail> getQueryData(MetricsDetail metrics) throws Exception;

}