package com.newland.boss.cloud.web.userInfo.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.newland.boss.cloud.web.userInfo.entity.SyncTable;
import com.newland.boss.cloud.web.userInfo.entity.TableField;

public interface UserInfoService {

	public List<SyncTable> getSyncTable() throws Exception;

	public List<TableField> getTableField() throws SQLException;

	/**
	 * 用户资料库查询
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, String> getUserInfos(String tableName, String mainKeyValue, String condition, String type)
			throws Exception;

	/**
	 * 用户资料库更新
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public Map<String, String> updateUserInfo(String sql, String tableName, String keyValue) throws Exception;

	/**
	 * 用户资料库添加
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> addUserInfo(List<Map<String, Object>> list) throws Exception;

	/**
	 * 查询同步表
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public SyncTable querySyncTable(String tableName) throws Exception;

	/**
	 * 查询套餐详情
	 * 
	 * @param id
	 * @param input
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getProductDesc(String productId, String userId, String tableName) throws Exception;

	/**
	 * altibase库用户资料查询
	 * 
	 * @param id
	 * @param input
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryAltiBaseUserInfos(String tableName, String mainKey, String condition)
			throws Exception;
}
