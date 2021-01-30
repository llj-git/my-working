package com.newland.boss.cloud.web.bds.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newland.boss.cloud.web.balanceLibrary.dao.MetricsDetailDao;
import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.bds.service.MetricsDetailService;

@Component
public class MetricsDetailServiceImpl implements MetricsDetailService {
	
	static MetricsDetailDao metricsDetailDao = new MetricsDetailDao();
	
	@Override
	public List<MetricsDetail> queryAllMetricsDetail(MetricsDetail metrics) {
		return metricsDetailDao.queryAllMetricsDetail(metrics);
	}

	@Override
	public List<String> queryAllAppNames() {
		return metricsDetailDao.queryAllAppNames();
	}

	@Override
	public List<String> queryAllHostNames() {
		return metricsDetailDao.queryAllHostNames();
	}

	@Override
	public List<String> queryAllFileNames() {
		return metricsDetailDao.queryAllFileNames();
	}

}
