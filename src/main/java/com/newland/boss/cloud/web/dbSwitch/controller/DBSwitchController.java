package com.newland.boss.cloud.web.dbSwitch.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.newland.boss.cloud.re.core.exception.JsonParseException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.dbSwitch.entity.DataGuard;
import com.newland.boss.cloud.web.dbSwitch.service.DBSwitchService;

@RestController
public class DBSwitchController {

	private static Log logger = LogFactory.getLog(DBSwitchController.class);

	@Autowired
	private DBSwitchService dBSwitchService;

	/**
	 * 获取主备库列表
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getDateBaseList", method = RequestMethod.POST)
	public String getDateBaseList(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";

		try {
			List<DataGuard> resultList = dBSwitchService.getDataGuardList(null);
			if (resultList != null && resultList.size() > 0) {
				respJson = JacksonHelper.writeBean2Json(resultList);
			}
		} catch (JsonProcessingException e) {
			logger.error("", e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/confirmInfo", method = RequestMethod.POST)
	public String confirmParam(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		List<DataGuard> jsonList = JSONObject.parseArray(reqJson, DataGuard.class);

		try {
			Map<String, String> map = dBSwitchService.confirmInfo(jsonList);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (Exception e) {
			logger.error("", e);
		}
		return respJson;
	}

	/**
	 * 切换主备库
	 * 
	 * @param reqJson
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/switchDateBase", method = RequestMethod.POST)
	public String switchDateBase(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		List<DataGuard> jsonList = JSONObject.parseArray(reqJson, DataGuard.class);
		String respJson = "";

		try {
			Map<String, String> map = dBSwitchService.switchDataGuard(jsonList);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (Exception e) {
			logger.error("", e);
		}
		return respJson;
	}
}
