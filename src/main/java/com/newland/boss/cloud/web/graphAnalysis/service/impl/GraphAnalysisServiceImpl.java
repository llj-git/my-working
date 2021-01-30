package com.newland.boss.cloud.web.graphAnalysis.service.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.graphAnalysis.dao.GraphAnalysisDao;
import com.newland.boss.cloud.web.graphAnalysis.entity.Metrics;
import com.newland.boss.cloud.web.graphAnalysis.service.GraphAnalysisService;

@Component
public class GraphAnalysisServiceImpl implements GraphAnalysisService {

	private static Log LOGGER = LogFactory.getLog(GraphAnalysisServiceImpl.class);

	static GraphAnalysisDao chartsdao = new GraphAnalysisDao();

	@Override
	public List<String> getMetricsApp() throws Exception {
		return chartsdao.querymetricApp();
	}

	@Override
	public List<Metrics> getMetricsList() throws Exception {
		return chartsdao.queryMetricsList();
	}

	@Override
	public List<MetricsDetail> getQueryData(MetricsDetail metrics) throws Exception {
		LOGGER.debug("hostName:" + metrics.getHostName());
		return chartsdao.queryDetailData(metrics);
	}

	@Override
	public List<String> getHostName() throws Exception {
		return chartsdao.queryHostName();
	}

}
