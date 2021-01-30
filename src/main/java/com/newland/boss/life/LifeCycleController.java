package com.newland.boss.life;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.cloud.web.userInfo.dao.UserInfoDao;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.cloud.web.util.DbUtil;
import com.newland.boss.cloud.web.util.JSchUtil;


@RestController
public class LifeCycleController {

    private static Log LOGGER = LogFactory.getLog(LifeCycleController.class);

    private static UserInfoDao userInfoDao = new UserInfoDao();

    private static String execType = null;

    private static String groupType = ""; //系统id 1 物联网 0 下一代
    //	private static HashMap<String, Object> finalresultMap = new HashMap<>(); //最后一层Map
//	private static List<HashMap> finalResultMapList = new ArrayList<HashMap>(); //多个主机的情况
//	private List<HashMap> myFinalResultMapList =  myFinalResultMapList = new ArrayList<HashMap>();
//	private static List<HashMap> myFinalResultMapListSub =  myFinalResultMapListSub = new ArrayList<HashMap>();
//	private static Map<String,Object> sessionMap = new HashMap<String,Object>(); // 避免1次请求，多次登入同一台主机
//	private static List<HashMap> itemsForappname = new ArrayList<HashMap>();
    private static int isAllSystem = 0; //判断是否全系统操作

    @RequestMapping(value = "/appstart", method = RequestMethod.POST)
    public String appstart(@RequestBody String reqJson) throws Exception {

        long x = System.currentTimeMillis();

//		finalresultMap.clear();
//		finalResultMapList.clear();
//		myFinalResultMapList.clear();
//		itemsForappname.clear(); //1
//		sessionMap.clear();

        HashMap<String, Object> finalresultMap = new HashMap<>(); //最后一层Map
        List<HashMap> finalResultMapList = new ArrayList<HashMap>(); //多个主机的情况
        List<HashMap> myFinalResultMapList  = new ArrayList<HashMap>();
        List<HashMap> itemsForappname = new ArrayList<HashMap>();
        Map<String, Object> sessionMap = new HashMap<String, Object>(); // 避免1次请求，多次登入同一台主机
        List<HashMap> myFinalResultMapListSub = new ArrayList<HashMap>();

//
//	private static List<HashMap> itemsForappname = new ArrayList<HashMap>();
        //json 解析参考  JacksonHelper.readJson2MapBean
        try {
            List<HashMap> items = getRequstJson(reqJson, myFinalResultMapListSub);
            //去重整理请求参数
            reExtraItem(items, myFinalResultMapListSub); //start11
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            DoWorkTasker myTasker = new DoWorkTasker(items, "-start", "1", finalResultMapList, sessionMap, myFinalResultMapListSub);
            threadPool.submit(myTasker);
            threadPool.shutdown(); //关闭线程池


        } catch (Exception e) {
            e.printStackTrace();
            finalresultMap.put("info", "1");
            return JacksonHelper.writeBean2Json(finalresultMap);
        }
//		catch (SQLException e) {
//			finalresultMap.put("data", "数据库相关操作异常");
//			return JacksonHelper.writeBean2Json(finalresultMap);
//		}
        /****finalresultMap.put("items", finalResultMapList);
         String resultTmp=JacksonHelper.writeBean2Json(finalresultMap);
         converToNewJson();
         finalresultMap.remove("items");
         finalresultMap.put("items", myFinalResultMapList);*/

        HashMap map = new HashMap();
        map.put("info", "0");
        String result = JacksonHelper.writeBean2Json(map);
        LOGGER.info(result);
        long y = System.currentTimeMillis();
        System.out.println(execType + "耗时" + (y - x));
        return result;
    }

