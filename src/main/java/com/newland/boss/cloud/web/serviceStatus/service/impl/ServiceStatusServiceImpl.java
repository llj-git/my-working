package com.newland.boss.cloud.web.serviceStatus.service.impl;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.re.core.util.StringUtils;
import com.newland.boss.cloud.web.bds.dao.AppViewDao;
import com.newland.boss.cloud.web.bds.entity.AppNodeInfo;
import com.newland.boss.cloud.web.bds.entity.ServiceInfo;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.cloud.web.serviceStatus.entity.AppDeployInfo;
import com.newland.boss.cloud.web.serviceStatus.entity.IgniteNode;
import com.newland.boss.cloud.web.serviceStatus.service.ServiceStatusService;

import com.newland.boss.cloud.web.util.ZkUtil;

@Component
public class ServiceStatusServiceImpl extends ZkUtil implements ServiceStatusService {

    private static Log logger = LogFactory.getLog(ServiceStatusServiceImpl.class);
    private static List<String> needReadPathList = new ArrayList<String>();
    // private static ZooKeeper zk;
    private static String ignitePath = "";
    private static String servicesRootPath = "";
    private static List<String> childNodeList = new ArrayList<>();
    static AppViewDao appViewDao = new AppViewDao();

    static {
        String flag = AppConfig.getProperty("zk.switch").toUpperCase();
        if (flag.equals("TRUE")) {
            try {
                long begin = System.currentTimeMillis();
                ignitePath = AppConfig.getProperty("zk.ignite.services.path");
                servicesRootPath = AppConfig.getProperty("zk.bds.services.path");
                getNodePath("/config");
                if (childNodeList.isEmpty() || childNodeList == null) {
                    logger.info("init zookeeper config path fail!");
                    throw new Exception("init zookeeper config path fail!");
                }
                long end = System.currentTimeMillis();
                logger.info("init zk node path data use time:" + (end - begin));
            } catch (Exception e) {
                logger.error("bdsweb.properties配置信息不完整,请检查!", e);
            }
        }
    }

    // public List<String> getServiceType() throws KeeperException,
    // InterruptedException, IOException {
    // // 连接服务
    // connectZookeeper();
    // // 初始化路径
    // parseNeedReadServicesPath();
    // List<String> serviceList = new ArrayList<>();
    // String serviceType = "";
    // // 获取serviceType
    // for (String path : needReadPathList) {
    // // 路径中截取serviceType
    // serviceType = interceptServiceType(path);
    // if (!serviceList.contains(serviceType)) {
    // serviceList.add(serviceType);
    // }
    // }
    // return serviceList;
    // }

    public List<ServiceInfo> getServiceDetail(String serviceType, String serviceName)
            throws KeeperException, InterruptedException, IOException {
        List<ServiceInfo> result = new ArrayList<ServiceInfo>();
        List<String[]> provideList = new ArrayList<String[]>();
        List<String> consumerList = new ArrayList<String>();
        List<String> sericeNodeList = null;
        String serviceNodeValue = null;
        String consumerValue = null;
        Map<String, Object> serviceNodeValueMap = null;
        // 断开后重连
        connectZookeeper();
        // 根据ServiceType获取对应路径
        String realPath = getRealPathByServiceType(serviceType);
        // 获取完整serviceName
        String fullServiceNamePath = getFullServiceName(realPath, serviceName);
        // consumers
        if (zk.exists(realPath + "/" + fullServiceNamePath + "/consumers", false) != null) {
            List<String> lists = zk.getChildren(realPath + "/" + fullServiceNamePath + "/consumers", false);
            for (String node : lists) {
                consumerValue = this.getZkNodeData(realPath + "/" + fullServiceNamePath + "/consumers/" + node);
                if ("csn".indexOf(serviceType) != -1) {
                    consumerList.add(consumerValue);
                } else if ("bds".indexOf(serviceType) != -1) {
                    Map<String, Object> map = JacksonHelper.readJson2Map(consumerValue);
                    consumerList.add((String) map.get("ip"));
                }
            }
        }
        // providers
        if (zk.exists(realPath + "/" + fullServiceNamePath + "/providers", false) != null) {
            sericeNodeList = zk.getChildren(realPath + "/" + fullServiceNamePath + "/providers", false);
            for (String serviceNodeName : sericeNodeList) {
                serviceNodeValue = this
                        .getZkNodeData(realPath + "/" + fullServiceNamePath + "/providers/" + serviceNodeName);
                serviceNodeValueMap = JacksonHelper.readJson2Map(serviceNodeValue);
                if ("csn".indexOf(serviceType) != -1) {
                    String[] csn = getCsnServiceInfo(serviceNodeValueMap);
                    provideList.add(csn);
                } else if ("bds".indexOf(serviceType) != -1) {
                    String[] bds = getBdsServiceInfo(serviceNodeValueMap, serviceNodeName);
                    provideList.add(bds);
                }
            }
        }
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceType(serviceType);
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setProvideList(provideList);
        serviceInfo.setConsumerList(consumerList);
        result.add(serviceInfo);
        return result;
    }

