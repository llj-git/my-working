package com.newland.boss.cloud.web.balanceLibrary.controller;

import java.util.HashMap;
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
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.balanceLibrary.service.SQLDMLService;

@RestController
public class DMLController {

	private static final Log LOGGER = LogFactory.getLog(DMLController.class);
	@Autowired
	private SQLDMLService sqlDMLService;

	private static final String RESULT = "result";
	private static final String FALSE = "false";
	private static final String MSG = "msg";

	

	/**
	 * 根据SQL查询相关数据
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/addSqlData", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addSqlData(@RequestBody String reqJson) {
		LOGGER.info("addSqlData method:" + reqJson);
		Map<String, String> resultMap = new HashMap<>();
		JSONObject jsStr = JSONObject.parseObject(reqJson);
		String sqlsObeject = jsStr.getString("sql").trim();
		String[] sqlArray = sqlsObeject.split(";");
		Integer partitionID = calcPartitionID(jsStr.getInteger("homeCity"),jsStr.getLong("userId"));

		String respJson = null;
		try {
			if (!verificateAddSQL(sqlArray)) {
				resultMap.put(RESULT, FALSE);
				resultMap.put(MSG, "新增语句有误，请检查！！！");
				return JacksonHelper.writeBean2Json(resultMap);
			}
			respJson = JacksonHelper.writeBean2Json(sqlDMLService.insertSql(sqlArray, partitionID));
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException", e);
		}
		return respJson;
	}


	/**
	 * 验证ADD SQL的格式
	 * 
	 * @param sql
	 * @return
	 */
	public Boolean verificateAddSQL(String[] sqlArray) {

		for (String verificateSql : sqlArray) {
			if (!verificateSql.toUpperCase().trim().startsWith("INSERT")) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 根据地市和用户标识计算分区标识
	 * @param homeCity
	 * @param userId
	 * @return
	 */
	public static Integer calcPartitionID(Integer homeCity, Long userId) {
		if (null != homeCity && null != userId) {
			return homeCity.intValue() * 1000 + ((int) (userId.longValue() % 200));
		} else {
			return null;
		}
	}

}
