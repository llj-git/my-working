package com.newland.boss.cloud.web.serviceStatus.entity;

public class AppDeployInfo {

	private String ip;
	private String appStanceName;
	private String appName;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppStanceName() {
		return appStanceName;
	}

	public void setAppStanceName(String appStanceName) {
		this.appStanceName = appStanceName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public boolean equals(Object obj) {
		AppDeployInfo s = (AppDeployInfo) obj;
		return ip.equals(s.ip) && appName.equals(s.appName) && appStanceName.equals(s.appStanceName);
	}

	@Override
	public int hashCode() {
		String in = ip + appName + appStanceName;
		return in.hashCode();
	}

}
