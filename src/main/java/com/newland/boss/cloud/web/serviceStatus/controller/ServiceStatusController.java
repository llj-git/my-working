package com.newland.boss.cloud.web.serviceStatus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.bds.entity.BdsServiceListComparator;
import com.newland.boss.cloud.web.bds.entity.ServiceInfo;
import com.newland.boss.cloud.web.serviceStatus.entity.AppDeployInfo;
import com.newland.boss.cloud.web.serviceStatus.entity.IgniteNode;
import com.newland.boss.cloud.web.serviceStatus.service.ServiceStatusService;

@RestController
public class ServiceStatusController {
	private static Log logger = LogFactory.getLog(ServiceStatusController.class);

	@Autowired
	private ServiceStatusService statusService;

	/**
	 * zk读取服务类型
	 * 
	 * @param reqJson
	 * @return
	 */
	// @RequestMapping(value = "/getServiceType", method = RequestMethod.POST)
	// public String getServiceType(@RequestBody String reqJson) {
	// logger.info("reqJson:" + reqJson);
	// String respJson = "";
	// try {
	// List<String> serviceTypeList = statusService.getServiceType();
	// Collections.sort(serviceTypeList);
	// respJson = JacksonHelper.writeBean2Json(serviceTypeList);
	// } catch (KeeperException | InterruptedException | IOException e) {
	// logger.error("获取ServiceType失败", e);
	// }
	// logger.info("respJson:" + respJson);
	// return respJson;
	// }

	@RequestMapping(value = "/serviceChildNodes", method = RequestMethod.POST)
	public String getSerivceList(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		try {
			List<String> serviceInfoList = statusService.getServiceName(reqJson);
			Collections.sort(serviceInfoList);
			respJson = JacksonHelper.writeBean2Json(serviceInfoList);
		} catch (KeeperException | InterruptedException | IOException e) {
			logger.error("", e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/getServiceDetail", method = RequestMethod.POST)
	public String getServiceDetail(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String serviceType = json.getString("serviceType");
		String serviceName = json.getString("serviceName");

		try {
			if ("ignite".equalsIgnoreCase(serviceType)) {
				String newServiceName = serviceName.equals("centerA") ? "na" : "nb";
				IgniteNode igniteNode = statusService.getIgniteNodeInfo(serviceType, newServiceName);
				respJson = JacksonHelper.writeBean2Json(igniteNode);
			} else {
				List<ServiceInfo> serviceInfoList = statusService.getServiceDetail(serviceType, serviceName);
				for (ServiceInfo serviceInfo : serviceInfoList) {
					Collections.sort(serviceInfo.getProvideList(), new BdsServiceListComparator());
				}
				respJson = JacksonHelper.writeBean2Json(serviceInfoList);
			}
		} catch (KeeperException | InterruptedException | IOException e) {
			logger.error("", e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}
	
	@RequestMapping(value = "/appstatus_zk", method = RequestMethod.POST)
	public String getHostAppStatus(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		long start=System.currentTimeMillis();
		String respJson = "";
		JSONObject jsonObject = null;

		List<String> ipList = new ArrayList<>();
		JSONArray jsonArray = JSONObject.parseObject(reqJson).getJSONArray("items");
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String ip = jsonObject.getString("ip");
			if (null != jsonObject && StringUtils.isNotEmpty(ip)) {
				ipList.add(ip);
			}
		}
		Map<String, Object> map = statusService.getAppStatusByIp(ipList);
		respJson = JacksonHelper.writeBean2Json(map);
		logger.info("respJson:" + respJson);
		long end=System.currentTimeMillis();
		logger.info("time:" + (end-start));
		return respJson;
	}
	
	
//	@RequestMapping(value = "/appstatus_zk_new", method = RequestMethod.POST)
//	public String getAppStatusByCondition(@RequestBody String reqJson) throws Exception {
//		long start = System.currentTimeMillis();
//		logger.info("reqJson:" + reqJson);
//		String respJson = "";
//
//		String json = JSONObject.parseObject(reqJson).getJSONArray("items").toJSONString();
//		List<AppDeployInfo> requestAppInfos = JSON.parseArray(json, AppDeployInfo.class);
//
//		Map<String, Object> map = statusService.getAppStatusByCondition(requestAppInfos);
//		respJson = JacksonHelper.writeBean2Json(map);
//		logger.info("respJson:" + respJson);
//		long end = System.currentTimeMillis();
//		logger.info("time:" + (end - start));
//		return respJson;
//	}
//	

}