    @SuppressWarnings("unchecked")
    public String[] getBdsServiceInfo(Map<String, Object> serviceNodeValueMap, String serviceNodeName) {
        String[] bdsData = new String[5];
        bdsData[0] = serviceNodeName;
        bdsData[1] = (null == serviceNodeValueMap.get("status")) ? null : (String) serviceNodeValueMap.get("status");
        bdsData[2] = (null == serviceNodeValueMap.get("type")) ? null : (String) serviceNodeValueMap.get("type");
        try {
            Map<String, Object> featureMap = (Map<String, Object>) serviceNodeValueMap.get("features");
            bdsData[3] = (String) featureMap.get("dbsource");
            bdsData[4] = (String) featureMap.get("ability");
        } catch (ClassCastException e) {
            bdsData[3] = "";
            bdsData[4] = "";
        }
        return bdsData;
    }

    @SuppressWarnings("unchecked")
    public String[] getCsnServiceInfo(Map<String, Object> serviceNodeValueMap) {
        String[] csnData = new String[3];
        String url = (String) serviceNodeValueMap.get("url");
        String name = (String) serviceNodeValueMap.get("serviceName");
        // serviceName-ip
        if (url.startsWith("dubbo")) {
            name = name.substring(0, name.indexOf("@"));
            csnData[0] = url.substring(url.indexOf("//") + 2, url.indexOf(name) - 1);
        }
        // status
        String status = (null == serviceNodeValueMap.get("status")) ? null : (String) serviceNodeValueMap.get("status");
        csnData[1] = status;
        // center
        try {
            csnData[2] = (String) ((Map<String, Object>) serviceNodeValueMap.get("features")).get("center");
        } catch (ClassCastException e) {
            csnData[2] = "";
        }
        return csnData;
    }

    /**
     * ignite节点信息
     *
     * @param serviceType
     * @param serviceNodeValueMap
     * @return
     * @throws Exception
     */
    public IgniteNode getIgniteNodeInfo(String serviceType, String serviceName) throws Exception {
        List<String> igniteNodes = zk.getChildren(ignitePath, false);

        if (igniteNodes == null || igniteNodes.size() < 0) {
            return null;
        }
        IgniteNode igniteNode = new IgniteNode();
        if (zk.exists(ignitePath + "/" + serviceName + "/ignite_worker", false) != null) {
            List<String> workerList = zk.getChildren(ignitePath + "/" + serviceName + "/ignite_worker", false);
            igniteNode.setWorkerList(workerList);
        }
        if (zk.exists(ignitePath + "/" + serviceName + "/ignite_sevrer", false) != null) {
            List<String> sevrerList = zk.getChildren(ignitePath + "/" + serviceName + "/ignite_sevrer", false);
            igniteNode.setServrerList(sevrerList);
        }
        igniteNode.setServiceType(serviceType);
        return igniteNode;
    }

    public List<String> getServiceName(String inputServiceType)
            throws KeeperException, InterruptedException, IOException {
        List<String> serviceNameList = new ArrayList<>();
        // 连接
        connectZookeeper();
        // 初始化路径
        parseNeedReadServicesPath();
        // 根据ServiceType获取对应路径
        String realPath = getRealPathByServiceType(inputServiceType);
        // 获取指定节点下名称
        String serviceName = "";
        List<String> childList = zk.getChildren(realPath, false);
        for (String childName : childList) {
            if (childName.indexOf(".") != -1) {
                serviceName = childName.substring(childName.lastIndexOf(".") + 1);
            } else {
                serviceName = childName;
            }
            if (!serviceNameList.contains(serviceName)) {
                serviceNameList.add(serviceName);
            }
        }
        return serviceNameList;
    }

