package com.newland.boss.cloud.web.dbSwitch.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.newland.boss.cloud.web.dbSwitch.entity.DataGuard;
import com.newland.boss.cloud.web.dbSwitch.entity.Script;

public interface DBSwitchService {

	/**
	 * 获取主备库列表
	 * 
	 * @param path
	 * @return
	 */
	public List<DataGuard> getDataGuardList(String path);

	/**
	 * 根据主机名查询详细信息
	 * 
	 * @param hostName
	 * @return
	 */
	public DataGuard getDataGuardByHostName(String hostName);

	/**
	 * 确认切换文件是否正确
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> confirmInfo(List<DataGuard> list) throws Exception;

	/**
	 * 切换主备库
	 * 
	 * @param oldDataGuard
	 * @param newDataGuard
	 * @throws Exception
	 */
	public Map<String, String> switchDataGuard(List<DataGuard> list) throws Exception;

	/**
	 * 获取脚本
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Script> getAllScript() throws SQLException;
}
