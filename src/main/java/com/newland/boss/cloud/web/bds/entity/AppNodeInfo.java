package com.newland.boss.cloud.web.bds.entity;

public class AppNodeInfo {
	
	private String dbSource;
	private String nodeName;
	private String state;
	private String ip;
	private String deployPath;
	//bds
	private String serviceType;
	 
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	 
	public String getDbSource() {
		return dbSource;
	}
	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}
	public String getDeployPath() {
		return deployPath;
	}
	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	 
}
