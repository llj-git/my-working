package com.newland.boss.cloud.web.bds.entity;

import java.util.Comparator;

//nodeName,status,type,dbsource
public class BdsServiceListComparator implements Comparator<String[]> {

	@Override
	public int compare(String[] aData1, String[] aData2) {
		int flag = 0;
		if (checkDbSource(aData1) && checkDbSource(aData2)) {
			flag = aData1[3].compareTo(aData2[3]);
		}
		if (checkNodeName(aData1) && checkNodeName(aData2) && flag==0) {
			return aData1[0].compareTo(aData2[0]);
		} else {
			return flag;
		}
	}

	public boolean checkDbSource(String[] aData) {
		if (null != aData && aData.length >= 4 && null != aData[3]) {
			return true;
		}
		return false;
	}

	public boolean checkNodeName(String[] aData) {
		if (null != aData && aData.length >= 4 && null != aData[0]) {
			return true;
		}
		return false;
	}
}
