package com.newland.boss.cloud.web.bds.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;

import com.newland.boss.cloud.web.bds.entity.AppNodeInfo;
import com.newland.boss.cloud.web.bds.entity.ServiceInfo;

public interface ZkMgr {

	/**
	 * 根据路径获取路径下的应用节点信息
	 * 
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<AppNodeInfo> getAppNodeInfoList(String path) throws KeeperException, InterruptedException, IOException;

	/**
	 * 获取需要进行bds切换的所有bds节点
	 * 
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<AppNodeInfo> getNeedSwitchNodeList() throws KeeperException, InterruptedException, IOException;

	/**
	 * 解析应用与路径的映射关系
	 * 
	 * @return
	 */
	public Map<String, String> parsePathMapping();

	/**
	 * 获取bds主库标识
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getBdsMainFlag() throws Exception;

	/**
	 * 移除备用节点名称
	 * 
	 * @param list
	 * @param nodeName
	 * @return
	 */
	public List<AppNodeInfo> removeBackNodeInfo(List<AppNodeInfo> list, String nodeName);

}
