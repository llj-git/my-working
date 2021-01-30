package com.newland.boss.cloud.web.userInfo.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.userInfo.entity.SyncTable;
import com.newland.boss.cloud.web.userInfo.entity.TableField;
import com.newland.boss.cloud.web.userInfo.service.UserInfoService;

@RestController
public class UserInfoQueryController {

	private static Log logger = LogFactory.getLog(UserInfoQueryController.class);

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping(value = "/loadSyncTable", method = RequestMethod.POST)
	public String loadSyncTable() throws Exception {
		String respJson = "";
		List<SyncTable> list = userInfoService.getSyncTable();
		respJson = JacksonHelper.writeBean2Json(list);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/loadTableField", method = RequestMethod.POST)
	public String loadTableField() throws Exception {
		String respJson = "";
		List<TableField> list = userInfoService.getTableField();
		respJson = JacksonHelper.writeBean2Json(list);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public String getQueryData(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String type = json.getString("type");
		String tableName = json.getString("tableName");
		String condition = json.getString("condition");
		String mainKeyValue = json.getString("mainKeyValue");

		try {
			Map<String, String> map = userInfoService.getUserInfos(tableName, mainKeyValue, condition, type);
 			respJson = JacksonHelper.writeBean2Json(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/queryAltiBase", method = RequestMethod.POST)
	public String queryAltiBase(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String tableName = json.getString("tableName");
		String mainKeyValue = json.getString("mainKeyValue");
		String condition = json.getString("condition");

		try {
			Map<String, String> map = userInfoService.queryAltiBaseUserInfos(tableName, mainKeyValue, condition);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		logger.info("respJson:" + respJson);
		return respJson;
	}
}
