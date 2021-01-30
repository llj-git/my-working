package com.newland.boss.cloud.web.serviceStatus.entity;

import java.util.List;

public class IgniteNode {

	private String serviceType;

	private String serviceName;

	private List<String> workerList;

	private List<String> servrerList;

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

	public List<String> getWorkerList() {
		return workerList;
	}

	public void setWorkerList(List<String> workerList) {
		this.workerList = workerList;
	}

	public List<String> getServrerList() {
		return servrerList;
	}

	public void setServrerList(List<String> servrerList) {
		this.servrerList = servrerList;
	}

}
