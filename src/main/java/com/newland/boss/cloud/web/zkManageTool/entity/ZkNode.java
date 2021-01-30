package com.newland.boss.cloud.web.zkManageTool.entity;

import org.springframework.stereotype.Component;

@Component
public class ZkNode {

	/**
	 * 父级菜单名称
	 */
	private String pName;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * zk路径
	 */
	private String path;

	/**
	 * 内容
	 */
	private String text;

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
