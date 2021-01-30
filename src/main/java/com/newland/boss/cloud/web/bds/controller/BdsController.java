package com.newland.boss.cloud.web.bds.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.bds.entity.AppNodeInfo;
import com.newland.boss.cloud.web.bds.entity.BdsGroup;
import com.newland.boss.cloud.web.bds.entity.ComparatorAppNodeInfo;
import com.newland.boss.cloud.web.bds.entity.ServiceGroup;
import com.newland.boss.cloud.web.bds.entity.ServiceInfo;
import com.newland.boss.cloud.web.bds.service.ZkMgr;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.sri.ccp.rtc.bds.bdsswitch.BdsSwitchImpl;

@RestController
public class BdsController {

	private static Log logger = LogFactory.getLog(BdsController.class);
	@Autowired
	private ZkMgr zkMgr;

	private static ComparatorAppNodeInfo comparatorAppNodeInfo = new ComparatorAppNodeInfo();

	/**
	 * 获取bds节点列表
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getSwitchBdsList", method = RequestMethod.POST)
	public String getSwitchBdsList(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		try {
			List<AppNodeInfo> nodeInfoList = zkMgr.getNeedSwitchNodeList();
			Collection<BdsGroup> bdsGroupList = this.getBdsGroup(nodeInfoList);
			respJson = JacksonHelper.writeBean2Json(bdsGroupList);
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 执行bds主备切换
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/doBdsSwitch", method = RequestMethod.POST)
	public String doBdsSwitch(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		StringBuffer sbRespJson = new StringBuffer();
		try {
			String zkHost = AppConfig.getProperty("zk.connect.host");
			logger.info("zkHost:" + zkHost);

			List<Map<String, Object>> reqList = JacksonHelper.readJson2ListMap(reqJson);

			String source = "";
			String from = "";
			String dest = "";
			boolean bResult = false;
			sbRespJson.append("[");

			String bdsMappingKey = null;
			String bdsNodePathMapping = null;
			String[] aBdsNodePathMapping = null;
			BdsSwitchImpl bdsSwitch = null;
			for (Map<String, Object> reqMap : reqList) {
				source = (String) reqMap.get("source");
				from = (String) reqMap.get("from");
				dest = (String) reqMap.get("dest");

				if (source.startsWith("bdsna")) {
					bdsMappingKey = "bds.node.path.mapping.a";
				} else if (source.startsWith("bdsnb")) {
					bdsMappingKey = "bds.node.path.mapping.b";
				} else {
					bdsMappingKey = "bds.node.path.mapping";
				}
				bdsNodePathMapping = AppConfig.getProperty(bdsMappingKey);
				aBdsNodePathMapping = StringUtils.split(bdsNodePathMapping, ",");
				bdsSwitch = new BdsSwitchImpl(aBdsNodePathMapping);

				bResult = bdsSwitch.doSwitch(zkHost, source, from, dest);
				sbRespJson.append("{");
				sbRespJson.append("\"source\":");
				sbRespJson.append("\"" + source + "\"");
				sbRespJson.append(",\"result\":");
				sbRespJson.append(bResult);
				sbRespJson.append("},");
			}
			sbRespJson.deleteCharAt(sbRespJson.length() - 1);
			sbRespJson.append("]");
		} catch (Throwable e) {
			logger.error("", e);
			sbRespJson.setLength(0);
			sbRespJson.append("{");
			sbRespJson.append("\"error\":");
			sbRespJson.append("\"" + e.getMessage() + "\"");
			sbRespJson.append("}");
		}

		logger.info("respJson:" + sbRespJson.toString());
		return sbRespJson.toString();
	}

	/**
	 * 获取云化应用名称列表
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getAppList", method = RequestMethod.POST)
	public String getAppList(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		Map<String, String> appPathMapping = zkMgr.parsePathMapping();
		Set<String> appNameSet = appPathMapping.keySet();
		try {
			respJson = JacksonHelper.writeBean2Json(appNameSet);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 根据应用名称获取所有应用节点
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getAppNodeList", method = RequestMethod.POST)
	public String getAppNodeList(@RequestBody String reqJson) {
		logger.info("reqJson:" + reqJson);
		String respJson = "";
		Map<String, Object> map;
		try {
			map = JacksonHelper.readJson2Map(reqJson);
			Map<String, String> appPathMapping = zkMgr.parsePathMapping();
			String appPath = appPathMapping.get(map.get("appName"));
			// 移除备用节点信息
			List<AppNodeInfo> newAppNodeInfoList = zkMgr.removeBackNodeInfo(zkMgr.getAppNodeInfoList(appPath),
					(String) map.get("appName"));
			//测试数据
			//List<AppNodeInfo> newAppNodeInfoList = testData();
			Collections.sort(newAppNodeInfoList, comparatorAppNodeInfo);
			//停止状态显示在前，之后是ip
			respJson = JacksonHelper.writeBean2Json(newAppNodeInfoList);
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("respJson:" + respJson);
		return respJson;
	}
	
	public List<AppNodeInfo> testData(){
		List<AppNodeInfo> list=new ArrayList<>();
		AppNodeInfo aInfo1=null;
		for (int i = 0; i < 10; i++) {
			aInfo1=new AppNodeInfo();
			aInfo1.setDeployPath("/bossapp1/prg.new/na-jf-ctrl01_obds_01"+i);
			aInfo1.setIp("10.48.141.20"+i);
			aInfo1.setNodeName("na-jf-ctrl01_obds_01"+i);
			aInfo1.setState(i%2==0?"stop":"running");
			list.add(aInfo1);
		}
		return list;
	}

	/**
	 * 将应用节点列表,按数据库名进行归类
	 * 
	 * @param nodeInfoList
	 * @return
	 */
	public Collection<BdsGroup> getBdsGroup(List<AppNodeInfo> nodeInfoList) {
		Map<String, BdsGroup> tmpMap = new TreeMap<String, BdsGroup>(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		BdsGroup tmpBdsGroup = null;
		for (AppNodeInfo nodeInfo : nodeInfoList) {
			if (null == nodeInfo.getDbSource())
				continue;
			tmpBdsGroup = tmpMap.get(nodeInfo.getDbSource());
			if (null == tmpBdsGroup) {
				tmpBdsGroup = new BdsGroup();
				tmpBdsGroup.setDbSource(nodeInfo.getDbSource());
				tmpBdsGroup.setBranchDB0List(new ArrayList<AppNodeInfo>());
				tmpBdsGroup.setBranchDB1List(new ArrayList<AppNodeInfo>());
				tmpBdsGroup.setBranchDB2List(new ArrayList<AppNodeInfo>());
				tmpMap.put(nodeInfo.getDbSource(), tmpBdsGroup);
			}

			switch (nodeInfo.getServiceType()) {
			case "0":
				tmpBdsGroup.getBranchDB0List().add(nodeInfo);
				tmpBdsGroup.setIpMain(nodeInfo.getIp());
				break;
			case "1":
				tmpBdsGroup.getBranchDB1List().add(nodeInfo);
				tmpBdsGroup.setIpBak1(nodeInfo.getIp());
				break;
			case "2":
				tmpBdsGroup.getBranchDB2List().add(nodeInfo);
				tmpBdsGroup.setIpBak2(nodeInfo.getIp());
				break;
			}
		}

		// 设置主库标识
		try {
			Map<String, String> bdsMainFlagMap = zkMgr.getBdsMainFlag();
			for (Map.Entry<String, BdsGroup> entry : tmpMap.entrySet()) {
				entry.getValue().setCurrServiceType(bdsMainFlagMap.get(entry.getKey()));
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		Collection<BdsGroup> values = tmpMap.values();
		// 对结果进行排序
		for (BdsGroup bdsGroup : values) {
			Collections.sort(bdsGroup.getBranchDB0List(), comparatorAppNodeInfo);
			Collections.sort(bdsGroup.getBranchDB1List(), comparatorAppNodeInfo);
			Collections.sort(bdsGroup.getBranchDB2List(), comparatorAppNodeInfo);
		}

		return values;
	}

	/**
	 * zookeeper-services 提取服务信息表中所含有的服务类别
	 * 
	 * @param serviceInfoList
	 * @return
	 */
	public List<String> getTypeList(List<ServiceInfo> serviceInfoList) {
		List<String> list = new ArrayList<String>();
		for (ServiceInfo serviceInfo : serviceInfoList) {
			list.add(serviceInfo.getServiceType());
		}
		HashSet<String> h = new HashSet<String>(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	/**
	 * zookeeper-services 根据Key值提取相应类别的服务列表
	 * 
	 * @param serviceInfoList
	 * @param key
	 * @return
	 */
	public List<ServiceInfo> getServiceGroup(List<ServiceInfo> serviceInfoList, String key) {
		Map<String, ServiceGroup> tmpMap = new HashMap<String, ServiceGroup>();
		ServiceGroup tmpServiceGroup = null;
		for (ServiceInfo serviceInfo : serviceInfoList) {
			tmpServiceGroup = tmpMap.get(serviceInfo.getServiceType());
			if (null == tmpServiceGroup) {
				tmpServiceGroup = new ServiceGroup();
				tmpServiceGroup.setServiceType(serviceInfo.getServiceType());
				tmpServiceGroup.setServiceList(new ArrayList<ServiceInfo>());
				tmpMap.put(serviceInfo.getServiceType(), tmpServiceGroup);
			}
			tmpMap.get(serviceInfo.getServiceType()).getServiceList().add(serviceInfo);
		}
		return tmpMap.get(key).getServiceList();
	}

}
