package com.newland.boss.cloud.web.zkManageTool.export;

public class GenerateSql {
	
	public static final int OPERATOR_ID = 99999999;
	
	/**
	 * 生成删除语句
	 * @param reqNo
	 * @param zkId
	 * @return
	 */
	public static String generateClearSql(String reqNo, int zkId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("-------------------------");
		sbSql.append("clear");
		sbSql.append("-------------------------");
		sbSql.append("\r\n");
		sbSql.append(generateDeleteSql(reqNo, zkId));
		sbSql.append("\r\n");
		sbSql.append("\r\n");
		sbSql.append("\r\n");
		return sbSql.toString();
	}
	
	/**
	 * 生成插表语句
	 * @param reqNo
	 * @param zkId
	 * @param absolutePath
	 * @param nodeValue
	 * @return
	 */
	public static String generate(String reqNo, int zkId,  String absolutePath, String nodeValue) {
		StringBuffer sbSql = new StringBuffer();
		//note
		sbSql.append("-------------------------");
		sbSql.append(absolutePath);
		sbSql.append("-------------------------");
		sbSql.append("\r\n");
		//insert 
		sbSql.append(generateInsertSq(reqNo, zkId, absolutePath));
		sbSql.append("\r\n");
		sbSql.append("\r\n");
		//update
		sbSql.append(generateUpdateSql(reqNo, zkId, absolutePath, nodeValue));
		sbSql.append("\r\n");
		sbSql.append("\r\n");
		sbSql.append("\r\n");
		return sbSql.toString();
	}
	
	
	
	public static String generateDeleteSql(String reqNo, int zkId) {
		return String.format("DELETE FROM ZK_NODE_APPEND WHERE REQ_NO='%s' AND ZK_ID=%d;", reqNo, zkId);
	}
	
	public static String generateInsertSq(String reqNo, int zkId, String absolutePath) {
		return String.format("INSERT INTO ZK_NODE_APPEND (REQ_NO,ZK_ID,ABSOLUTE_PATH,NODE_VALUE,CREATE_TIME,OPERATOR_ID) VALUES ('%s', %d,'%s',empty_clob(),sysdate,%d);", reqNo,zkId,absolutePath, OPERATOR_ID);   
	}
	
	public static String generateUpdateSql(String reqNo, Integer zkId, String absolutePath, String nodeValue) {
		return String.format("UPDATE ZK_NODE_APPEND  SET NODE_VALUE = '%s' WHERE REQ_NO='%s' AND ZK_ID=%d AND ABSOLUTE_PATH = '%s';", nodeValue, reqNo, zkId, absolutePath);
	}
}
