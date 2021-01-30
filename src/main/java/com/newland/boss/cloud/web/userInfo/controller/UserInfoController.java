package com.newland.boss.cloud.web.userInfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.userInfo.service.UserInfoService;

@RestController
public class UserInfoController {

	private static Log logger = LogFactory.getLog(UserInfoController.class);

	@Autowired
	private UserInfoService userInfoService;
	
	
	@RequestMapping(value = "/getProductDesc", method = RequestMethod.POST)
	public String getProductDesc(@RequestBody String reqJson) throws Exception {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String productId = json.getString("productId");
		String tableName = json.getString("dbName");
		String userId = json.getString("userId");

		try {
			Map<String, String> map = userInfoService.getProductDesc(productId, userId, tableName);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/addUserInfo", method = RequestMethod.POST)
	public String addData(@RequestBody String reqJson) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		List<Map<String, Object>> list = JacksonHelper.readJson2ListMap(reqJson);
		if (list == null) {
			resultMap.put("data", "添加记录失败");
			resultMap.put("result", "false");
			return JacksonHelper.writeBean2Json(resultMap);
		}

		Map<String, String> map = userInfoService.addUserInfo(list);
		respJson = JacksonHelper.writeBean2Json(map);
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/submitData", method = RequestMethod.POST)
	public String updateData(@RequestBody String reqJson) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		logger.info("reqJson:" + reqJson);
		String respJson = "";

		List<Map<String, Object>> list = JacksonHelper.readJson2ListMap(reqJson);
		if (list == null) {
			resultMap.put("data", "修改失败，参数为空");
			resultMap.put("result", "false");
			return JacksonHelper.writeBean2Json(resultMap);
		}

		try {
			for (Map<String, Object> param : list) {
				String rowId = (String) param.get("rowId");
				String colum = (String) param.get("colum");
				StringBuffer sb = new StringBuffer();
				sb.append(" update ");
				sb.append((String) param.get("dbName"));
				sb.append(" set ");
				sb.append(colum.substring(0, colum.length() - 1));
				sb.append(" where rowid='");
				sb.append(rowId);
				sb.append("'");

				logger.info("sql:" + sb.toString());

				if (StringUtils.isEmpty(colum)) {
					resultMap.put("data", "修改失败,没有修改内容!");
					resultMap.put("result", "false");
					return JacksonHelper.writeBean2Json(resultMap);
				}
				if (StringUtils.isEmpty(rowId)) {
					resultMap.put("data", "修改失败,rowid不存在!");
					resultMap.put("result", "false");
					return JacksonHelper.writeBean2Json(resultMap);
				}

				resultMap = userInfoService.updateUserInfo(sb.toString(), (String) param.get("dbName"),
						(String) param.get("inputId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("data", "修改失败!");
			resultMap.put("result", "false");
		}
		respJson = JacksonHelper.writeBean2Json(resultMap);
		logger.info("respJson:" + respJson);
		return respJson;
	}
	
	
	

}
