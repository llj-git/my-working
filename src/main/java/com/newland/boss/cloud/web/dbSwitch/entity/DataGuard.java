package com.newland.boss.cloud.web.dbSwitch.entity;

public class DataGuard {

	/**
	 * 数据库名
	 */
	private String dbName;

	/**
	 * 主备表 (0:主库,1:备库1,2:备库2)
	 */
	private Integer isMain;

	/**
	 * 读写(1:写 0：读 2：不可用)
	 */
	private Integer isWrite;

	/**
	 * 主机名
	 */
	private String hostName;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 密码库pname
	 */
	private String pName;

	/**
	 * 备注
	 */
	private String commons;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public Integer getIsWrite() {
		return isWrite;
	}

	public void setIsWrite(Integer isWrite) {
		this.isWrite = isWrite;
	}

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

	public String getPname() {
		return pName;
	}

	public void setPname(String pName) {
		this.pName = pName;
	}

	public String getCommons() {
		return commons;
	}

	public void setCommons(String commons) {
		this.commons = commons;
	}

}
