package com.newland.boss.cloud.web.balanceLibrary.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface SQLDMLService {

	public Map<String, String> sqlQueryList(String sql) throws InterruptedException, ExecutionException;

	public Map<String, String> insertSql(String[] sqlArray, Integer partitionID);
}
