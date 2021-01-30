/**
 * 
 */
package com.newland.boss.cloud.web.balanceLibrary.entity;

/**
 * @author ylc
 *
 */
public class AltiRouteCfg {

	private String dbName;
	private Integer beginPartitionId;
	private Integer endPartitionId;
	private String partitionDesc;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Integer getBeginPartitionId() {
		return beginPartitionId;
	}

	public void setBeginPartitionId(Integer beginPartitionId) {
		this.beginPartitionId = beginPartitionId;
	}

	public Integer getEndPartitionId() {
		return endPartitionId;
	}

	public void setEndPartitionId(Integer endPartitionId) {
		this.endPartitionId = endPartitionId;
	}

	public String getPartitionDesc() {
		return partitionDesc;
	}

	public void setPartitionDesc(String partitionDesc) {
		this.partitionDesc = partitionDesc;
	}

	@Override
	public String toString() {
		return "AltiRouteCfg [dbName=" + dbName + ", beginPartitionId=" + beginPartitionId + ", endPartitionId="
				+ endPartitionId + ", partitionDesc=" + partitionDesc + "]";
	}

}
