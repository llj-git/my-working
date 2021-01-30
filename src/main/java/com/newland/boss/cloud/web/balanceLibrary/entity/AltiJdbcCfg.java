/**
 * 
 */
package com.newland.boss.cloud.web.balanceLibrary.entity;

/**
 * @author ylc
 *
 */
public class AltiJdbcCfg {

	private String dbName;
	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPwd;
	private String jdbcDesc;

	private String driverClass;

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


	@Override
	public String toString() {
		return "AltiJDBCCfg [dbName=" + dbName + ", jdbcUrl=" + jdbcUrl + ", jdbcUser=" + jdbcUser + ", jdbcPwd="
				+ jdbcPwd + ", jdbcDesc=" + jdbcDesc + "]";
	}

}