    /**
     * 截取路径中serviceType
     *
     * @param path
     * @return
     */
    public String interceptServiceType(String path) {
        String serviceType = "";
        if (path.lastIndexOf("/") != -1) {
            if (path.lastIndexOf("-") != -1) {
                serviceType = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("-"));
            } else {
                serviceType = path.substring(path.lastIndexOf("/") + 1);
            }
        } else {
            serviceType = path;
        }
        return serviceType;
    }

    /**
     * 根据ServiceType获取对应路径
     *
     * @param serviceType
     * @return
     */
    public String getRealPathByServiceType(String serviceType) {
        String realPath = "";
        for (String path : needReadPathList) {
            String serviceTypeParam = interceptServiceType(path);
            if (serviceType.equals(serviceTypeParam)) {
                realPath = path;
                break;
            }
        }
        return realPath;
    }

    /**
     * 获取完整serviceName
     *
     * @param realPath
     * @param serviceName
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getFullServiceName(String realPath, String serviceName) throws KeeperException, InterruptedException {
        List<String> childList = zk.getChildren(realPath, false);
        String fullServiceNamePath = "";
        for (String childName : childList) {
            String serviceNameParam = "";
            if (childName.indexOf(".") != -1) {
                serviceNameParam = childName.substring(childName.lastIndexOf(".") + 1);
            } else {
                serviceNameParam = childName;
            }
            if (serviceNameParam.equals(serviceName)) {
                fullServiceNamePath = childName;
                break;
            }
        }
        return fullServiceNamePath;
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

    /**
     * 连接zookeeper
     *
     * @return
     * @throws IOException
     */
    // protected ZooKeeper connectZookeeper() throws IOException {
    // if (null == zk) {
    // zk = super.connectZookeeper();
    // zk.addAuthInfo("digest", "admin:fmbs3_adm".getBytes());
    // }
    // return zk;
    // }
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Map<String, Object> getAppStatusByIp(List<String> list) throws Exception {
        if (childNodeList.isEmpty() || childNodeList == null) {
            logger.info("zk childNodeList data is null");
            long s1 = System.currentTimeMillis();
            getNodePath("/config");
            long s2 = System.currentTimeMillis();
            logger.info("reload data use time:" + (s2 - s1));
        }

//		List<Map<String, Object>> results = new ArrayList<>();
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		ExecutorService pool = Executors.newFixedThreadPool(25);
//		Future future = (Future) pool.submit(new Callable<List<AppNodeInfo>>() {
//
//			@Override
//			public List<AppNodeInfo> call() throws Exception {
//				return getZkNodeInfo();
//			}
//		});
//
//		List<AppNodeInfo> appNodeInfos = (List<AppNodeInfo>) future.get();
//		pool.shutdown();
//		while (true) {
//			if (pool.isTerminated()) {
//				// 解析数据完成,提取筛选数据
//				results = getTargetAppInfo(list, appNodeInfos);
//				break;
//			}
//			Thread.sleep(200);
//		}
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 读取zk节点信息
        List<AppNodeInfo> appNodeInfos = getZkNodeInfoByPool();

        // 筛选数据
        List<Map<String, Object>> results = getTargetAppInfo(list, appNodeInfos);
        resultMap.put("items", results);
        return resultMap;
    }

    public List<Map<String, Object>> getTargetAppInfo(List<String> ips, List<AppNodeInfo> appNodeInfos) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<AppDeployInfo> deployInfos = appViewDao.getAppDeployInfos();

        for (String ip : ips) {
            List<String> results = new ArrayList<>();
            List<String> appNames = getDBConfigAppInfo(ip, deployInfos);
            for (AppNodeInfo appNodeInfo : appNodeInfos) {
                if (ip.equals(appNodeInfo.getIp()) && appNames.contains(appNodeInfo.getNodeName())) {
                    results.add(appNodeInfo.getNodeName() + "," + appNodeInfo.getState());
                }
            }
            Map<String, Object> map = new HashMap<>();
            if (null == results || results.size() == 0) {
                map.put("ip", ip);
                map.put("instances", "");
                map.put("hostError", "no find host info");
            } else {
                map.put("ip", ip);
                map.put("instances", results);
            }
            list.add(map);
        }
        return list;
    }

    public List<String> getDBConfigAppInfo(String ip, List<AppDeployInfo> deployInfos) {
        List<String> appNames = new ArrayList<>();
        for (AppDeployInfo appDeployInfo : deployInfos) {
            if (ip.equals(appDeployInfo.getIp())) {
                appNames.add(appDeployInfo.getAppStanceName());
            }
        }
        return appNames;
    }

    /**
     * 扫描run_state节点目录
     *
     * @param path
     * @throws Exception
     */
    public static void getNodePath(String path) throws Exception {
        if (path.endsWith("run_state")) {
            childNodeList.add(path);
        }
        if (null == zk.exists(path, false)) {
            return;
        }
        List<String> childList = zk.getChildren(path, false);
        if (childList.isEmpty() || childList == null) {
            return;
        }
        for (String child : childList) {
            if (child.equals("/")) {
                getNodePath(path + child);
            } else {
                getNodePath(path + "/" + child);
            }
        }
    }


    public List<AppNodeInfo> getZkNodeInfoByPool() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        List<AppNodeInfo> appNodeInfos = new ArrayList<>();
        for (final String child : childNodeList) {
            Future future = pool.submit(new Callable<AppNodeInfo>() {

                @Override
                public AppNodeInfo call() throws Exception {
                    return getZkNodeInfo(child);
                }
            });
            AppNodeInfo appNodeInfo = (AppNodeInfo) future.get();
            if (null != appNodeInfo) {
                appNodeInfos.add(appNodeInfo);
            }
        }
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                return appNodeInfos;
            }
            Thread.sleep(200);
        }
    }

    public AppNodeInfo getZkNodeInfo(String path) throws Exception {
        String nodeData = this.getZkNodeData(path);
        if (StringUtils.isEmpty(nodeData)) {
            return null;
        }
        JsonNode jsonNode = JacksonHelper.readJson(nodeData);
        String ip = jsonNode.get("ip").asText();
        String state = jsonNode.get("state").asText();
        String[] strs = path.split("/");
        String nodeName = strs[strs.length - 2];
        // 如果没有子节点，说明没启动
        List<String> nextChildNode = zk.getChildren(path, false);
        if (null == nextChildNode || nextChildNode.size() == 0) {
            state = "stop";
        }
        AppNodeInfo appNodeInfo = new AppNodeInfo();
        appNodeInfo.setIp(ip);
        appNodeInfo.setNodeName(nodeName);
        appNodeInfo.setState(state);
        return appNodeInfo;
    }

