package com.newland.boss.cloud.web.bds.service.impl;

import java.io.IOException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.re.core.util.StringUtils;
import com.newland.boss.cloud.web.bds.dao.AppViewDao;
import com.newland.boss.cloud.web.bds.entity.AppNodeInfo;
import com.newland.boss.cloud.web.bds.service.ZkMgr;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.cloud.web.util.ZkUtil;

@Component
public class ZkMgrImpl extends ZkUtil implements ZkMgr {

	private static Log logger = LogFactory.getLog(ZkMgrImpl.class);

	private static ZooKeeper zk;

	private static List<String> appList = new ArrayList<String>();
	// key:csn value:/config/type-csn
	private static Map<String, String> pathMap = new LinkedHashMap<>();

	private static List<String> needSwitchPathList = new ArrayList<String>();

	private static Map<String, String> bdsSerTypeCfg = new HashMap<String, String>();

	// zookeeper-services需要解析的服务路径表
	private static List<String> needReadPathList = new ArrayList<String>();

	private static String pathMapping = "";
	private static String servicesRootPath; // zookeeper-services路径

	static AppViewDao appViewDao = new AppViewDao();

	static {
		try {
			servicesRootPath = AppConfig.getProperty("zk.bds.services.path");
			pathMapping = AppConfig.getProperty("zk.app.path.mapping");
		} catch (Exception e) {
			logger.error("bdsweb.properties配置信息不完整,请检查!", e);
		}
	}

	public List<AppNodeInfo> removeBackNodeInfo(List<AppNodeInfo> list, String nodeName) {
		if (list == null || list.size() == 0) {
			return null;
		}
		String[] nodes = StringUtils.split(nodeName, ".");
		String appName = nodes[0];
		String hostNamePrefix = "n" + nodes[1] + "%";

		List<String> deployInfos = appViewDao.getAppDeployInfo(appName, hostNamePrefix);
		if (deployInfos == null || deployInfos.size() == 0) {
			logger.debug("app_deploy_info无查询结果!");
			return null;
		}
		List<AppNodeInfo> appNodeInfos = new ArrayList<>();
		for (String appIntanceName : deployInfos) {
			for (AppNodeInfo appNodeInfo : list) {
				if (appIntanceName.equals(appNodeInfo.getNodeName())) {
					appNodeInfos.add(appNodeInfo);
				}
			}
		}
		return appNodeInfos;
	}

	/*
	 * 获取应用节点信息列表 (non-Javadoc)
	 * 
	 * @see
	 * com.newland.boss.cloud.web.bds.service.ZkMgr#getAppNodeInfoList(java.
	 * lang.String)
	 */
	public List<AppNodeInfo> getAppNodeInfoList(String path) throws KeeperException, InterruptedException, IOException {
		connectZookeeper();
		List<AppNodeInfo> list = new ArrayList<AppNodeInfo>();
		List<String> childList = zk.getChildren(path, false);
		String dbSource = "";
		String nodeData = "";
		String ip = "";
		String state = "";
		String deployPath = "";
		String servicePath = "";
		JsonNode root = null;
		List<String> stateChildList = null;
		logger.info("path:" + path);
		for (String nodeName : childList) {
			logger.info("nodeName:" + nodeName);
			AppNodeInfo nodeInfo = new AppNodeInfo();
			nodeInfo.setNodeName(nodeName);
			// >>>>>应用节点信息<<<<
			nodeData = this.getZkNodeData(path + "/" + nodeName);
			root = JacksonHelper.readJson(nodeData);
			dbSource = getJsonNodeByPath(root, "routeParam.value.dbsource.value");
			nodeInfo.setDbSource(dbSource);
			// 如果是bds,需要获取serviceType
			servicePath = getJsonNodeByPath(root, "harpc.value.service.value");
			if (null != servicePath && !"".equals(servicePath)) {
				nodeInfo.setServiceType(this.getServiceType(servicePath));
			}
			// >>>>>应用状态节点信息<<<<<
			nodeData = this.getZkNodeData(path + "/" + nodeName + "/run_state");
			if (null != nodeData && !"".equals(nodeData)) {
				root = JacksonHelper.readJson(nodeData);
				ip = root.get("ip").asText();
				state = root.get("state").asText();
				deployPath = root.get("startPath").asText();
				nodeInfo.setIp(ip);
				nodeInfo.setState(state);
				nodeInfo.setDeployPath(deployPath);
				// 如果没有子节点，说明没启动
				stateChildList = zk.getChildren(path + "/" + nodeName + "/run_state", false);
				if (null == stateChildList || stateChildList.size() == 0) {
					nodeInfo.setState("stop");
				}
			}

			list.add(nodeInfo);
		}
		return list;
	}

