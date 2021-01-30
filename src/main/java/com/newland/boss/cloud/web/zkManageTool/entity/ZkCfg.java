package com.newland.boss.cloud.web.zkManageTool.entity;

import org.springframework.stereotype.Component;

@Component
public class ZkCfg {

	private Integer zkId;

	private String zkName;

	private String zkHost;

	private String createTime;

	private String operatorId;

	private String zkSessionTimeOut;

	private String zkConnTimeOut;

	private String zkScheme;
	private String zkAuth;
	private String zkRetrySleep;
	private String zkRetryMax;

	public Integer getZkId() {
		return zkId;
	}

	public void setZkId(Integer zkId) {
		this.zkId = zkId;
	}

	public String getZkName() {
		return zkName;
	}

	public void setZkName(String zkName) {
		this.zkName = zkName;
	}

	public String getZkHost() {
		return zkHost;
	}

	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getZkSessionTimeOut() {
		return zkSessionTimeOut;
	}

	public void setZkSessionTimeOut(String zkSessionTimeOut) {
		this.zkSessionTimeOut = zkSessionTimeOut;
	}

	public String getZkConnTimeOut() {
		return zkConnTimeOut;
	}

	public void setZkConnTimeOut(String zkConnTimeOut) {
		this.zkConnTimeOut = zkConnTimeOut;
	}

	public String getZkScheme() {
		return zkScheme;
	}

	public void setZkScheme(String zkScheme) {
		this.zkScheme = zkScheme;
	}

	public String getZkAuth() {
		return zkAuth;
	}

	public void setZkAuth(String zkAuth) {
		this.zkAuth = zkAuth;
	}

	public String getZkRetrySleep() {
		return zkRetrySleep;
	}

	public void setZkRetrySleep(String zkRetrySleep) {
		this.zkRetrySleep = zkRetrySleep;
	}

	public String getZkRetryMax() {
		return zkRetryMax;
	}

	public void setZkRetryMax(String zkRetryMax) {
		this.zkRetryMax = zkRetryMax;
	}

}
