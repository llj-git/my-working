package com.newland.boss.cloud.web.bds.entity;

import java.util.List;


public class ServiceInfo {

	//zookeeper-services
	private String serviceType;
	private String serviceName;
	//nodeName,status,type,dbsource
	private List<String[]> provideList;
	private List<String> consumerList;
	//0:主 1:备1 2:备2
	private String type;
	private String dbsource;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<String[]> getProvideList() {
		return provideList;
	}
	public void setProvideList(List<String[]> provideList) {
		this.provideList = provideList;
	}
	public List<String> getConsumerList() {
		return consumerList;
	}
	public void setConsumerList(List<String> consumerList) {
		this.consumerList = consumerList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDbsource() {
		return dbsource;
	}
	public void setDbsource(String dbsource) {
		this.dbsource = dbsource;
	}
	
	
}
