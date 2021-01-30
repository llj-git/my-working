package com.newland.boss.cloud.web.graphAnalysis.entity;


public class Metrics {

	/**
	 * app
	 */
	private String app;

	/**
	 * 指标
	 */
	private String metrics;

	/**
	 * 指标名
	 */
	private String metricsName;

	/**
	 * 指标详情
	 */
	private String metricsDesc;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	public String getMetricsDesc() {
		return metricsDesc;
	}

	public void setMetricsDesc(String metricsDesc) {
		this.metricsDesc = metricsDesc;
	}

}
