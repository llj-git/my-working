package com.newland.boss.cloud.web.bds.entity;

import java.util.Comparator;

public class ComparatorAppNodeInfo implements Comparator<AppNodeInfo> {

	@Override
	public int compare(AppNodeInfo o1, AppNodeInfo o2) {
		int stateFlag = 0;
		if (null != o1.getState() && null != o2.getState()) {
			stateFlag = o2.getState().compareTo(o1.getState());
		}
		int flag = 0;
		if (stateFlag == 0 && null != o1.getIp() && null != o2.getIp()) {
			flag = o1.getIp().compareTo(o2.getIp());
			if (flag == 0 && null != o1.getNodeName() && null != o2.getNodeName()) {
				return o1.getNodeName().compareTo(o2.getNodeName());
			} else {
				return flag;
			}
		} else {
			return stateFlag;
		}
	}

}
