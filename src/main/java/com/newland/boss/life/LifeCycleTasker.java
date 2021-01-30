package com.newland.boss.life;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.newland.boss.cloud.web.util.DbUtil;
import com.newland.boss.cloud.web.util.JSchUtil;

public class LifeCycleTasker implements Callable<HashMap>{
	
	private HashMap map;
	private String oprType;
	private String resMsg;
	Map<String, Object> sessionMap;
	List<HashMap> myFinalResultMapListSub;
	
	
	public LifeCycleTasker(HashMap map, String oprType, String resMsg, Map<String, Object> sessionMap, List<HashMap> myFinalResultMapListSub){
		
		this.map=map;
		this.oprType=oprType;
		this.resMsg=resMsg;
		this.sessionMap=sessionMap;
		this.myFinalResultMapListSub=myFinalResultMapListSub;
	}

	@Override
	public HashMap call() throws Exception {
		return new LifeCycleController().startOrStop(map, oprType, resMsg,sessionMap,myFinalResultMapListSub);
	}
	
	

}
