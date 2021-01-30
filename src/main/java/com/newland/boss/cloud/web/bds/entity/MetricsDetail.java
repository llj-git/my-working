package com.newland.boss.cloud.web.bds.entity;

import java.util.Date;

public class MetricsDetail {
	
	private String hostName;
	private String appInstanceName;
	private String fileName;
	private Date t;
	private String time;
	private Integer count;
	private Integer max;
	private Integer mean;
	private Integer min;
	private Date insertTime;
	private Date beginDate;
	private Date endDate;
	
	public MetricsDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getAppInstanceName() {
		return appInstanceName;
	}
	public void setAppInstanceName(String appInstanceName) {
		this.appInstanceName = appInstanceName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getT() {
		return t;
	}
	public void setT(Date t) {
		this.t = t;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getMean() {
		return mean;
	}
	public void setMean(Integer mean) {
		this.mean = mean;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "MetricsDetail [hostName=" + hostName + ", appInstanceName=" + appInstanceName + ", fileName=" + fileName
				+ ", t=" + t + ", time=" + time + ", count=" + count + ", max=" + max + ", mean=" + mean + ", min="
				+ min + ", insertTime=" + insertTime + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
	}

}