    @RequestMapping(value = "/appstop", method = RequestMethod.POST)
    public String appstop(@RequestBody String reqJson) throws JSchException, IOException, SQLException, Exception {

        long x = System.currentTimeMillis();
//        finalresultMap.clear();
//        finalResultMapList.clear();
//        myFinalResultMapList.clear();
//        itemsForappname.clear(); //2
//        sessionMap.clear();
        HashMap<String, Object> finalresultMap = new HashMap<>(); //最后一层Map
        List<HashMap> finalResultMapList = new ArrayList<HashMap>(); //多个主机的情况
        List<HashMap> myFinalResultMapList =  new ArrayList<HashMap>();
        List<HashMap> itemsForappname = new ArrayList<HashMap>();
        Map<String, Object> sessionMap = new HashMap<String, Object>(); // 避免1次请求，多次登入同一台主机
        List<HashMap> myFinalResultMapListSub =  new ArrayList<HashMap>();
        //json 解析参考  JacksonHelper.readJson2MapBean
        try {
            List<HashMap> items = getRequstJson(reqJson, myFinalResultMapListSub);
            //去重整理请求参数
            reExtraItem(items, myFinalResultMapListSub); //stop22
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            DoWorkTasker myTasker = new DoWorkTasker(items, "-stop", "0", finalResultMapList, sessionMap, myFinalResultMapListSub);
            threadPool.submit(myTasker);
            threadPool.shutdown(); //关闭线程池
        } catch (Exception e) {
            e.printStackTrace();
            finalresultMap.put("info", "1");
            return JacksonHelper.writeBean2Json(finalresultMap);
        }
//		    catch(SQLException e){
//		    	finalresultMap.put("data", "数据库相关操作异常");
//		    	return JacksonHelper.writeBean2Json(finalresultMap);
//			}	


        /***finalresultMap.put("items", finalResultMapList);
         String resultTmp=JacksonHelper.writeBean2Json(finalresultMap);
         converToNewJson();

         finalresultMap.remove("items");
         finalresultMap.put("items", myFinalResultMapList);***/

        HashMap map = new HashMap();
        map.put("info", "0");
        String result = JacksonHelper.writeBean2Json(map);
        //LOGGER.info(resultTmp);
        //LOGGER.info(result);
        long y = System.currentTimeMillis();
        System.out.println(execType + "耗时" + (y - x));
        return result;

    }

    @RequestMapping(value = "/appstatus", method = RequestMethod.POST)
    public String appstatus(@RequestBody String reqJson) throws JSchException, IOException, SQLException, Exception {

//        myFinalResultMapListSub.clear();
//        finalresultMap.clear();
//        finalResultMapList.clear();
//        myFinalResultMapList.clear();
//        itemsForappname.clear(); //3
//        sessionMap.clear();
        HashMap<String, Object> finalresultMap = new HashMap<>(); //最后一层Map
        List<HashMap> finalResultMapList = new ArrayList<HashMap>(); //多个主机的情况
        List<HashMap> myFinalResultMapList =  new ArrayList<HashMap>();
        List<HashMap> itemsForappname = new ArrayList<HashMap>();
        Map<String, Object> sessionMap = new HashMap<String, Object>(); // 避免1次请求，多次登入同一台主机
        List<HashMap> myFinalResultMapListSub = new ArrayList<HashMap>();

        long x = System.currentTimeMillis();

        try {
            //json 解析参考  JacksonHelper.readJson2MapBean
            List<HashMap> items = getRequstJson(reqJson, myFinalResultMapListSub);//查找数据库解析报文
            long a = System.currentTimeMillis();
            LOGGER.info(execType + "获取串耗时" + (a - x));
            System.out.println(execType + "获取串耗时" + (a - x));
//	    String result=judgeIsDo(items);
//		if(!result.equals("")){
//			return result;
//		}
            //judgeIsDoWork(items);
            //去重整理请求参数
            reExtraItem(items, myFinalResultMapListSub); //status33
            long b = System.currentTimeMillis();
            System.out.println(execType + "去重耗时" + (b - a));
//————————————————————————入口————————————————————
            doWork(items, "-psg", "", finalResultMapList, sessionMap, myFinalResultMapListSub);//工作代码
            long c = System.currentTimeMillis();
            System.out.println(execType + "工作耗时" + (c - b));
        } catch (JSchException e) {
            finalresultMap.put("data", "主机会话相关操作异常");
            return JacksonHelper.writeBean2Json(finalresultMap);
        } catch (SQLException e) {
            finalresultMap.put("data", "数据库相关操作异常");
            return JacksonHelper.writeBean2Json(finalresultMap);
        }
        finalresultMap.put("items", finalResultMapList);
        String resultTmp = JacksonHelper.writeBean2Json(finalresultMap);
        converToNewJson(finalResultMapList, myFinalResultMapList);

        finalresultMap.remove("items");
        //myFinalResultMapList.addAll(myFinalResultMapListSub);

        if (myFinalResultMapListSub.size() != 0) {
            finalresultMap.put("erroritems", myFinalResultMapListSub);
        }
        if (myFinalResultMapList.size() != 0) {
            finalresultMap.put("items", myFinalResultMapList);
        }

        String result = JacksonHelper.writeBean2Json(finalresultMap);
        //LOGGER.info(result);
        //LOGGER.info(resultTmp);
        long y = System.currentTimeMillis();
        System.out.println(execType + "耗时" + (y - x));
        return result;
    }


