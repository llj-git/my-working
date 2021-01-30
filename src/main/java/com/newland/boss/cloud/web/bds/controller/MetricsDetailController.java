package com.newland.boss.cloud.web.bds.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.bds.entity.MetricsDetail;
import com.newland.boss.cloud.web.bds.service.MetricsDetailService;

@RestController
public class MetricsDetailController {
	
	private static Log logger = LogFactory.getLog(BdsController.class);
	@Autowired
	private  MetricsDetailService metricsDetailService;
	
	@RequestMapping(value = "/queryAllMetricsDetail", method = RequestMethod.POST)
	public String queryAllMetricsDetail(@RequestBody MetricsDetail metrics) {
		List<MetricsDetail> metricsList = new ArrayList<>();
		metricsList = metricsDetailService.queryAllMetricsDetail(metrics);
		String respJson = "";
		try {
			respJson =  JacksonHelper.writeBean2Json(metricsList);
		} catch (JsonProcessingException e) {
			logger.info(e);
		}
		return respJson;
	}
	
	@RequestMapping(value = "/queryAllAppNames", method = RequestMethod.POST)
	public String queryAllAppNames() {
		List<String> appNameList = new ArrayList<>();
		appNameList = metricsDetailService.queryAllAppNames();
		String respJson = "";
		try {
			respJson =  JacksonHelper.writeBean2Json(appNameList);
		} catch (JsonProcessingException e) {
			logger.info(e);
		}
		return respJson;
	}
	
	@RequestMapping(value = "/queryAllHostNames", method = RequestMethod.POST)
	public String queryAllHostNames() {
		List<String> hostNameList = new ArrayList<>();
		hostNameList = metricsDetailService.queryAllHostNames();
		String respJson = "";
		try {
			respJson =  JacksonHelper.writeBean2Json(hostNameList);
		} catch (JsonProcessingException e) {
			logger.info(e);
		}
		return respJson;
	}
	
	@RequestMapping(value = "/queryAllFileNames", method = RequestMethod.POST)
	public String queryAllFileNames() {
		List<String> fileNameList = new ArrayList<>();
		fileNameList = metricsDetailService.queryAllFileNames();
		String respJson = "";
		try {
			respJson =  JacksonHelper.writeBean2Json(fileNameList);
		} catch (JsonProcessingException e) {
			logger.info(e);
		}
		return respJson;
	}
	
	
}
