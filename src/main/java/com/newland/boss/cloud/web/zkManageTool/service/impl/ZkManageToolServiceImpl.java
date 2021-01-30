package com.newland.boss.cloud.web.zkManageTool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import com.newland.boss.cloud.web.util.ZkUtil;
import com.newland.boss.cloud.web.zkManageTool.dao.ZkManageToolDao;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkCfg;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkNode;
import com.newland.boss.cloud.web.zkManageTool.entity.ZkPropMsg;
import com.newland.boss.cloud.web.zkManageTool.service.ZkManageToolService;
import com.newland.sri.ccp.common.util.StringUtils;

@Component
public class ZkManageToolServiceImpl extends ZkUtil implements ZkManageToolService {

	private static Log logger = LogFactory.getLog(ZkManageToolServiceImpl.class);

	private static ZkManageToolDao zkManageToolDao = new ZkManageToolDao();

	private static ZooKeeper zk;

	private final String ROOT = "/";

	public String scheme = "digest";
	public String auth = "admin:fmbs3_adm";

	/**
	 * 连接zookeeper
	 * 
	 * @return
	 * @throws IOException
	 */
	protected ZooKeeper connectZookeeper(String connectHost) throws IOException {
		zk = super.connectZookeeper(connectHost);
		zk.addAuthInfo(scheme, auth.getBytes());
		return zk;
	}

	
	/**
	 * 获取下一级节点(不遍历当前节点下的子节点,保证性能)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ZkNode> getZkNode(String path, String connectHost) throws Exception {
		// 连接zk
		connectZookeeper(connectHost);
		String childPath = "";
		ZkNode zkNode = null;
		List<ZkNode> zkNodeList = new ArrayList<>();
		if (StringUtils.isEmpty(path)) {
			path = "/";
		}
		List<String> children = zk.getChildren(path, false);
		// 补充顶级节点
		if (path.equals("/")) {
			ZkNode root = new ZkNode();
			root.setName(connectHost);
			root.setpName("0");
			zkNodeList.add(root);
		}
		for (String child : children) {
			zkNode = new ZkNode();
			if (path.equals("/")) {
				childPath = path + child;
				zkNode.setpName(connectHost);
			} else {
				childPath = path + "/" + child;
				zkNode.setpName(StringUtils.substring(path, path.lastIndexOf("/") + 1));
			}
			byte b[] = getNodeData(childPath);
			zkNode.setText(new String(b));
			zkNode.setPath(childPath);
			zkNode.setName(child);
			zkNodeList.add(zkNode);
		}

		Collections.sort(zkNodeList, new Comparator<ZkNode>() {
			@Override
			public int compare(ZkNode t1, ZkNode t2) {
				return t1.getName().compareToIgnoreCase(t2.getName());
			}
		});
		return zkNodeList;
	}

	/**
	 * 获取节点数据
	 * 
	 * @param path
	 * @return
	 */
	public byte[] getNodeData(String path) {
		try {
			Stat s = zk.exists(path, false);
			if (s != null) {
				byte[] b = zk.getData(path, false, s);
				if (null == b) {
					return new byte[0];
				}
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * 保存节点数据
	 * 
	 * @param nodePath
	 * @param data
	 * @return
	 */
	public boolean setData(String nodePath, String nodeData) {
		try {
			zk.setData(nodePath, nodeData.getBytes(), -1);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("=setData=>", e);
		}
		return false;
	}

	/**
	 * 添加节点
	 */
	public boolean addNode(String nodePath, String nodeTitle) {
		try {
			String p;
			if (ROOT.equals(nodePath)) {
				p = nodePath + nodeTitle;
			} else {
				p = nodePath + "/" + nodeTitle;
			}
			Stat s = zk.exists(p, false);
			byte[] data = {};
			if (s == null) {
				logger.info("add node:" + nodeTitle);
				zk.create(p, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("=addNode=>", e);
		}
		return false;
	}

	/**
	 * 复制节点
	 */
	public Map<String, Object> copyNode(String nodePath, String nodeTitle) {
		Map<String, Object> map = new HashMap<>();
		String parentPath = "";
		String copyPath = "";
		try {
			if (ROOT.equals(nodePath)) {
				// 根节点不允许复制
				map.put("flag", "fail");
				map.put("copyPath", "");
				return map;
			} else {
				parentPath = nodePath.substring(0, nodePath.lastIndexOf("/"));
				copyPath = parentPath + "/" + nodeTitle;
			}
			Stat s = zk.exists(copyPath, false);
			if (s == null) {
				// 创建平级节点
				zk.create(copyPath, getNodeData(nodePath), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				// 创建子节点
				batchCopyNode(nodePath, copyPath);
				// List<String> childList = zk.getChildren(nodePath, false);
				// String cpChildNodePath = "";
				// if (null != childList && childList.size() > 0) {
				// for (String childNodeName : childList) {
				// cpChildNodePath = copyPath + "/" + childNodeName;
				// logger.debug(">>>>>>>>>>>cpChildNodePath:" +
				// cpChildNodePath);
				// zk.create(cpChildNodePath, getNodeData(nodePath + "/" +
				// childNodeName), Ids.OPEN_ACL_UNSAFE,
				// CreateMode.PERSISTENT);
				//
				// //根据nodePath + "/" + childNodeName再次遍历下级节点,复制
				// getLoopChildNode(nodePath + "/" + childNodeName,
				// cpChildNodePath);
				//
				// }
				// }
			}
			map.put("flag", "success");
			map.put("copyPath", copyPath);
		} catch (Exception e) {
			map.put("flag", "fail");
			map.put("copyPath", copyPath);
			logger.error("=createNode=>", e);
		}
		return map;
	}

	/**
	 * 批量复制节点(多级复制)
	 * 
	 * @param nodePath
	 * @param copyPath
	 * @throws Exception
	 */
	public void batchCopyNode(String nodePath, String copyPath) throws Exception {
		List<String> childList = zk.getChildren(nodePath, false);
		String cpChildNodePath = "";
		if (null != childList && childList.size() > 0) {
			for (String childNodeName : childList) {
				cpChildNodePath = copyPath + "/" + childNodeName;
				logger.debug(">>>>>>>>>>>cpChildNodePath:" + cpChildNodePath);
				zk.create(cpChildNodePath, getNodeData(nodePath + "/" + childNodeName), Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
				batchCopyNode(nodePath + "/" + childNodeName, cpChildNodePath);
			}
		}
	}

	/**
	 * 删除节点
	 */
	public boolean deleteNode(String nodePath) {
		try {
			Stat s = zk.exists(nodePath, false);
			if (s != null) {
				List<String> children = zk.getChildren(nodePath, false);
				for (String child : children) {
					String node = nodePath + "/" + child;
					deleteNode(node);
				}
				logger.info("delete node:" + nodePath);
				zk.delete(nodePath, -1);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("=deleteNode=>", e);
		}
		return false;
	}

	@Override
	public boolean changeZkConn(String connectHost) {
		try {
			connectZookeeper(connectHost);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 获取zk配置信息
	 */
	@Override
	public List<ZkCfg> getZkCfg() {
		return zkManageToolDao.queryZkCfgList();
	}

	/**
	 * 获取zk配置字段描述说明
	 */
	@Override
	public List<ZkPropMsg> getZkPropMsg(String zkPath) {
		return zkManageToolDao.queryZkPropMsgList(zkPath);
	}

}