    /**
     * 对输入json串进行去重，避免重复的操作
     */
    private void reExtraItem(List<HashMap> items, List<HashMap> myFinalResultMapListSub) throws SQLException {
        // 1 如果请求报文中有存在只含ip的,那么ip+appName 或则  ip+appName+instanceName 都去除
        distinctIpForItem(items);

        // 2 经过第一步过滤后，若含ip+appName 那么  ip+appName+instanceName 都去除
        List<HashMap> appnameAndIpItems = new ArrayList<HashMap>();
        for (HashMap item : items) {
            String ip = (String) item.get("ip");
            String appname = (String) item.get("appname");
            String instancename = (String) item.get("instancename");
            if (!ip.equals("") && !appname.equals("") && instancename.equals("")) {

                if (!appnameAndIpItems.contains(item)) {
                    appnameAndIpItems.add(item);
                }

            }

        }
        Iterator<HashMap> iterator = items.iterator();
        while (iterator.hasNext()) {
            HashMap item = iterator.next();

            String instancename = (String) item.get("instancename");

            for (HashMap appItem : appnameAndIpItems) {
                if (appItem.get("appname").equals(item.get("appname")) && appItem.get("ip").equals(item.get("ip"))) {
                    iterator.remove(); //肯定会匹配到自己
                }
            }
        }
        LOGGER.info("appnameAndIpItems.size=" + appnameAndIpItems.size());

        for (HashMap item : appnameAndIpItems) {

            String appname = (String) item.get("appname");
            String ip = (String) item.get("ip");
            int flag = 0;
            for (HashMap appDeployEntity : DBPoolConnection.appDeployEntityList) {
                if (appDeployEntity.get("appname").equals(appname) && appDeployEntity.get("ip").equals(ip)
                        && (appDeployEntity.get("groupType").equals(groupType) || 1 == isAllSystem)) {  //1 ok11
                    flag = 1;
                    item.put("loginName", appDeployEntity.get("loginName"));
                    item.put("passwd", appDeployEntity.get("passwd"));
                    item.put("deployPath", appDeployEntity.get("deployPath")); //4
                    item.put("hostname", appDeployEntity.get("hostname"));
                    break;
                }
            }

            if (flag == 1) {
                items.add(item);
            } else {
                item.put("error", "ip+appname not find");
                myFinalResultMapListSub.add(item); //4
            }
        }
        // 3  将请求报文中 ip+appName 中的相同主机appName 归到一起
        List<HashMap> combineAppNameMapList = comBineAppName(items);
        // 4  将请求报文中 ip+appName+appInstanceName  中的相同主机appInstanceName 归到一起
        List<HashMap> combineAPPInstanceMapList = comBineAppInstanceName(items);

        System.out.println(combineAppNameMapList.toString());
        System.out.println(combineAPPInstanceMapList.toString());

        items.addAll(combineAppNameMapList);
        items.addAll(combineAPPInstanceMapList);
        LOGGER.info("items.size=" + items.size());


    }

    // 如果请求报文中有存在只含ip的,那么ip+appName 或则  ip+appName+instanceName 都去除
    private void distinctIpForItem(List<HashMap> items) {
        List<HashMap> ipItems = new ArrayList<HashMap>();
        for (HashMap item : items) {
            String ip = (String) item.get("ip");
            String appname = (String) item.get("appname");
            String instancename = (String) item.get("instancename");
            if (!ip.equals("") && appname.equals("") && instancename.equals("")) {

                if (!ipItems.contains(item)) {
                    ipItems.add(item);
                }

            }

        }

        Iterator<HashMap> iteratorForIp = items.iterator();
        while (iteratorForIp.hasNext()) {
            HashMap item = iteratorForIp.next();

            String ip = (String) item.get("ip");

            for (HashMap appItem : ipItems) {
                if (appItem.get("ip").equals(ip)) {
                    iteratorForIp.remove(); //肯定会匹配到自己,把自己去掉所以后面补上自己的items.addAll(ipItems);
                }
            }
        }

        items.addAll(ipItems);
    }

