package com.newland.boss.cloud.web.userInfo.entity;

public class SyncTable {

	private Integer tableId;

	private String tableName;

	private String mainKey;

	private String condition;

	/**
	 * 排序字段
	 */
	private String conditionExt;

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getMainKey() {
		return mainKey;
	}

	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getConditionExt() {
		return conditionExt;
	}

	public void setConditionExt(String conditionExt) {
		this.conditionExt = conditionExt;
	}

}
