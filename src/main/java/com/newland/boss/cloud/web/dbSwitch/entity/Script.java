package com.newland.boss.cloud.web.dbSwitch.entity;

public class Script {

	/**
	 * 主机名
	 */
	private String hostName;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * shell脚本
	 */
	private String fileName;

	/**
	 * 参数
	 */
	private String param;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
