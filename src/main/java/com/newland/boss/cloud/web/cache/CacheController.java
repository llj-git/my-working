package com.newland.boss.cloud.web.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.re.core.util.StringUtils;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.cloud.web.pojo.JsonData;
import com.newland.boss.cloud.web.util.CsvUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CacheController {

	private static final String SEPARAT_LEVEL1 = ";";
	private static final String SEPARAT_LEVEL2 = ",";
	private static final String SEPARAT_LEVEL3 = "=";
	private static final String SEPARAT_LEVEL4 = ":";
	private static final String SEPARAT_LEVEL7 = "NgAccountBillItemDetail";
	private static final String SEPARAT_LEVEL8 = "";
	private static final String SEPARAT_LEVEL9 = ":";
	private static final String SEPARAT_LEVEL10 = ":\"";
	private static final String SEPARAT_LEVEL11 = ",";
	private static final String SEPARAT_LEVEL12 = "\",";
	private static final String SEPARAT_LEVEL13 = "]\",";
	private static final String SEPARAT_LEVEL14 = "\"],";
	private static final String SEPARAT_LEVEL15 = "],]";
	private static final String SEPARAT_LEVEL16 = "]";
	private static Log logger = LogFactory.getLog(CacheController.class);

	private static String addr = "";
	private static String[] strArr;
	// 读配置文件方式
	// static {
	// try {
	// addr = AppConfig.getProperty("B.ignite.service.ip");
	// } catch (Exception ex) {
	// logger.error("读取ignite配置文件异常", ex);
	// }
	// }
	
	public void Agency(){
		String ip = AppConfig.getProperty("AgencyIP");
		String port = AppConfig.getProperty("AgencyPort");
		System.out.println(ip);
		System.getProperties().setProperty("proxySet", "true"); 
		 //代理服务器地址
		 System.getProperties().setProperty("http.proxyHost", ip); 
		 //代理端口  
		 System.getProperties().setProperty("http.proxyPort", port); 
    }
	

	/**
	 * 修改服务IP和端口
	 *
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/setIp", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String setIp(@RequestBody String reqJson) {
		logger.info("setIp reqJson:" + reqJson);
		String respJson = "";
		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String ip = (String) params.get("ip");
		String port = (String) params.get("port");
		if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
			logger.error("参数不合法！ ip: " + ip + "  port: " + port);
			return reqJson;
		}
		addr = ip + ":" + port;
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + addr + "/ignite?cmd=cachelist";
		respJson = restTemplate.getForObject(url, String.class, new Object[] {});
				
		if (respJson == "" || respJson == null) {
			respJson = "success";
		}
		return respJson;
	}

	/**
	 * 获取AB中心IP
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/Centerqry", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String Centerqry(@RequestBody String reqJson) {
		logger.info("Centerqry reqJson:" + reqJson);
		String respJson = "";
		String addIp = "";
		Map<String, Object> params;
		JSONObject obj = new JSONObject();
		JSONObject boj = new JSONObject();
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}
		String centerName = String.valueOf(params.get("centerName"));
		try {
			addr = AppConfig.getProperty("A.ignite.service.ip");
			addIp = AppConfig.getProperty("B.ignite.service.ip");
		} catch (Exception ex) {
			logger.error("读取ignite配置文件异常", ex);
		}

		if (StringUtils.isEmpty(centerName)) {
			logger.error("参数不合法！ centerName: " + centerName);
			return reqJson;
		}
		String[] cachesArr = addr.split(",");
		for (int i = 0; i < cachesArr.length; i++) {
			String str = "A" + String.valueOf(i);
			obj.put(str, cachesArr[i]);
			System.out.println("cachesArr[i]:" + cachesArr[i]);
		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("Acenter", obj);

		cachesArr = addIp.split(",");
		for (int i = 0; i < cachesArr.length; i++) {
			String str = "B" + String.valueOf(i);
			boj.put(str, cachesArr[i]);
			System.out.println("cachesArr[i]:" + cachesArr[i]);
		}
		resMap.put("Bcenter", boj);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("respJson:" + respJson);
		return respJson;
	}

	/**
	 * 连接AB服务IP
	 *
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/GetIpcon", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String GetIpcon(@RequestBody String reqJson) {
		logger.info("setIp reqJson:" + reqJson);
		String respJson = "";
		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String ip = (String) params.get("ip");
		if (StringUtils.isEmpty(ip)) {
			logger.error("参数不合法！ ip: " + ip);
			return reqJson;
		}
		addr = ip;
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + addr + "/ignite?cmd=cachelist";
		respJson = restTemplate.getForObject(url, String.class, new Object[] {});
		if (respJson == "" || respJson == null) {
			respJson = "success";
		}
		return respJson;
	}

	/**
	 * 获取初始目录
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getCatheList", method = RequestMethod.POST)
	public String getCatheList(@RequestBody String reqJson) {
		logger.info("初始目录请求时间:" + System.currentTimeMillis());
		logger.info("getCatheList reqJson:" + reqJson);
		String respJson = "";
		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cachelist";
		logger.info("初始目录http请求时间:" + System.currentTimeMillis());
		String caches = restTemplate.getForObject(url, String.class, new Object[] {});
		logger.info("初始目录http响应时间:" + System.currentTimeMillis());
		String[] cachesArr = caches.split(SEPARAT_LEVEL1);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("cacheNames", cachesArr);
		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("初始目录响应时间:" + System.currentTimeMillis());
		return respJson;
	}

	/**
	 * 获取cachename提示字段
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/CachePk", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String CachePk(@RequestBody String reqJson) {
		logger.info("cachename请求时间:" + System.currentTimeMillis());
		logger.info("CachePk reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cachepk&cachename=";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		if (StringUtils.isEmpty(cacheName)) {
			logger.error("参数不合法！ cacheName: " + cacheName);
			return reqJson;
		}
		url = url + cacheName;
		logger.info("CachePk url: " + url);
		logger.info("cachename的http请求时间:" + System.currentTimeMillis());
		String values = restTemplate.getForObject(url, String.class, new Object[] {});
		logger.info("cachename的http响应时间:" + System.currentTimeMillis());
		String[] cachesArr = values.split(SEPARAT_LEVEL1);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("cacheCommit", cachesArr);
		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("cachename的响应时间:" + System.currentTimeMillis());
		return respJson;
	}

	/**
	 * 获取pk的值
	 *
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/queryPk", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String queryPk(@RequestBody String reqJson) {
		logger.info("queryPk reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cacheget";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		String key = (String) params.get("key");
		if (StringUtils.isEmpty(cacheName) || StringUtils.isEmpty(key)) {
			logger.error("参数不合法！ cacheName: " + cacheName + "  key: " + key);
			return reqJson;
		}
		url = url + "&cachename=" + cacheName + "&key=" + key;
		logger.info("queryPk url: " + url);
		String values = restTemplate.getForObject(url, String.class, new Object[] {});

		String flag = values.split("\\{")[0];
		values = values.replaceAll(flag, "");
		values = values.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL2);
		values = values.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL4);
		values = "[" + values + "]";
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", values);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson --> " + respJson);
		return respJson;
	}

	/**
	 * 获取A中心ignitestate
	 *
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getIgniteStateACenter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getIgniteStateACenetr(@RequestBody String reqJson,HttpSession session) {
		logger.info("getIgniteState reqJson:" + reqJson);

		String cmd = "ignite";
		String respJson = getDate(session,cmd,"A");
		logger.info("getIgniteState --> " + respJson);
		return respJson;
	}


	/**
	 * 获取B中心ignitestate
	 *
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getIgniteStateBCenter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getIgniteStateBCenetr(@RequestBody String reqJson,HttpSession session) {
		logger.info("getIgniteState reqJson:" + reqJson);

		String cmd = "ignite";
		String respJson = getDate(session,cmd,"B");
		logger.info("getIgniteState --> " + respJson);
		return respJson;
	}

	/**
	 * 获取数据网格信息ACenter
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getDataGridACenter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getDatagridACenter(@RequestBody String reqJson,HttpSession session) {
		logger.info("数据网格信息请求时间:" + System.currentTimeMillis());
		logger.info("getIgniteState reqJson:" + reqJson);

		String cmd = "datagrid";
		String respJson = getDate(session,cmd,"A");

		logger.info("getDatagrid --> " + respJson);
		return respJson;
	}

	/**
	 * 获取数据网格信息BCenter
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getDataGridBCenter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getDatagridBCenter(@RequestBody String reqJson,HttpSession session) {
		logger.info("数据网格信息请求时间:" + System.currentTimeMillis());
		logger.info("getIgniteState reqJson:" + reqJson);
		String cmd = "datagrid";
		String respJson = getDate(session,cmd,"B");
		logger.info("getDatagrid --> " + respJson);
		return respJson;
	}

	/**
	 * 获取集群节点列表
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/nodeList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String nodeList(@RequestBody String reqJson) {
		logger.info("nodeList reqJson:" + reqJson);

		String cmd = "nodelist";
		String respJson = getRespJson(cmd);

		logger.info("nodeList --> " + respJson);
		return respJson;
	}

	/**
	 * 获取集群数据节点列表
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/serverNodeList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String serverNodeList(@RequestBody String reqJson) {
		logger.info("nodeList reqJson:" + reqJson);
		String cmd = "nodeserverlist";
		String respJson = getRespJson(cmd);
		respJson = respJson.replace("startTime:", "startTime:'").replace("upTime:", "upTime:'").replace(", upTime", "', upTime").replace(", busyTimePerc", "', busyTimePerc");
		logger.info("nodeList --> " + respJson);
		return respJson;
	}

	/**
	 * 获取集群节点id集合
	 * 
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getNodeIds", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getNodeIds(@RequestBody String reqJson) {
		logger.info("getNodeIds reqJson:" + reqJson);

		String cmd = "nodelist";
		String result = getResult(cmd);
		String[] resultarr = result.split(SEPARAT_LEVEL2);
		String idsStr = "";
		// for (int i = 1; i < resultarr.length; i++) {
		// if (i > 1) {
		// idsStr += ",";
		// }
		// idsStr += resultarr[i].split(",")[0];
		// }
		// idsStr = idsStr.replaceAll("\'", "\"");
		// String idarrStr = "[" + idsStr + "]";

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("cacheNames", resultarr);

		String respJson = "";
		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("getNodeIds --> " + respJson);
		return respJson;
	}

	/**
	 * 根据nodeId获取集群数据节点列表
	 * 
	 * @param reqJson
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/getNodeDetails", method =
	 * RequestMethod.POST,produces="text/html;charset=UTF-8") public String
	 * getNodeDetails(@RequestBody String reqJson) { logger.info(
	 * "getNodeDetails reqJson:" + reqJson);
	 * 
	 * 
	 * logger.info("getNodeDetails --> "+respJson); return respJson; }
	 */

	/**
	 * 转换成动态表格的格式 数组 [{对象属性key:值,对象属性key:值},{对象属性key:值,对象属性key:值}]
	 * [{"name":"john","age":12},{"name":"peter","age":12}]
	 * 
	 * @param str
	 * @return
	 */
	private String tableConvert(String str) {
		// domain.toString若以"[]" 而不是"{}"
		// str=str.replaceAll("\\[","\\{");//将"["换成"{",为了动态表格；
		// str=str.replaceAll("]","}");

		// ip:'[10.1.8.105, 127.0.0.1]', hostName:'[boss105]'
		// 先把源str中的[] 去掉，免得影响动态表格的数组格式
		// str=str.replaceAll("\\[","");
		// str=str.replaceAll("]","");

		// domain.toString的开头若是类名,去掉

		if (StringUtils.isNotBlank(str)){
			String flag = str.split("\\{")[0];
			str = str.replaceAll(flag, "");
			// 后端对象属性key和值用"="分离，改为用":"
			str = str.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL4);

			// 后端对象和对象用";"分离，改为用","
			str = str.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL2);

			// 如果多个对象，则最后一位多了一个","(";"替换成",") 去掉
			if (",".equals(str.substring(str.length() - 1))) {
				str = str.substring(0, str.length() - 1);
			}

			// 动态表格，传回一个数组，用"[]"
			str = "[" + str + "]";
		}

		return str;
	}

	/**
	 * 通过对应的cmd发送re'stful
	 * 
	 * @param cmdAndParas
	 * @return
	 */
	private String getResult(String cmdAndParas) {
		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";



		String url = "http://" + addr + "/ignite?cmd=" + cmdAndParas;
		logger.info("url: " + url);

		String result = restTemplate.getForObject(url, String.class, new Object[] {});
		System.out.println("result:"+result);
		return result;
	}

	/**
	 * 获取结果并转换 ，适用于一定格式（tableConvert） 且返回为writeBean2Json 结果存在'resultValue'中
	 * 
	 * @param cmdAndParas
	 * @return
	 */
	private String getRespJson(String cmdAndParas) {
		String result = getResult(cmdAndParas);
		result = tableConvert(result);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("resultValue", result);
		String respJson = "";
		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		return respJson;
	}

	/**
	 * 获取节点信息列表的值
	 * 
	 * @author lcj
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getNodeDetails", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getNodeDetails(@RequestBody String reqJson) {
		logger.info("节点信息列表:" + System.currentTimeMillis());
		logger.info("getNodeDetails reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=nodeget";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String nodeId = (String) params.get("nodeId");
		if (StringUtils.isEmpty(nodeId)) {
			logger.error("参数不合法！ nodeId: " + nodeId);
			return reqJson;
		}
		url = url + "&hostname=" + nodeId;
		logger.info("getNodeDetails url: " + url);
		logger.info("节点信息列表http请求时间:" + System.currentTimeMillis());
		String values = restTemplate.getForObject(url, String.class, new Object[] {});
		logger.info("节点信息列表http响应时间:" + System.currentTimeMillis());
//		String flag = values.split("\\{")[0];
//		values = values.replaceAll(flag, "");
		values = values.replaceAll("NodeInfo", "");
		values = values.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL2);
		values = values.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL4);
		String substring = values.substring(0, values.length()-2);
		values = values.replace("startTime:", "startTime:'").replace("upTime:", "upTime:'").replace(", upTime", "', upTime").replace(", busyTimePerc", "', busyTimePerc");
		values = values.substring(0,values.length()-1);
		substring = "[" + values + "]";

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", substring);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}

		logger.info("respJson --> " + respJson);
		logger.info("节点信息列表响应时间:" + System.currentTimeMillis());
		return respJson;
	}

	/**
	 * 获取缓存指标明细
	 * 
	 * @author lcj
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getCacheinfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getCacheinfo(@RequestBody String reqJson) {
		logger.info("getCacheinfo reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cacheinfo";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		if (StringUtils.isEmpty(cacheName)) {
			logger.error("参数不合法！ cachename: " + cacheName);
			return reqJson;
		}
		url = url + "&cachename=" + cacheName;
		logger.info("getCacheinfo url: " + url);
		String values = restTemplate.getForObject(url, String.class, new Object[] {});

		String flag = values.split("\\{")[0];
		values = values.replaceAll(flag, "");
		values = values.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL2);
		values = values.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL4);
		// values=values.replaceAll("cacheName","\"cacheName");
		values = "[" + values + "]";
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", values);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson --> " + respJson);
		return respJson;
	}

	/**
	 * 获取缓存配置明细
	 * 
	 * @author lcj
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getCachecfg", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getCachecfg(@RequestBody String reqJson) {
		logger.info("getCachecfg reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cachecfg";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		if (StringUtils.isEmpty(cacheName)) {
			logger.error("参数不合法！ cacheName: " + cacheName);
			return reqJson;
		}
		url = url + "&cachename=" + cacheName;
		logger.info("getCacheinfo url: " + url);
		String values = restTemplate.getForObject(url, String.class, new Object[] {});
		String flag = values.split("\\{")[0];
		String as1 = "\"name\":";
		String as2 = "\"writeSync\":";
		String as3 = "\"cacheMode\":";
		String as4 = "\"atomicityMode\":";
		String as5 = "\"rebalanceMode\":";
		String as6 = "\"affMapper\":";
		String as7 = "\"partLossPlc\":";
		String as8 = ",\"grpName";
		String as9 = ",\"storeFactory";
		String as10 = ",\"atomicityMode\":";
		String as11 = ",\"rebalanceOrder";
		String as12 = ",\"rebalanceDelay";
		String as13 = ",\"qryParallelism";
		String as14 = "\"";

		String as15 = "RendezvousAffinityFunction";
		String as16 = ",\"cacheMode";
		String as17 = "IgniteAllNodesPredicate";
		String as18 = "ATOMIC,";
		String[] name = { as1, as2, as3, as4, as5, as6, as7, as8, as9, as10, as11, as12, as13 };
		values = values.replaceAll(flag, "");
		// values = values.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL5);
		// values = values.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL6);
		values = values.replaceAll("CacheConfiguration ", "");
		values = values.replaceAll("\\{", "{\"");
		values = values.replaceAll(", ", ",\"");
		values = values.replaceAll("=", "\":");
		System.out.println("values6:" + values);
		for (int i = 1; i < 8; i++) {
			String operation = String.valueOf(as14) + name[i - 1];
			System.out.println("operation:" + operation);
			values = values.replaceAll(name[i - 1], name[i - 1] + String.valueOf(as14));
		}
		for (int i = 8; i < 14; i++) {
			if (String.valueOf(i).equals("10")) {
				System.out.println("name[i-1]:" + name[i - 1]);
				values = values.replaceAll(name[i - 1], String.valueOf(as14) + name[i - 1]);
				values = values.replaceAll(as18, "ATOMIC\",");
			} else {
				values = values.replaceAll(name[i - 1], String.valueOf(as14) + name[i - 1]);
			}
		}
		values = values.replaceAll(as15, "{\"" + as15 + "\":");
		values = values.replaceAll(as16, "}" + as16);
		values = values.replaceAll(as17, "{\"" + as17 + "\":");
		values = values.replaceAll("\\{\"\\},\"sqlSchema", "{}},\"sqlSchema");
		values = values.replaceAll("\\}\\{\"IgniteAllNodesPredicate\"", "{\"IgniteAllNodesPredicate\"");
		// values=values.replaceAll(SEPARAT_LEVEL7,SEPARAT_LEVEL6);
		values = "[" + values + "]";
		System.out.println("values:" + values);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", values);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson --> " + respJson);
		return respJson;
	}

	/**
	 * 获取cacheName的domain字段
	 * 
	 * @author lcj
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/getDomain", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getDomain(@RequestBody String reqJson) {
		logger.info("getDomain reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cachedomain";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		if (StringUtils.isEmpty(cacheName)) {
			logger.error("参数不合法！ cacheName: " + cacheName);
			return reqJson;
		}
		url = url + "&cachename=" + cacheName;
		logger.info("getDomain url: " + url);
		String values = restTemplate.getForObject(url, String.class, new Object[] {});

		// String flag=values.split("\\{")[0];
		// values=values.replaceAll(flag,"");
		// values=values.replaceAll(SEPARAT_LEVEL1,SEPARAT_LEVEL5);
		// values=values.replaceAll(SEPARAT_LEVEL3,SEPARAT_LEVEL6);
		// values=values.replaceAll(SEPARAT_LEVEL5,SEPARAT_LEVEL6);
		// values=values.replaceAll(SEPARAT_LEVEL7,SEPARAT_LEVEL6);
		values = "[" + "\"" + values + "]";

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", values);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson --> " + respJson);
		return respJson;
	}

	/**
	 * sql语句查询结果
	 * 
	 * @author lcj
	 * @param reqJson
	 * @return
	 */
	@RequestMapping(value = "/submitForm", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String submitForm(@RequestBody String reqJson) {
		logger.info("submitForm reqJson:" + reqJson);
		String respJson = "";

		RestTemplate restTemplate = new RestTemplate();
		// String addr="10.48.183.57:44474";
		String url = "http://" + addr + "/ignite?cmd=cachesql";

		Map<String, Object> params;
		try {
			params = JacksonHelper.readJson2Map(reqJson);
		} catch (IOException e) {
			logger.error("参数不合法！ --> " + reqJson);
			logger.error("参数转换失败！ --> " + e);
			return reqJson;
		}

		String cacheName = (String) params.get("cacheName");
		String sql = (String) params.get("sql");
		if (StringUtils.isEmpty(cacheName)) {
			logger.error("参数不合法！ cacheName: " + cacheName);
			return reqJson;
		}
		if (StringUtils.isEmpty(sql)) {
			url = url + "&cachename=" + cacheName;
		} else {
			url = url + "&cachename=" + cacheName + "&args=" + sql;
		}
		logger.info("submitForm url: " + url);
		String values = restTemplate.getForObject(url, String.class, new Object[] {});

		String flag = values.split("\\{")[0];
		values = values.replaceAll(flag, "");
		values = values.replaceAll(SEPARAT_LEVEL1, SEPARAT_LEVEL2);
		values = values.replaceAll(SEPARAT_LEVEL3, SEPARAT_LEVEL4);
		values = values.replaceAll(SEPARAT_LEVEL7, SEPARAT_LEVEL8);
		values = values.replaceAll(SEPARAT_LEVEL9, SEPARAT_LEVEL10);
		values = values.replaceAll(SEPARAT_LEVEL11, SEPARAT_LEVEL12);
		values = values.replaceAll(SEPARAT_LEVEL13, SEPARAT_LEVEL14);
		values = values.replaceAll(SEPARAT_LEVEL13, SEPARAT_LEVEL14);
		values = values + "]";
		values = values.replaceAll(SEPARAT_LEVEL15, SEPARAT_LEVEL16);

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("value", values);

		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		logger.info("respJson --> " + respJson);
		return respJson;
	}


	/**
	 * 分别获取AB中心的数据
	 * @param session
	 */
	public String getDate(HttpSession session,String cmdAndParas,String center){

		String ACenter=null;
		String BCenter=null;
		String json=null;

		try {
			ACenter = AppConfig.getProperty("A.ignite.service.ip");
			BCenter = AppConfig.getProperty("B.ignite.service.ip");
		} catch (Exception ex) {
			logger.error("读取ignite配置文件异常", ex);
		}

		String[] aSplit = ACenter.split(",");

		String[] bSplit = BCenter.split(",");

		if (center.equals("A")){

			for (int i = 0; i < aSplit.length; i++) {

				json = getJson(aSplit[i], cmdAndParas);

				JsonData jsonData = JSON.parseObject(json, JsonData.class);

				if (StringUtils.isNotBlank(jsonData.getResultValue())){

					break;

				}

			}

		}else if (center.equals("B")){


			for (int i = 0; i < bSplit.length; i++) {

				json = getJson(bSplit[i], cmdAndParas);

				JsonData jsonData = JSON.parseObject(json, JsonData.class);

				if (StringUtils.isNotBlank(jsonData.getResultValue())){

					break;

				}
			}
		}


		return json;
	}

	/**
	 * 通过对应的cmd发送re'stful
	 *
	 * @param cmdAndParas
	 * @return
	 */
	private String getABResult(String center,String cmdAndParas) {
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://" + center + "/ignite?cmd=" + cmdAndParas;
		logger.info("url: " + url);
		String result=null;

		try {
			result = restTemplate.getForObject(url, String.class, new Object[]{});
		}catch (Exception e){

			logger.info("can't get data by"+center);
		}


		return result;
	}

	private String getJson(String center,String cmdAndParas) {
		String result = getABResult(center,cmdAndParas);
		result = tableConvert(result);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("resultValue", result);
		String respJson = "";
		try {
			respJson = JacksonHelper.writeBean2Json(resMap);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		return respJson;
	}

	@RequestMapping(value = "exporCsv")
	public void exportCsv(HttpServletResponse response, HttpServletRequest request, String data) throws IOException {


		JSONArray jsonArray = (JSONArray) JSON.parse(data, Feature.OrderedField);


		LinkedHashMap<String, String> header = new LinkedHashMap<>();

		List<LinkedHashMap<String, Object>> exportData = new ArrayList<>();

		for (int i = 0; i < jsonArray.size(); i++) {
			LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();

			for (Map.Entry<String, Object> entry : entries) {


				String key = entry.getKey();
				Object value = entry.getValue();

				if (!key.equals("undefined")&&i==0){
					header.put(key,key);
					rowData.put(key,value);
				}else if (!key.equals("undefined")&&i!=0){
					rowData.put(key,value);
				}

			}

			exportData.add(rowData);

		}

//		String path = "D:/export/";
//		String fileName = "这里是一个CSV文件";

		//将其生成一个excel文件，输出，保证每次生成的文件都不一样，则使用时间日期命名
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HHmmss");
		Calendar calendar=Calendar.getInstance();
		String excelName=sFormat.format(calendar.getTime());

		String path = request.getSession().getServletContext().getRealPath("/resources/download/");

		path=path.replaceAll("\\\\","/");

		String fileURL=path+excelName+".csv";

		HttpSession session = request.getSession();

		session.setAttribute("fileURL",fileURL);

		CsvUtil.createCSVFile(exportData,header,path,excelName);


		// csv文件名字，为了方便默认给个名字，当然名字可以自定义，看实际需求了
//		String fileName = "data.csv";
//		// 解决不同浏览器出现的乱码
//		fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
//		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//		response.setHeader("Content-Disposition", "attachment; filename=" + fileName +"; filename*=utf-8" + fileName);
//		FileCopyUtils.copy(bytes, response.getOutputStream());

	}

	@RequestMapping(value = "downloadCsv")
	public void downloadCsv(HttpServletResponse response, HttpServletRequest request){

//		response.setContentType("application/csv;charset=UTF-8");
//		response.setHeader("Content-Disposition",
//				"attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//
//		InputStream in = null;
//		try {
//			in = new FileInputStream(csvFilePath);
//			int len = 0;
//			byte[] buffer = new byte[1024];
//			response.setCharacterEncoding("UTF-8");
//			OutputStream out = response.getOutputStream();
//			while ((len = in.read(buffer)) > 0) {
//				out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
//				out.write(buffer, 0, len);
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println(e);
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//			}
//		}

	}

}
