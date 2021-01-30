package com.newland.boss.cloud.web.graphAnalysis.controller;

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
import com.newland.boss.cloud.web.graphAnalysis.entity.Metrics;
import com.newland.boss.cloud.web.graphAnalysis.service.GraphAnalysisService;

@RestController
public class GraphAnalysisController {

	private static Log logger = LogFactory.getLog(GraphAnalysisController.class);

	@Autowired
	private GraphAnalysisService graphAnalysisService;

	@RequestMapping(value = "/loadHostName", method = RequestMethod.POST)
	public String loadHostName() throws Exception {
		String respJson = "";
		List<String> list = graphAnalysisService.getHostName();
		respJson = JacksonHelper.writeBean2Json(list);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/loadMetricsApp", method = RequestMethod.POST)
	public String loadMetrics() throws Exception {
		String respJson = "";
		List<String> list = graphAnalysisService.getMetricsApp();
		respJson = JacksonHelper.writeBean2Json(list);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/getMetricsList", method = RequestMethod.POST)
	public String getMetricsList() throws Exception {
		String respJson = "";
		List<Metrics> list = graphAnalysisService.getMetricsList();
		respJson = JacksonHelper.writeBean2Json(list);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/getDetailData", method = RequestMethod.POST)
	public String getQueryData(@RequestBody MetricsDetail metrics) throws Exception {
		String respJson = "";
		logger.info("param:" + metrics.toString());

		try {
			List<MetricsDetail> list = graphAnalysisService.getQueryData(metrics);
			respJson = JacksonHelper.writeBean2Json(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

}
