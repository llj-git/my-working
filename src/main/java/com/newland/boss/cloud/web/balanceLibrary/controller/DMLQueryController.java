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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.balanceLibrary.service.SQLDMLService;

@RestController
public class DMLQueryController {

	private static final Log LOGGER = LogFactory.getLog(DMLQueryController.class);
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
	@RequestMapping(value = "/getSQLDMLList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getSQLDMLList(@RequestBody String sql) {
		LOGGER.info("getSQLDMLList:" + sql);
		Map<String, String> resultMap = new HashMap<>();
		String respJson = null;
		try {
			if (sql.split(";").length > 1) {
				resultMap.put(RESULT, FALSE);
				resultMap.put(MSG, "查询更新删除语句目前仅仅支持单条语句的处理，请检查！！！");
				return JacksonHelper.writeBean2Json(resultMap);
			}
			respJson = verificateSQLFormat(sql);
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException", e);
		}
		return respJson;
	}

	/**
	 * 验证SQL的格式
	 * 
	 * @param sql
	 * @return
	 */
	public String verificateSQLFormat(String sql) {
		String respJson = "";
		Map<String, String> resultMap = new HashMap<>();
		String formatSQL = sql.toUpperCase().trim();
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		try {
			if (formatSQL.startsWith("SELECT")) {
				respJson = JacksonHelper.writeBean2Json(sqlDMLService.sqlQueryList(sql.trim()));
			} else if (formatSQL.startsWith("DELETE") || formatSQL.startsWith("UPDATE")) {
				if (formatSQL.contains("WHERE")) {
					respJson = JacksonHelper.writeBean2Json(sqlDMLService.sqlQueryList(sql.trim()));
				} else {
					resultMap.put(RESULT, FALSE);
					resultMap.put(MSG, "更新删除语句执行必须带条件，请检查！！！");
					respJson = JacksonHelper.writeBean2Json(resultMap);
				}
			} else {
				resultMap.put(RESULT, FALSE);
				resultMap.put(MSG, "输入的SQL语句有误，请检查！！！");
				respJson = JacksonHelper.writeBean2Json(resultMap);
			}
		} catch (Exception e) {
			LOGGER.error("JacksonHelper writeBean2Json Change Exception", e);
		}

		return respJson;
	}

}
