package com.newland.boss.cloud.web.zkManageTool.controller;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkCfg;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkNode;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkPropMsg;
import com.newland.boss.cloud.web.zkManageTool.export.GenerateSql;
import com.newland.boss.cloud.web.zkManageTool.service.ZkManageToolService;

@Controller
public class ZkManageToolController {

	private static Log logger = LogFactory.getLog(ZkManageToolController.class);
	@Autowired
	private ZkManageToolService zkManageToolService;

	/**
	 * 获取zk目录结构
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getDir", method = RequestMethod.POST)
	@ResponseBody
	public String getDir(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String connectHost = json.getString("connectHost");
		String path = json.getString("path");

		try {
			String zkHost = StringUtils.isEmpty(connectHost) ? AppConfig.getProperty("zk.connect.host") : connectHost;
			List<ZkNode> list = zkManageToolService.getZkNode(path, zkHost);
			Map<String, Object> map = new HashMap<>();
			map.put("data", list);
			map.put("zkHost", zkHost);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (Exception e) {
			logger.error(e);
		}
		respJson = "[" + respJson + "]";
		logger.info("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 保存节点信息
	 * 
	 * @param reqJson
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	@ResponseBody
	public String saveData(@RequestBody String reqJson) throws JsonProcessingException {
		logger.info("reqJson:" + reqJson);
		JSONObject json = JSONObject.parseObject(reqJson);
		String path = json.getString("nodepath");
		String data = json.getString("nodedata");
		String respJson = "";
		try {
			boolean flag = zkManageToolService.setData(path, data);
			if (flag == true) {
				respJson = "保存成功";
			} else {
				respJson = "保存失败";
			}
			respJson = JacksonHelper.writeBean2Json(respJson);
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}
	
	

	/**
	 * 添加节点
	 * 
	 * @param reqJson
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/addNode", method = RequestMethod.POST)
	@ResponseBody
	public String addNode(@RequestBody String reqJson) throws JsonProcessingException {
		logger.info("reqJson:" + reqJson);
		JSONObject json = JSONObject.parseObject(reqJson);
		String respJson = "";
		String path = json.getString("nodepath");
		String title = json.getString("nodetitle");
		Map<String, Object> resultMap = new HashMap<>();

		try {
			boolean flag = zkManageToolService.addNode(path, title);
			if (flag == true) {
				resultMap.put("data", "添加节点成功!");
				resultMap.put("result", "success");
			} else {
				resultMap.put("data", "添加节点失败!");
				resultMap.put("result", "false");
			}
			respJson = JacksonHelper.writeBean2Json(resultMap);
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 删除节点
	 * 
	 * @param reqJson
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/deleteNode", method = RequestMethod.POST)
	@ResponseBody
	public String deleteNode(@RequestBody String reqJson) throws JsonProcessingException {
		logger.info("reqJson:" + reqJson);
		JSONObject json = JSONObject.parseObject(reqJson);
		String path = json.getString("nodepath");
		Map<String, Object> resultMap = new HashMap<>();
		String respJson = "";

		try {
			boolean flag = zkManageToolService.deleteNode(path);
			if (flag == true) {
				resultMap.put("data", "删除节点成功!");
				resultMap.put("result", "success");
			} else {
				resultMap.put("data", "删除节点失败!");
				resultMap.put("result", "false");
			}
			respJson = JacksonHelper.writeBean2Json(resultMap);
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/copyNode", method = RequestMethod.POST)
	@ResponseBody
	public String copyNode(@RequestBody String reqJson) throws JsonProcessingException {
		logger.info("reqJson:" + reqJson);
		JSONObject json = JSONObject.parseObject(reqJson);
		String path = json.getString("nodepath");
		String title = json.getString("nodetitle");
		Map<String, Object> resultMap = new HashMap<>();
		String respJson = "";
		try {
			Map<String, Object> map = zkManageToolService.copyNode(path, title);
			if ("success".equals(map.get("flag"))) {
				String copyPath = (String) map.get("copyPath");
				List<ZkNode> list = zkManageToolService.getZkNode(copyPath, null);
				resultMap.put("node", list);
				resultMap.put("data", "复制节点成功!");
				resultMap.put("result", "success");
			} else {
				resultMap.put("data", "复制节点失败!");
				resultMap.put("result", "false");
			}
			respJson = JacksonHelper.writeBean2Json(resultMap);
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 切换zk
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/changeZKConn", method = RequestMethod.POST)
	@ResponseBody
	public String changeZKConnection(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		JSONObject json = JSONObject.parseObject(reqJson);
		String connectHost = json.getString("connectHost");
		try {
			Boolean success = zkManageToolService.changeZkConn(connectHost);
			// String zkHost = connectHost == null ?
			// AppConfig.getProperty("zk.connect.host") : connectHost;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("connectHost", connectHost);
			map.put("result", success);
			respJson = JacksonHelper.writeBean2Json(map);
		} catch (Exception e) {
			logger.error(e);
		}
		respJson = "[" + respJson + "]";
		logger.info("respJson:" + respJson);
		return respJson;
	}

	@RequestMapping(value = "/exportData2Sql", method = RequestMethod.POST)
	public void exportData2Sql(HttpServletRequest request, HttpServletResponse response, @RequestBody String reqJson)
			throws Exception {

		JSONObject json = JSONObject.parseObject(reqJson);

		String reqNo = json.getString("reqNo");
		int zkId = json.getIntValue("zkId");
		JSONArray jsonArray = json.getJSONArray("paths");

		String fileName = reqNo;

		System.out.println("reqNo:" + reqNo + ",zkId=" + zkId + ",paths:" + jsonArray);

		response.reset();
		response.setContentType("multipart/form-data");
		String userAgent = request.getHeader("User-Agent");

		if (StringUtils.isBlank(userAgent)) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			if (userAgent.indexOf("MSIE") != -1) {
				// IE使用URLEncoder
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				// FireFox使用ISO-8859-1
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));

		ServletOutputStream out = null;
		PrintWriter writer = null;
		try {
			out = response.getOutputStream();
			writer = new PrintWriter(out);

			writer.write(GenerateSql.generateClearSql(reqNo, zkId));

			String path = "";
			String nodeValue = "";
			Iterator<Object> it = jsonArray.iterator();
			while (it.hasNext()) {
				path = (String) it.next();
				nodeValue = new String(zkManageToolService.getNodeData(path));
				writer.write(GenerateSql.generate(reqNo, zkId, path, nodeValue));
				writer.flush();
			}
		} finally {
			if (null != writer) {
				writer.flush();
				writer.close();
			}

			if (null != out) {
				out.flush();
				out.close();
			}

			response.reset();

		}
	}

	@RequestMapping(value = "/getZkCfg", method = RequestMethod.POST)
	@ResponseBody
	public String getZkCfg(@RequestBody String reqJson) {
		String respJson = "";
		List<ZkCfg> list = zkManageToolService.getZkCfg();
		if (list != null && list.size() > 0) {
			try {
				respJson = JacksonHelper.writeBean2Json(list);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		logger.info("zkCfg:" + respJson);
		return respJson;
	}

	/**
	 * 获取zk配置字段说明
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getZkProp", method = RequestMethod.POST)
	@ResponseBody
	public String getZkProp(@RequestBody String reqJson) {
		String respJson = "";
		logger.info("reqJson:" + reqJson);

		JSONObject json = JSONObject.parseObject(reqJson);
		String zkPath = json.getString("zkPath");

		List<ZkPropMsg> list = zkManageToolService.getZkPropMsg(zkPath);
		if (list != null && list.size() > 0) {
			try {
				respJson = JacksonHelper.writeBean2Json(list);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}
}
