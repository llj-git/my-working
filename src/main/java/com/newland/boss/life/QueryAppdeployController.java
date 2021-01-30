package com.newland.boss.life;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.life.dao.GetDataDao;

@RestController
public class QueryAppdeployController {
	private static Log LOGGER = LogFactory.getLog(QueryAppdeployController.class);
	//public static  List<HashMap>  appDeployEntityList = null;
	

	@RequestMapping(value = "/appList", method = RequestMethod.POST)
	public String appList(@RequestBody String reqJson) throws Exception {

		JSONObject json = JSONObject.parseObject(reqJson);
		String hostName = json.getString("hostName");
		String appNames = json.getString("appNames");
		String appInstanceName = json.getString("appInstanceName");
		

		//LOGGER.info("appDeployEntityList.size"+DBPoolConnection.appDeployEntityList.size());
	
		List<HashMap> appDeployEntityListByHName =new ArrayList<HashMap>();
		//hostname不为空
		for (HashMap appDeployEntity :DBPoolConnection.appDeployEntityList){
			
			if(appDeployEntity.get("hostname").equals(hostName)){
				appDeployEntityListByHName.add(appDeployEntity);
			}
		}
		
		//LOGGER.info("appDeployEntityListByHName.size"+appDeployEntityListByHName.size());
		
		
		List<HashMap> finalAppDeployEntityList =new ArrayList<HashMap>();
		
		//如果appInstanceName 不为空 则直接通过appInstanceName获取取
		if(appInstanceName!=null){
			String [] appInstances = null;
			if(appInstanceName!=null){
				appInstances=  appInstanceName.split(",");
			}
         
			
		    for(String str:appInstances){
        	   for (HashMap appDeployEntity:appDeployEntityListByHName){
        		   if(appDeployEntity.get("instancename").equals(str)){
        			   finalAppDeployEntityList.add(appDeployEntity);
        		   }
        		   
        	   }
            }
		}
		else if(appNames!=null&&appInstanceName==null){ //如果appName 不为空 appInstanceName为空 则通过appName获取
			
			String [] appNamesList = appNames.split(","); //将appName集合 转为数组
			
			 for (HashMap appDeployEntity:appDeployEntityListByHName){
				 for(String appName : appNamesList){
					  if(appDeployEntity.get("appname").equals(appName)){
		      		 	   finalAppDeployEntityList.add(appDeployEntity);
		      		   }
				 }
	      		   
      		   
      	   }
		}
		else if(appNames==null&&appInstanceName==null){
			finalAppDeployEntityList.addAll(appDeployEntityListByHName);
		}
		//LOGGER.info("finalAppDeployEntityList.size"+finalAppDeployEntityList.size());
		return JacksonHelper.writeBean2Json(finalAppDeployEntityList);

	}
	
	@RequestMapping(value = "/appDeployMutualAidRelation", method = RequestMethod.POST)
	public String appDeployMutualAidRelation(@RequestBody String reqJson) throws Exception {
		
		JSONObject json = JSONObject.parseObject(reqJson);
		
//		if(appDeployMutualAidRelationList==null){
//			LOGGER.info("load appDeployMutualAidRelationList");
//			GetDataDao getDataDao = new GetDataDao();
//			appDeployMutualAidRelationList = getDataDao.getAppDeployMutualAidRelationList();
//		}
		
		String mainHostName = json.getString("mainHostName"); //不为空
		
		List<HashMap> finalAppDeployMutualAidRelationList =new ArrayList<HashMap>();
		
		
		for(HashMap appDeployMutualAidRelation:DBPoolConnection.appDeployMutualAidRelationList){
			if(appDeployMutualAidRelation.get("mainhostname").equals(mainHostName)){
				finalAppDeployMutualAidRelationList.add(appDeployMutualAidRelation);
			}
		}
	 
	    return JacksonHelper.writeBean2Json(finalAppDeployMutualAidRelationList);
		
			
	}
	
	
	
	@RequestMapping(value = "/checkWaitingDuration", method = RequestMethod.POST)
	public String checkWaitingDuration(@RequestBody String reqJson) throws Exception {
		
		
		
		
		JSONObject json = JSONObject.parseObject(reqJson);
		String appName = json.getString("appName");
		
//		if(checkWaitingDurationList==null){
//			LOGGER.info("load checkWaitingDurationList");
//			GetDataDao getDataDao = new GetDataDao();
//			checkWaitingDurationList = getDataDao.getCheckWaitingDurationList();
//			LOGGER.info("checkWaitingDurationList.size"+checkWaitingDurationList.size());
//		}
		
		List<HashMap> finalCheckWaitingDurationList = new ArrayList<HashMap>();
		
		for(HashMap checkWaitingDuration:DBPoolConnection.checkWaitingDurationList){
			if(checkWaitingDuration.get("appname").equals(appName)){
				finalCheckWaitingDurationList.add(checkWaitingDuration);
			}
		}
		LOGGER.info("checkWaitingDurationList.size"+finalCheckWaitingDurationList.size());
      return JacksonHelper.writeBean2Json(finalCheckWaitingDurationList);
		
	}
	
	
	
	
	
	
	
	

}
