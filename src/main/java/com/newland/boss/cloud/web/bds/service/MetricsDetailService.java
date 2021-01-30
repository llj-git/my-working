package com.newland.boss.cloud.web.bds.service;

import java.util.List;

import com.newland.boss.cloud.web.bds.entity.MetricsDetail;

public interface MetricsDetailService {
	List<MetricsDetail> queryAllMetricsDetail(MetricsDetail metrics);
	List<String> queryAllAppNames();
	List<String> queryAllHostNames();
	List<String> queryAllFileNames();
}
