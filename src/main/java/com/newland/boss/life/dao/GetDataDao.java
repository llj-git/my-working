package com.newland.boss.life.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.newland.boss.cloud.web.util.DbUtil;

public class GetDataDao {
    
	public  List<HashMap>  getAppDeployInfoList() throws SQLException{
		
		String sql = "select *from app_deploy_info where app_status=0";
		ResultSet rs=DbUtil.executeQuery(sql.toString());
		List<HashMap> appMapList = new ArrayList<HashMap>();
		HashMap temMap=null;
		   while(rs.next()){ //1台主机
			   temMap = new HashMap();
				 temMap.put("appname", rs.getString("app_name"));
				 temMap.put("instancename", rs.getString("app_instance_name"));
				 temMap.put("hostname", rs.getString("host_name"));
				 temMap.put("appType", rs.getString("app_type"));
				 temMap.put("groupType", rs.getString("group_type"));
				 
				 temMap.put("startpath", rs.getString("start_path"));
				 temMap.put("stoppath", rs.getString("stop_path"));
				 temMap.put("statuspath", rs.getString("status_path"));
				 temMap.put("ip", rs.getString("ip"));
				 temMap.put("passwd", rs.getObject("passwd"));
				 temMap.put("loginName", rs.getObject("login_name"));
				 temMap.put("groupType", rs.getString("group_type"));
				 temMap.put("deployPath", rs.getString("deploy_path"));
				 
				 appMapList.add(temMap);
			}
		
		return appMapList;
		
	}
	
	
	
   public  List<HashMap>  getAppDeployMutualAidRelationList() throws SQLException{
		
		String sql = "select *from app_deploy_mutual_aid_relation where app_status =0";
		ResultSet rs=DbUtil.executeQuery(sql.toString());
		List<HashMap> mapList = new ArrayList<HashMap>();
		HashMap appDeployMutualAidRelation=null;
		   while(rs.next()){ //1台主机
			   appDeployMutualAidRelation = new HashMap();
			   appDeployMutualAidRelation.put("bakhostname",rs.getString("bak_host_name"));
			   appDeployMutualAidRelation.put("mainhostname",rs.getString("main_host_name"));
			   mapList.add(appDeployMutualAidRelation);
			}
		
		return mapList;
		
	}
   
   
   public  List<HashMap>  getCheckWaitingDurationList() throws SQLException{
		
		String sql = "select *from check_waiting_duration";
		ResultSet rs=DbUtil.executeQuery(sql.toString());
		List<HashMap> mapList = new ArrayList<HashMap>();
		HashMap checkWaitingDuration=null;
		   while(rs.next()){ //1台主机
			   checkWaitingDuration = new HashMap();
			   checkWaitingDuration.put("statuswaitingduration", Integer.valueOf(rs.getString("status_waiting_duration")));
			   checkWaitingDuration.put("appname", rs.getString("app_name"));
			   mapList.add(checkWaitingDuration);
			}
		
		return mapList;
		
	}
	
	
	
	
	
}
