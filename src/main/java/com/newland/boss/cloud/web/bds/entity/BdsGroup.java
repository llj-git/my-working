package com.newland.boss.cloud.web.bds.entity;

import java.util.List;

public class BdsGroup {

	// 数据源
	private String dbSource;

	// 当前主库serviceType,为0,代表现在0是主库，为1,代表现在1是主库,以此类推
	// 取 /sysinfo/state/bds/dbsourceName/
	private String currServiceType;

	// 主库0
	private List<AppNodeInfo> branchDB0List;
	// 用于主备切换打勾
	private boolean branchDB0Selected = false;
	private String ipMain;

	// 备库1
	private List<AppNodeInfo> branchDB1List;
	// 用于主备切换打勾
	private boolean branchDB1Selected = false;
	private String ipBak1;

	// 备库2
	private List<AppNodeInfo> branchDB2List;
	// 用于主备切换打勾
	private boolean branchDB2Selected = false;
	private String ipBak2;

	public String getDbSource() {
		return dbSource;
	}

	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}

	public boolean isBranchDB0Selected() {
		return branchDB0Selected;
	}

	public void setBranchDB0Selected(boolean branchDB0Selected) {
		this.branchDB0Selected = branchDB0Selected;
	}

	public List<AppNodeInfo> getBranchDB1List() {
		return branchDB1List;
	}

	public String getIpMain() {
		return ipMain;
	}

	public void setIpMain(String ipMain) {
		this.ipMain = ipMain;
	}

	public String getIpBak1() {
		return ipBak1;
	}

	public void setIpBak1(String ipBak1) {
		this.ipBak1 = ipBak1;
	}

	public String getIpBak2() {
		return ipBak2;
	}

	public void setIpBak2(String ipBak2) {
		this.ipBak2 = ipBak2;
	}

	public void setBranchDB1List(List<AppNodeInfo> branchDB1List) {
		this.branchDB1List = branchDB1List;
	}

	public boolean isBranchDB1Selected() {
		return branchDB1Selected;
	}

	public void setBranchDB1Selected(boolean branchDB1Selected) {
		this.branchDB1Selected = branchDB1Selected;
	}

	public String getCurrServiceType() {
		return currServiceType;
	}

	public void setCurrServiceType(String currServiceType) {
		this.currServiceType = currServiceType;
	}

	public List<AppNodeInfo> getBranchDB0List() {
		return branchDB0List;
	}

	public void setBranchDB0List(List<AppNodeInfo> branchDB0List) {
		this.branchDB0List = branchDB0List;
	}

	public List<AppNodeInfo> getBranchDB2List() {
		return branchDB2List;
	}

	public void setBranchDB2List(List<AppNodeInfo> branchDB2List) {
		this.branchDB2List = branchDB2List;
	}

	public boolean isBranchDB2Selected() {
		return branchDB2Selected;
	}

	public void setBranchDB2Selected(boolean branchDB2Selected) {
		this.branchDB2Selected = branchDB2Selected;
	}

}
