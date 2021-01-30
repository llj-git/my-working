package com.newland.boss.cloud.web.bds.entity;

import java.util.List;

public class ServiceGroup {

	//zookeeper-services
	private String serviceType;
	private List<ServiceInfo> serviceList;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public List<ServiceInfo> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
	}
	
	
}