    // 将请求报文中 ip+appName 中的相同主机appName 归到一起
    private List<HashMap> comBineAppName(List<HashMap> items) {
        List<HashMap> combineMapList = new ArrayList<HashMap>();
        List<String> strList = new ArrayList<String>();
        Iterator<HashMap> iterator1 = items.iterator();
        while (iterator1.hasNext()) {

            HashMap map = iterator1.next();

            if (!map.get("ip").toString().equals("") && !map.get("appname").toString().equals("") && map.get("instancename").toString().equals("")) {

                iterator1.remove();
                if (!strList.contains(map.get("ip").toString())) { //ip 首次出现创建combineMapList
                    strList.add(map.get("ip").toString());
                    combineMapList.add(map);
                } else { //ip 非首次出现 遍历已有combineMapList 将已有appname串加上新增appname。最终实现相同主机appname归属在一起
                    for (HashMap combineMap : combineMapList) {
                        if (combineMap.get("ip").equals(map.get("ip").toString())) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(combineMap.get("appname")).append(",").append(map.get("appname"));
                            combineMap.remove(combineMap.get("ip"));
                            combineMap.put("ip", combineMap.get("ip"));
                            combineMap.put("appname", sb.toString());
                            combineMap.put("instancename", "");

                        }
                    }
                }

            }

        }
        return combineMapList;
    }


    // 将请求报文中 ip+appName+appInstanceName  中的相同主机appName 归到一起
    private List<HashMap> comBineAppInstanceName(List<HashMap> items) {
        List<HashMap> combineMapList = new ArrayList<HashMap>();
        List<String> strList = new ArrayList<String>();
        Iterator<HashMap> iterator1 = items.iterator();
        while (iterator1.hasNext()) {

            HashMap map = iterator1.next();

            if (!map.get("ip").toString().equals("") && !map.get("appname").toString().equals("") && !map.get("instancename").toString().equals("")) {

                iterator1.remove();
                if (!strList.contains(map.get("ip").toString())) {
                    strList.add(map.get("ip").toString());
                    combineMapList.add(map);
                } else {
                    for (HashMap combineMap : combineMapList) {

                        if (combineMap.get("ip").equals(map.get("ip").toString())) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(combineMap.get("instancename")).append(",").append(map.get("instancename"));
                            combineMap.remove(combineMap.get("ip"));
                            combineMap.put("ip", combineMap.get("ip"));
                            combineMap.put("appname", "all");
                            combineMap.put("instancename", sb.toString());

                        }
                    }
                }

            }

        }
        return combineMapList;
    }


    /**
     * 将输出的json串进行格式化，使得相同主机的实例归到同一组
     */
    private void converToNewJson(List<HashMap> finalResultMapList, List<HashMap> myFinalResultMapList) {
        // 新的集合 1台主机 一个子集
        for (HashMap map : finalResultMapList) {

            int flag = 0; //每次循环重置为0

            String ip = map.get("ip").toString();
            List<HashMap> resultMapList = (List<HashMap>) map.get("appitems");

            Iterator<HashMap> iterator = myFinalResultMapList.iterator();
            while (iterator.hasNext()) { //遍历新的集合的每一台主机
                HashMap newMap = iterator.next();

                if (newMap.get("ip").equals(ip)) { //如果已经有了，就再原先的基础上加
                    List<HashMap> newResultMapList = (List<HashMap>) newMap.get("appitems"); //获取该主机应用列表


                    for (HashMap temMap : resultMapList) { //把旧的集合应用列表加入新的集合应用列表

                        int flag1 = 0; //每次循环重置为0

                        String appname = temMap.get("appname").toString();

                        // 如果该主机已经存在改应用的节点，那么把实例加 到      新的返回集合应用下 ,否则把新的应用节点加入进去
                        if (newResultMapList != null) {
                            for (HashMap newTemMap : newResultMapList) {
                                String newAppname = newTemMap.get("appname").toString();


                                if (appname.equals(newAppname)) {

                                    List<String> newInstancesList = (List<String>) newTemMap.get("instances");
                                    List<String> newInstancesListTemp = (List<String>) temMap.get("instances");
                                    List<String> finalStringList = new ArrayList<String>();
                                    finalStringList.addAll(newInstancesList);
                                    finalStringList.addAll(newInstancesListTemp);
                                    newTemMap.remove("instances");
                                    newTemMap.put("instances", finalStringList);
                                    flag1 = 1; // 标记为1 ，下面  "把新的应用节点加入进去" 代码不执行

                                }


                            }//for(HashMap newTemMap:newResultMapList)
                        }
                        // 把新的应用节点加入进去
                        if (flag1 == 0) {
                            newResultMapList.add(temMap);
                        }

                    }
                    flag = 1; //如果在已有ip上面加
                }

            }

            if (flag == 0) {
                myFinalResultMapList.add(map);
            }
        }
    }


    /**
     * 解析输入报文
     *
     * @throws Exception
     */
    private List<HashMap> getRequstJson(String reqJson, List<HashMap> myFinalResultMapListSub) throws Exception {

        JSONObject json = JSONObject.parseObject(reqJson.toString());
        execType = json.getString("exec_type");// 0 串行 1并行
        groupType = json.getString("group_type");

        if (groupType == null) {
            isAllSystem = 1;
        }

        JSONArray arr = json.getJSONArray("items");
        //1 解析报文
        List<HashMap> items = new ArrayList<HashMap>();
        HashMap item = null;
        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String ip = obj.getString("ip");
            String appname = obj.getString("appname");
            String instancename = obj.getString("instancename");

            if (StringUtils.isEmpty(ip)) {
                ip = "";
            }
            if (StringUtils.isEmpty(appname)) {
                appname = "";
            }
            if (StringUtils.isEmpty(instancename)) {
                instancename = "";
            }

            if (!appname.equals("") && ip.equals("") && instancename.equals("")) { //appname

                List<String> listIp = new ArrayList<String>();
                //hostname不为空
                int flag = 0;
                for (HashMap appDeployEntity : DBPoolConnection.appDeployEntityList) {
                    if (appDeployEntity.get("appname").equals(appname) &&
                            (appDeployEntity.get("groupType").equals(groupType) || 1 == isAllSystem)) { //2 ok22
                        flag = 1;
                        item = new HashMap();
                        String newIp = (String) appDeployEntity.get("ip");
                        if (!listIp.contains(newIp)) {
                            listIp.add(newIp);
                            LOGGER.info("newIp=" + newIp);
                            item.put("ip", newIp);
                            item.put("appname", appname);
                            item.put("instancename", instancename);
                            item.put("loginName", appDeployEntity.get("loginName"));
                            item.put("passwd", appDeployEntity.get("passwd"));
                            item.put("deployPath", appDeployEntity.get("deployPath")); //1
                            item.put("hostname", appDeployEntity.get("hostname"));
                            items.add(item);
                        }
                    }
                }
                if (flag == 0) {
                    item = new HashMap();
                    item.put("ip", ip);
                    item.put("appname", appname);
                    item.put("instancename", instancename);
                    item.put("error", "appname not find");
                    myFinalResultMapListSub.add(item); //1
                }

            } else if (!appname.equals("") && !ip.equals("") && !instancename.equals("")) { //appname+ip+instancename

                int flag = 0;
                for (HashMap appDeployEntity : DBPoolConnection.appDeployEntityList) {
                    if (appDeployEntity.get("appname").equals(appname) && appDeployEntity.get("instancename").equals(instancename)
                            && appDeployEntity.get("ip").equals(ip) &&
                            (appDeployEntity.get("groupType").equals(groupType) || 1 == isAllSystem)) { //3 ok33

                        flag = 1;
                        item = new HashMap();
                        item.put("ip", ip);
                        item.put("appname", appname);
                        item.put("instancename", instancename);
                        item.put("loginName", appDeployEntity.get("loginName"));
                        item.put("passwd", appDeployEntity.get("passwd"));
                        item.put("deployPath", appDeployEntity.get("deployPath")); //2
                        item.put("hostname", appDeployEntity.get("hostname"));
                        items.add(item);

                    }
                }
                if (flag == 0) {
                    item = new HashMap();
                    item.put("ip", ip);
                    item.put("appname", appname);
                    item.put("instancename", instancename);
                    item.put("error", "ip+appname+instancename not find");
                    myFinalResultMapListSub.add(item); //2
                }

            } else if (appname.equals("") && !ip.equals("") && instancename.equals("")) { //ip

                int flag = 0;
                for (HashMap appDeployEntity : DBPoolConnection.appDeployEntityList) {
                    if (appDeployEntity.get("ip").equals(ip) &&
                            (appDeployEntity.get("groupType").equals(groupType) || 1 == isAllSystem)) { //4 ok44

                        flag = 1;
                        item = new HashMap();
                        item.put("ip", ip);
                        item.put("appname", appname);
                        item.put("instancename", instancename);
                        item.put("loginName", appDeployEntity.get("loginName"));
                        item.put("passwd", appDeployEntity.get("passwd"));
                        item.put("deployPath", appDeployEntity.get("deployPath")); //3
                        item.put("hostname", appDeployEntity.get("hostname"));
                        items.add(item);
                        break;
                    }
                }
                if (flag == 0) {
                    item = new HashMap();
                    item.put("ip", ip);
                    item.put("appname", appname);
                    item.put("instancename", instancename);
                    item.put("error", "ip not find");
                    myFinalResultMapListSub.add(item); //3
                }
            } else {  //ip+appname
                item = new HashMap();
                item.put("ip", ip);
                item.put("appname", appname);
                item.put("instancename", instancename);
                items.add(item);
            }
        }
        return items;
    }
    /***
     * 程序启停查看接入口
     * */
    //doWork(items,"-psg","");
    public static void doWork(List<HashMap> items, String workType, String resMsg, List<HashMap> finalResultMapList, Map<String, Object> sessionMap, List<HashMap> myFinalResultMapListSub)
            throws JSchException, IOException, Exception, SQLException, InterruptedException, ExecutionException {
        if (items.size() != 0) {
            if (execType.equals("0")) {
                for (HashMap map : items) {
                    HashMap<String, Object> resultMap = startOrStop(map, workType, resMsg, sessionMap, myFinalResultMapListSub);
                    if (resultMap != null) {
                        finalResultMapList.add(resultMap);
                    }
                } //for (HashMap map : items){
            } else if (execType.equals("1")) {
                ExecutorService threadPool = Executors.newFixedThreadPool(items.size());
                List<Future<HashMap>> results = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    HashMap map = items.get(i);
                    //————————————————入口——————————————————
                    LifeCycleTasker lifeCycleTasker = new LifeCycleTasker(map, workType, resMsg, sessionMap, myFinalResultMapListSub);
                    Future<HashMap> resultMap = threadPool.submit(lifeCycleTasker);
                    results.add(resultMap);
                }
                for (int i = 0; i < results.size(); i++) {
                    HashMap resultMap = results.get(i).get();
                    if (resultMap != null) {
                        finalResultMapList.add(resultMap);
                    }

                }
                // 关闭线程池
                threadPool.shutdown();
            }
        }
    }
    /**
     * 启停查看程序
     */
    public static HashMap<String, Object> startOrStop(HashMap map, String oprType, String resMsg, Map<String, Object> sessionMap, List<HashMap> myFinalResultMapListSub)
            throws IOException, Exception, SQLException {
        HashMap<String, Object> resultMap = null; //1个主机 ip 和  appitems
        List<HashMap> resultMapList = null; //1台主机的 appitems 的  多个appname  和 instancenames
        HashMap<String, Object> map1 = null; // 具体的 appname  和 instancenames

        StringBuilder result1 = new StringBuilder();
        //2 判断执行类型

        String ip = map.get("ip").toString();
        String appname = map.get("appname").toString();
        String instancename = map.get("instancename").toString();
        String loginName = map.get("loginName").toString();
        String passwd = map.get("passwd").toString();
        String appDeployPath = map.get("deployPath").toString();
        String hostName = map.get("hostname").toString();
        // 如果会话已经获取，则存入集合，防止重新获取该会话
        Session session = null;
        if (sessionMap.get(ip) == null) {
            session = getSession(ip, loginName, passwd); //1
            if (session == null) {
                resultMap = new HashMap();
                resultMap.put("ip", ip);
                resultMap.put("appname", "");
                resultMap.put("instancename", "");
                resultMap.put("errorCode", 1);
                resultMap.put("errorMsg", "session can not connect");
                myFinalResultMapListSub.add(resultMap);
                return null;
            }
            sessionMap.put(ip, session);
        } else {
            session = (Session) sessionMap.get(ip);
        }
        if (appname.contains(",")) {
            appname = appname.replace(",", ",\\\n");
        } //将逗号用换行代替，是为了防止appname传递过多而导致远程调用失败
        if (instancename.contains(",")) {
            instancename = instancename.replace(",", ",\\\n");
        } //将逗号用换行代替，是为了防止instancename传递过多而导致远程调用失败


        if (!ip.equals("") && !appname.equals("") && !instancename.equals("")) {
            System.out.println("三个不为空");
            resultMap = new HashMap<>();
            resultMapList = new ArrayList<HashMap>();
            List<String> arr = new ArrayList<String>();

            if (!oprType.equals("-psg")) {
                JSchUtil.execCommandByShellExt(session, oprType, appname, instancename, appDeployPath);
            }
            //2查库返回结果
//——————————————————————入口————————————————
            if (oprType.equals("-psg")) {
                String lsStrig = JSchUtil.execCommandByShell(session, oprType, appname, instancename, appDeployPath);
                // 若远端主机卡住，导致远程调用超出额定，返回超时
                if ("sessionTimeOut".equals(lsStrig)) { //1 ok11 ok1
                    resultMap = new HashMap();
                    resultMap.put("ip", ip);
                    resultMap.put("appname", "");
                    resultMap.put("instancename", "");
                    resultMap.put("errorCode", 2);
                    resultMap.put("errorMsg", "sessionTimeOut");
                    myFinalResultMapListSub.add(resultMap);
                    return null;
                }
                /**将制定返回报文进行解析，拼凑json串
                 * myresult=bcsn:na-jf-ctrl01_bcsn_001,0|na-jf-ctrl01_bcsn_002,0
                 myresult=bbds:na-jf-ctrl01_bbds_001,0
                 * */
                String[] strArr = lsStrig.split("\r\n");
                for (String str : strArr) {
                    if (str.startsWith("myresult")) {
                        map1 = new HashMap<>();
                        String strExt = str.split("=")[1];
                        String appName = strExt.split(":")[0];
                        map1.put("appname", appName);
                        String appIns = strExt.split(":")[1];
                        arr = new ArrayList<String>();
                        String[] appInses = appIns.split("\\|");
                        for (String strNew : appInses) {
                            if (strNew != null && strNew.contains("\\r")) { //111
                                strNew = strNew.replace("\\r", "");
                            }
                            arr.add(strNew);
                        }
                        map1.put("instances", arr);
                        resultMapList.add(map1);
                    }
                }
            }

            resultMap.put("ip", ip);
            resultMap.put("hostName", hostName);
            resultMap.put("appitems", resultMapList);
            JSchUtil.closeSession(session);


        } else if (!ip.equals("") && !appname.equals("") && instancename.equals("")) {
            System.out.println("ip+appname不空,instancename空");
            resultMap = new HashMap<>();
            resultMapList = new ArrayList<HashMap>();

            if (!oprType.equals("-psg")) {
                long d = System.currentTimeMillis();

                JSchUtil.execCommandByShellExt(session, oprType, appname, instancename, appDeployPath);
                long f = System.currentTimeMillis();
                System.out.println("调用代理耗时" + (f - d));
            }
            List<String> arr = new ArrayList<String>();

            if (oprType.equals("-psg")) {
                String lsStrig = JSchUtil.execCommandByShell(session, oprType, appname, instancename, appDeployPath);
                //LOGGER.info("lsStrig="+lsStrig);
                if ("sessionTimeOut".equals(lsStrig)) { //2 ok22 ok2
                    resultMap = new HashMap();
                    resultMap.put("ip", ip);
                    resultMap.put("appname", "");
                    resultMap.put("instancename", "");
                    resultMap.put("errorCode", 2);
                    resultMap.put("errorMsg", "sessionTimeOut");
                    myFinalResultMapListSub.add(resultMap);
                    return null;
                }


                String[] strArr = lsStrig.split("\r\n");
                for (String str : strArr) {
                    if (str.startsWith("myresult")) {
                        map1 = new HashMap<>();
                        String strExt = str.split("=")[1];
                        String appName = strExt.split(":")[0];
                        map1.put("appname", appName);
                        String appIns = strExt.split(":")[1];
                        arr = new ArrayList<String>();
                        String[] appInses = appIns.split("\\|");
                        for (String strNew : appInses) {
                            if (strNew != null && strNew.contains("\\r")) { //222
                                strNew = strNew.replace("\\r", "");
                            }
                            arr.add(strNew);
                        }
                        map1.put("instances", arr);
                        resultMapList.add(map1);
                    }


                }

            } //if(oprType.equals("-psg"))
            resultMap.put("ip", ip);
            resultMap.put("hostName", hostName);
            resultMap.put("appitems", resultMapList);
            JSchUtil.closeSession(session); //2


        } else if (!ip.equals("") && appname.equals("") && instancename.equals("")) {
            System.out.println("ip不空,appname+instancename空");
            resultMap = new HashMap<>();
            resultMapList = new ArrayList<HashMap>();

            if (!oprType.equals("-psg")) {
                long d = System.currentTimeMillis();

                JSchUtil.execCommandByShellExt(session, oprType, appname, instancename, appDeployPath);
                long e = System.currentTimeMillis();
                System.out.println("调用代理耗时" + (e - d));
            }
            if (oprType.equals("-psg")) {
                String lsStrig = JSchUtil.execCommandByShell(session, oprType, appname, instancename, appDeployPath);
                if ("sessionTimeOut".equals(lsStrig)) { //3 ok33 ok3
                    resultMap = new HashMap();
                    resultMap.put("ip", ip);
                    resultMap.put("appname", "");
                    resultMap.put("instancename", "");
                    resultMap.put("errorCode", 2);
                    resultMap.put("errorMsg", "sessionTimeOut");
                    myFinalResultMapListSub.add(resultMap);
                    return null;
                }
                //long d=System.currentTimeMillis();
                List<String> arr = null;

                String[] strArr = lsStrig.split("\r\n");
                for (String str : strArr) {
                    if (str.startsWith("myresult")) {
                        map1 = new HashMap<>();
                        String strExt = str.split("=")[1];
                        String appName = strExt.split(":")[0];
                        map1.put("appname", appName);
                        String appIns = strExt.split(":")[1];
                        arr = new ArrayList<String>();
                        String[] appInses = appIns.split("\\|");
                        for (String strNew : appInses) {
                            if (strNew != null && strNew.contains("\\r")) { //333
                                strNew = strNew.replace("\\r", "");
                            }
                            arr.add(strNew);
                        }
                        map1.put("instances", arr);
                        resultMapList.add(map1);
                    }
                }
                //long e=System.currentTimeMillis();
                //System.out.println("剩余耗时"+(e-d));
            }
            JSchUtil.closeSession(session); //3
            //}
            resultMap.put("ip", ip);
            resultMap.put("hostName", hostName);
            resultMap.put("appitems", resultMapList);
        }


        return resultMap; //方法
    }
    /**
     * 获取会话
     */
    private static Session getSession(String ip, String hostName, String passwd) throws JSchException {
        Session session = null;
        try {
            session = JSchUtil.getJSchSession(hostName, ip, passwd, 30000);
        } catch (JSchException e) {
            return null;
        }

        return session;
    }


    public static String getExecString(String oper, String appname, String instancename) {

        String exec0 = "cd /bossapp1/prg/DeployAgent;";


        String exec = "";
        if (!instancename.equals("")) { //appinstance+appname+ip
            exec = "./DeployAgentExt " + oper + " -inst_id=" + instancename;
        } else if (!appname.equals("") && instancename.equals(""))  //ip+appname
        {
            exec = "./DeployAgentExt " + oper + " " + appname;
        } else if (appname.equals("") && instancename.equals("")) //ip
        {
            exec = "./DeployAgentExt " + oper;
        }
        return exec0 + exec;
    }
}
