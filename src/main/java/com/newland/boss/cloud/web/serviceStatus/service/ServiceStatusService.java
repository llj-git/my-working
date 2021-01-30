package com.newland.boss.cloud.web.serviceStatus.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import com.newland.boss.cloud.web.bds.entity.ServiceInfo;
import com.newland.boss.cloud.web.serviceStatus.entity.AppDeployInfo;
import com.newland.boss.cloud.web.serviceStatus.entity.IgniteNode;

public interface ServiceStatusService {

	/**
	 * 获取serviceType
	 * 
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	// public List<String> getServiceType() throws KeeperException,
	// InterruptedException, IOException;

	/**
	 * 获取serviceName
	 * 
	 * @param inputServiceType
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<String> getServiceName(String inputServiceType)
			throws KeeperException, InterruptedException, IOException;

	/**
	 * 获取服务详细节点信息
	 * 
	 * @param serviceType
	 * @param serviceName
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<ServiceInfo> getServiceDetail(String serviceType, String serviceName)
			throws KeeperException, InterruptedException, IOException;

	/**
	 * Ignite节点信息
	 * 
	 * @param serviceType
	 * @return
	 * @throws Exception
	 */
	public IgniteNode getIgniteNodeInfo(String serviceType, String serviceName) throws Exception;

	/**
	 * 通过ip获取应用状态
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAppStatusByIp(List<String> list) throws Exception;

	/**
	 * 多条件获取应用状态
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
//	public Map<String, Object> getAppStatusByCondition(List<AppDeployInfo> list) throws Exception;
}
