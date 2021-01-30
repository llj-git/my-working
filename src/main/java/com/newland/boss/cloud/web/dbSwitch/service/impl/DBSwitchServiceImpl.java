package com.newland.boss.cloud.web.dbSwitch.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.newland.boss.cloud.web.dbSwitch.dao.DBSwitchDao;
import com.newland.boss.cloud.web.dbSwitch.entity.DataGuard;
import com.newland.boss.cloud.web.dbSwitch.entity.ExcuteFile;
import com.newland.boss.cloud.web.dbSwitch.entity.Script;
import com.newland.boss.cloud.web.dbSwitch.service.DBSwitchService;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.cloud.web.util.StringUtil;

@Component
public class DBSwitchServiceImpl implements DBSwitchService {

	private static final Logger LOGGER = LogManager.getLogger(DBSwitchServiceImpl.class);
	static DBSwitchDao dBSwitchDao = new DBSwitchDao();
	private static List<ExcuteFile> fileList;

	@Override
	public List<DataGuard> getDataGuardList(String path) {
		return dBSwitchDao.queryAllDataGuard();
	}

	@Override
	public DataGuard getDataGuardByHostName(String hostName) {
		return dBSwitchDao.queryDataGuard(hostName);
	}

	@Override
	public Map<String, String> confirmInfo(List<DataGuard> list) throws Exception {
		Map<String, String> map = new HashMap<>();
		StringBuffer buffer = new StringBuffer();
		List<Script> scriptList = getAllScript();
		if (scriptList == null || scriptList.size() < 1) {
			map.put("result", "fail");
			map.put("data", "数据库没有配置切换脚本,请检查switch_master_slave表！");
			return map;
		}
		ExcuteFile file = null;
		fileList = new ArrayList<>();
		for (Script script : scriptList) {
			String fullFilePath = "";
			if (!script.getPath().endsWith("/")) {
				fullFilePath = script.getPath() + "/" + script.getFileName();
			} else {
				fullFilePath = script.getPath() + script.getFileName();
			}
			if (new File(fullFilePath).exists()) {
				String shellParam = script.getParam();
				LOGGER.info("shell param:" + shellParam);
				String params = generateFileParam(shellParam, list);
				buffer.append("即将执行").append(fullFilePath).append(" ");
				buffer.append(params).append("\r\n");
				LOGGER.info("file params:" + params);
				// 保存脚本参数,避免重复查询
				file = new ExcuteFile();
				file.setFullFilePath(fullFilePath);
				file.setParams(params);
				fileList.add(file);
			} else {
				buffer.append(fullFilePath + "脚本不存在!" + "\r\n");
			}
		}

		map.put("result", "success");
		map.put("data", buffer.toString());
		return map;
	}

	@Override
	public Map<String, String> switchDataGuard(List<DataGuard> list) throws Exception {
		Map<String, String> map = new HashMap<>();
		DataGuard writeDb = null;// 写库
		DataGuard readDb = null;// 读库
		for (DataGuard dataGuard : list) {
			if (1 == dataGuard.getIsWrite()) {
				writeDb = dataGuard;
			} else {
				readDb = dataGuard;
			}
		}
		Connection conn = null;
		try {
			conn = DBPoolConnection.getConnection();
			conn.setAutoCommit(false);
			dBSwitchDao.updateDataGuard(writeDb.getHostName(), readDb.getIsWrite(), writeDb.getDbName(), conn);
			dBSwitchDao.updateDataGuard(readDb.getHostName(), writeDb.getIsWrite(), readDb.getDbName(), conn);
			if (null != fileList && fileList.size() > 0) {
				for (ExcuteFile excuteFile : fileList) {
					excuteShell(excuteFile.getFullFilePath(), excuteFile.getParams());
					// 休眠1秒
					Thread.sleep(1000);
				}
				conn.commit();
				map.put("result", "success");
				map.put("data", "切换主备库成功,数据库:" + readDb.getDbName() + "当前写库是：" + readDb.getHostName());
			} else {
				conn.rollback();
				map.put("result", "fail");
				map.put("data", "无切换脚本,操作失败!");
			}
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			LOGGER.error("", e);
			map.put("result", "success");
			map.put("data", "切换主备库失败！");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return map;
	}

	@Override
	public List<Script> getAllScript() throws SQLException {
		return dBSwitchDao.queryScript();
	}

	/**
	 * 字段转化
	 * 
	 * @param field
	 * @param dataGuard
	 * @return
	 * @throws Exception
	 */
	public static Object transformField(String field, DataGuard dataGuard) throws Exception {
		field = StringUtil.conversionField(field);
		return StringUtil.getMethod("get" + field, dataGuard);
	}

	/**
	 * 拼接脚本的参数
	 * 
	 * @param params
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String generateFileParam(String params, List<DataGuard> list) throws Exception {
		DataGuard writeDb = null;// 写库
		DataGuard readDb = null;// 读库
		for (DataGuard dataGuard : list) {
			if (1 == dataGuard.getIsWrite()) {
				writeDb = dataGuard;
			} else {
				readDb = dataGuard;
			}
		}
		int startIndex = StringUtils.indexOf(params, "[");
		int endIndex = StringUtils.indexOf(params, "]");
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		if (startIndex > 0) {
			// db_name,[is_main]格式
			String preStr = StringUtils.substring(params, 0, startIndex);
			String[] fixedParam = StringUtils.split(preStr, ",");
			for (int i = 0; i < fixedParam.length; i++) {
				buffer1.append(transformField(fixedParam[i], writeDb)).append(" ");
			}
			String endStr = StringUtils.substring(params, startIndex + 1, endIndex);
			String[] variableParams = StringUtils.split(endStr, ",");
			for (int i = 0; i < variableParams.length; i++) {
				buffer1.append(transformField(variableParams[i], writeDb)).append(" ");
				buffer2.append(transformField(variableParams[i], readDb)).append(" ");
			}
			return (buffer1.append(buffer2.toString())).toString();

		} else if (startIndex == 0) {
			// [host_name,ip,pname]格式
			String notFixedParam = StringUtils.substring(params, startIndex + 1, endIndex);
			String[] variableParams = StringUtils.split(notFixedParam, ",");
			for (int i = 0; i < variableParams.length; i++) {
				buffer1.append(transformField(variableParams[i], writeDb)).append(" ");
				buffer2.append(transformField(variableParams[i], readDb)).append(" ");
			}
			return (buffer1.append(buffer2.toString())).toString();
		} else {
			// name,ip格式
			String[] fixedParam = StringUtils.split(params, ",");
			for (int i = 0; i < fixedParam.length; i++) {
				buffer1.append(transformField(fixedParam[i], writeDb)).append(" ");
			}
			return buffer1.toString();
		}

	}

	public String excuteShell(String shellPath, String param) throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec("chmod 777 " + shellPath);
		process.waitFor();
		String cmd = shellPath + " " + param;
		LOGGER.info("开始启动脚本命令..." + cmd);
		process = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = null;
		StringBuffer sb = new StringBuffer("");
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		LOGGER.info("脚本执行完毕。");
		return process.waitFor() + "";
	}

}
