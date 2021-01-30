package com.newland.boss.cloud.web.zkManageTool.service;

import java.util.List;
import java.util.Map;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkCfg;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkNode;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkPropMsg;

public interface ZkManageToolService {

	// public TreeNode getZkTree(String connectHost) throws KeeperException,
	// InterruptedException, IOException;

	// public TreeNode getRootInfo(String connectHost) throws KeeperException,
	// InterruptedException, IOException;

	// public TreeNode getNodeInfo(String path, String title) throws
	// IOException, KeeperException, InterruptedException;

	public boolean setData(String nodePath, String nodeData);

	public boolean addNode(String nodePath, String nodeTitle);

	public Map<String, Object> copyNode(String nodePath, String nodeTitle);

	public boolean deleteNode(String nodePath);

	public boolean changeZkConn(String connectHost);

	public byte[] getNodeData(String path);

	public List<ZkCfg> getZkCfg();

	public List<ZkPropMsg> getZkPropMsg(String zkPath);

	/**
	 * 获取节点
	 * 
	 * @param path
	 * @param connectHost
	 * @return
	 * @throws Exception
	 */
	public List<ZkNode> getZkNode(String path, String connectHost) throws Exception;
}