	/*
	 * 获取需要主备切换的节点信息 (non-Javadoc)
	 * 
	 * @see com.newland.boss.cloud.web.bds.service.ZkMgr#getNeedSwitchNodeList()
	 */
	public List<AppNodeInfo> getNeedSwitchNodeList() throws KeeperException, InterruptedException, IOException {
		connectZookeeper();
		List<AppNodeInfo> list = new ArrayList<AppNodeInfo>();
		for (String path : needSwitchPathList) {
			logger.info("Go to get path:" + path);
			list.addAll(this.getAppNodeInfoList(path));
		}
		return list;
	}

	/**
	 * 获取ZK节点数据
	 * 
	 * @param appName
	 * @return
	 * @throws IOException
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String getZkNodeData(String zkNodePath) throws IOException, KeeperException, InterruptedException {
		connectZookeeper();

		if (null == zkNodePath || "".equals(zkNodePath)) {
			throw new RuntimeException("path[" + zkNodePath + "] cannot be null.");
		}

		if (null == zk.exists(zkNodePath, false)) {
			throw new RuntimeException("path[" + zkNodePath + "] does not exists.");
		}

		byte[] aData = zk.getData(zkNodePath, false, null);
		return new String(aData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.newland.boss.cloud.web.bds.service.ZkMgr#parsePathMapping()
	 * key:csn value:/config/type-csn
	 */
	public Map<String, String> parsePathMapping() {
		String tmpPathMapping = pathMapping.substring(1);
		tmpPathMapping = tmpPathMapping.substring(0, tmpPathMapping.length() - 1);
		String[] mappings = StringUtils.split(tmpPathMapping, ",");
		for (String mapping : mappings) {
			String[] cfg = StringUtils.split(mapping, ":");
			pathMap.put(cfg[0].trim(), AppConfig.getProperty(cfg[1].trim()));
			appList.add(cfg[0].trim());
			logger.info("app:" + cfg[0] + " path:" + AppConfig.getProperty(cfg[1].trim()));
		}
		return pathMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.newland.boss.cloud.web.bds.service.ZkMgr#getBdsMainFlag()
	 */
	public Map<String, String> getBdsMainFlag() throws Exception {
		connectZookeeper();
		Map<String, String> bdsMainFlagMap = new HashMap<String, String>();
		String bdsMainFlagCfgPath = AppConfig.getProperty("zk.bds.state.path");
		List<String> childList = zk.getChildren(bdsMainFlagCfgPath, false);
		String bdsMainFlag = "";
		for (String dbsource : childList) {
			bdsMainFlag = this.getZkNodeData(bdsMainFlagCfgPath + "/" + dbsource);
			bdsMainFlagMap.put(dbsource, bdsMainFlag.trim());
		}
		return bdsMainFlagMap;
	}

	/**
	 * 从集群配置中获取serviceType
	 * 
	 * @param servicePath
	 * @return
	 * @throws IOException
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private String getServiceType(String servicePath) throws IOException, KeeperException, InterruptedException {
		String serviceType = bdsSerTypeCfg.get(servicePath);
		if (null == serviceType) {
			String cfgJson = this.getZkNodeData(servicePath);
			List<Map<String, Object>> list = JacksonHelper.readJson2ListMap(cfgJson);
			if (null != list && list.size() > 0) {
				serviceType = (String) list.get(0).get("serviceType");
			}
		}
		return serviceType;
	}

	/**
	 * 解析需要主备切换的应用路径
	 * 
	 * @return
	 */
	private List<String> parseNeedSwitchBdsPath() {
		String bdsSwitchInclude = AppConfig.getProperty("zk.bds.switch.include");
		logger.info("bdsSwitchInclude:" + bdsSwitchInclude);
		String[] aInclude = StringUtils.split(bdsSwitchInclude, ",");
		for (String bdsAppName : aInclude) {
			logger.info("bdsAppName:" + bdsAppName);
			needSwitchPathList.add(pathMap.get(bdsAppName));
		}
		return needSwitchPathList;
	}

	/**
	 * 连接zookeeper
	 * 
	 * @return
	 * @throws IOException
	 */
	protected ZooKeeper connectZookeeper() throws IOException {
		if (null == zk) {
			zk = super.connectZookeeper();
			parsePathMapping();
			parseNeedSwitchBdsPath();
		}
		return zk;
	}

	private String getJsonNodeByPath(JsonNode jsonNode, String path) {
		String[] aPath = StringUtils.split(path, ".");
		// 带路径，需从根节点下开始
		try {
			for (String node : aPath) {
				jsonNode = jsonNode.get(node);
			}
		} catch (NullPointerException e) {
			// 如果路径不存在返回null
			return null;
		}
		return jsonNode.asText();
	}

	/**
	 * zookeeper-services 获取需要读取的服务路径
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	private List<String> parseNeedReadServicesPath() throws KeeperException, InterruptedException {
		List<String> childList = zk.getChildren(servicesRootPath, false);
		for (String child : childList) {
			String childPath = servicesRootPath + "/" + child;
			needReadPathList.add(childPath);
		}
		return needReadPathList;
	}

}