//	public List<AppNodeInfo> getZkNodeInfo() throws Exception {
//
//		List<AppNodeInfo> appNodeInfos = new ArrayList<>();
//		AppNodeInfo appNodeInfo = null;
//
//		for (String child : childNodeList) {
//			long begin=System.currentTimeMillis();
//			
//			String nodeData = this.getZkNodeData(child);
//			if (StringUtils.isEmpty(nodeData)) {
//				continue;
//
//			}
//			JsonNode jsonNode = JacksonHelper.readJson(nodeData);
//			String ip = jsonNode.get("ip").asText();
//			String state = jsonNode.get("state").asText();
//			String[] strs = child.split("/");
//			String nodeName = strs[strs.length - 2];
//			// 如果没有子节点，说明没启动
//			List<String> nextChildNode = zk.getChildren(child, false);
//			if (null == nextChildNode || nextChildNode.size() == 0) {
//				state = "stop";
//			}
//			appNodeInfo = new AppNodeInfo();
//			appNodeInfo.setIp(ip);
//			appNodeInfo.setNodeName(nodeName);
//			appNodeInfo.setState(state);
//			appNodeInfos.add(appNodeInfo);
//			
//			long end=System.currentTimeMillis();
//			logger.info("当次耗时："+(end-begin));
//		}
//		return appNodeInfos;
//	}

}
