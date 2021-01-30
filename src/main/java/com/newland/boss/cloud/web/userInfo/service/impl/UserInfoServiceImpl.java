package com.newland.boss.cloud.web.userInfo.service.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.boss.cloud.re.core.util.StringUtils;
import com.newland.boss.cloud.web.userInfo.dao.UserInfoDao;
import com.newland.boss.cloud.web.userInfo.entity.SyncTable;
import com.newland.boss.cloud.web.userInfo.entity.TableField;
import com.newland.boss.cloud.web.userInfo.service.UserInfoService;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.cloud.web.util.DbUtil;

@Component
public class UserInfoServiceImpl implements UserInfoService {

	private static Log LOGGER = LogFactory.getLog(UserInfoServiceImpl.class);
	private static UserInfoDao userInfoDao = new UserInfoDao();

	@Override
	public List<SyncTable> getSyncTable() throws Exception {
		return userInfoDao.loadAllSyncTable();
	}
	
	@Override
	public List<TableField> getTableField() throws SQLException {
		return userInfoDao.getTableField();
	}


	@Override
	public Map<String, String> getUserInfos(String tableName, String mainKeyValue, String condition, String type)
			throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		SyncTable syncTable = userInfoDao.querySyncTable(tableName);
		String sql = generateSql(syncTable, mainKeyValue, condition, type);
		if (StringUtils.isEmpty(sql)) {
			return null;
		}
		LOGGER.info("query sql:" + sql);
		try {
			ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getBillConnection(), sql);
			String result = resultSetToJson(rs);
			resultMap.put("data", result);
			resultMap.put("result", "true");
			resultMap.put("tableName", syncTable.getTableName());
		} catch (Exception e) {
			LOGGER.error("", e);
			resultMap.put("data", "查询失败");
			resultMap.put("result", "false");
		}
		return resultMap;
	}

	@Override
	public Map<String, String> updateUserInfo(String sql, String tableName, String keyValue) throws Exception {
		Map<String, String> map = new HashMap<>();
		String mainKey = userInfoDao.queryMainKey(tableName);
		// 更新记录
		int res = userInfoDao.updateData(sql);
		if (res == 0) {
			map.put("result", "fail");
			map.put("data", "修改记录失败");
			return map;
		}
		// 插入数据到异步同步表
		boolean flag = userInfoDao.addSyncData(mainKey, keyValue, tableName);
		if (flag) {
			map.put("result", "success");
			map.put("data", "修改记录成功,数据已同步表！");
		} else { 
			map.put("data", "插入同步表失败!");
			map.put("result", "fail");
		}
		return map;
	}

	@Override
	public Map<String, String> addUserInfo(List<Map<String, Object>> list) throws Exception {
		Map<String, Object> lastMap = list.get(list.size() - 1);// 最后一个为表名
		String table = (String) lastMap.get("value");
		String prefix = "insert into " + table + "(";

		String mainKey = userInfoDao.queryMainKey(table);
		String syncKeyValue = "";

		StringBuffer column = new StringBuffer();
		StringBuffer columnVal = new StringBuffer();
		int count = list.size() - 1;
		for (int i = 0; i < count; i++) {
			Map<String, Object> m = list.get(i);
			String name = (String) m.get("name");
			String value = (String) m.get("value");

			if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(name)) {
				column.append(name);
				column.append(",");

				columnVal.append(isDate(value));
				columnVal.append(",");
				if (mainKey.equalsIgnoreCase(name)) {
					syncKeyValue = value;
				}
			}
		}

		String field = column.toString();
		String fieldVal = columnVal.toString();

		String sql = prefix + field.substring(0, field.length() - 1) + ")values("
				+ fieldVal.substring(0, fieldVal.length() - 1) + ")";

		LOGGER.info(sql);

		Map<String, String> result = new HashMap<>();
		if (StringUtils.isEmpty(syncKeyValue)) {
			result.put("data", mainKey + "值为空,插入同步表失败!");
			result.put("result", "fail");
			return result;
		}
		return updateUserInfo(sql, table, syncKeyValue);
	}

	public Map<String, String> getProductDesc(String productId, String userId, String tableName) throws Exception {
		Map<String, String> map = new HashMap<>();
		if (StringUtils.isEmpty(productId)) {
			map.put("result", "fail");
			map.put("data", "productId is null");
			return map;
		}

		String result = userInfoDao.queryProductDesc(productId, userId);
		map.put("result", "success");
		map.put("data", StringUtils.isBlank(result) ? "套餐详情为空!" : result);
		return map;
	}

	public String generateSql(SyncTable syncTable, String mainKeyValue, String condition, String type) {
		if (null == syncTable) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		if ("1".equals(type)) {
			if ("user_product".equalsIgnoreCase(syncTable.getTableName())) {
				buffer.append("select a.rowid,b.name,a.* from user_product a,ng_product_def b where a.user_id='");
				buffer.append(mainKeyValue);
				buffer.append("' and a.product_id=b.product_id");
			} else {
				buffer.append("select a.rowid,a.* from ");
				buffer.append(syncTable.getTableName());
				buffer.append(" a where ");
				buffer.append(syncTable.getMainKey() + "='" + mainKeyValue + "'");
			}
			if (StringUtils.isNotEmpty(condition)) {
				buffer.append(" and a.");
				buffer.append(condition);
			}
		} else if ("2".equals(type)) {
			buffer.append("select a.rowid,a.* from ");
			buffer.append(syncTable.getTableName());
			buffer.append(" a where ");
			buffer.append(syncTable.getMainKey() + "='" + mainKeyValue + "' ");
			if (StringUtils.isNotEmpty(condition)) {
				buffer.append(" and a.");
				buffer.append(condition);
			}
			String sortCondition = syncTable.getConditionExt();
			if (!StringUtils.isEmpty(sortCondition)) {
				buffer.append(" "+sortCondition);
			}
		}
		
		// // 判断是否有查询条件
		// if (syncTable != null &&
		// !StringUtils.isEmpty(syncTable.getCondition())) {
		// sb.append(" "+syncTable.getCondition());
		// }

		return buffer.toString();
	}

	/**
	 * 结果集转json
	 * 
	 * @param sql
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	public String resultSetToJson(ResultSet rs) throws Exception {
		if (rs == null) {
			return "";
		}
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject(new LinkedHashMap());
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value != null ? value : "");
			}
			array.add(jsonObj);
		}
		return array.toString();
	}

	public static String isDate(String text) {
		if (text.length() > 20) {
			text = text.substring(0, 19);
		}
		if (isValidDate(text)) {
			return "to_date('" + text.replace(" ", "") + "','yyyy-mm-dd hh24:mi:ss')";
		}
		return "'" + text + "'";
	}

	public static boolean isValidDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	@Override
	public SyncTable querySyncTable(String tableName) throws Exception {
		return userInfoDao.querySyncTable(tableName);
	}

	@Override
	public Map<String, String> queryAltiBaseUserInfos(String tableName, String mainKey, String condition)
			throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		SyncTable syncTable = userInfoDao.querySyncTable(tableName);
		if (null == syncTable) {
			resultMap.put("data", "query error,syncTable is null!");
			resultMap.put("result", "false");
			return resultMap;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("select t.* from ");
		sb.append(syncTable.getTableName());
		sb.append(" t where ");
		sb.append(syncTable.getMainKey() + "='" + mainKey + "'");
		if (StringUtils.isNotEmpty(condition)) {
			sb.append(" and " + condition);
		}
		if (StringUtils.isNotEmpty(syncTable.getConditionExt())) {
			sb.append(" " + syncTable.getConditionExt());
		}
		LOGGER.info("query altibase sql:" + sb.toString());
		ResultSet rs = DbUtil.executeQuery(DBPoolConnection.getUsrAltibaseConnection(), sb.toString());
		String result = resultSetToJson(rs);
		if (StringUtils.isEmpty(result)) {
			resultMap.put("data", "result is empty!");
			resultMap.put("result", "false");
		} else {
			resultMap.put("data", result);
			resultMap.put("result", "true");
		}
		return resultMap;
	}




}
