package com.newland.boss.cloud.web.userInfo.entity;

/**
 * @author lcc
 *
 */
public class JdbcCfg {

	public static Integer CONFIGDB = 2;
	/**
	 * 数据库名
	 */
	private String dbName;

	/**
	 * url
	 */
	private String jdbcUrl;

	/**
	 * 用户名
	 */
	private String jdbcUser;

	/**
	 * 密码
	 */
	private String jdbcPwd;

	/**
	 * 描述
	 */
	private String jdbcDesc;

	/**
	 * driverClass
	 */
	private String driverClass;

	/**
	 * 类型(1-oracle,2-altiBase)
	 */
	private Integer type;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcPwd() {
		return jdbcPwd;
	}

	public void setJdbcPwd(String jdbcPwd) {
		this.jdbcPwd = jdbcPwd;
	}

	public String getJdbcDesc() {
		return jdbcDesc;
	}

	public void setJdbcDesc(String jdbcDesc) {
		this.jdbcDesc = jdbcDesc;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
