package com.newland.boss.cloud.web.balanceLibrary.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.newland.boss.cloud.re.core.util.JacksonHelper;
import com.newland.boss.cloud.web.balanceLibrary.dao.AltiRouteCfgDao;
import com.newland.boss.cloud.web.balanceLibrary.dao.SQLDMLDao;
import com.newland.boss.cloud.web.balanceLibrary.entity.AltiRouteCfg;
import com.newland.boss.cloud.web.balanceLibrary.service.SQLDMLService;
import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.cloud.web.util.DBThread;

@Component
public class SQLDMLServiceImpl implements SQLDMLService {

	private static Log LOGGER = LogFactory.getLog(SQLDMLServiceImpl.class);
	private static final String SQL_EXCEPTION = "SQLException | JsonProcessingException ";
	private static final String TABLE_DATA = "data";
	private static final String RESULT = "result";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String MSG = "msg";

	private SQLDMLDao sqlDMLDao = new SQLDMLDao();
	private AltiRouteCfgDao routeCfgDao = new AltiRouteCfgDao();

	@Override
	public Map<String, String> sqlQueryList(String sql) throws InterruptedException, ExecutionException {
		Map<String, String> resultMap = new HashMap<>();
		List<String> dataLists = new ArrayList<>();
		// 获得线程任务的个数
		int taskSize = 0;
		try {
			taskSize = DBPoolConnection.getDBNameListSize();
			// 创建一个线程池
			ExecutorService pool = Executors.newFixedThreadPool(taskSize-2);
			//过滤掉type=2,3
			List<String> poolList = DBPoolConnection.getIPList();
			int altiBaseNum = poolList.indexOf("usrAltibase");
			int oraCleNum = poolList.indexOf("usrOracle");
			// 创建多个有返回值的任务
			List<Future> list = new ArrayList<>();
			for (int i = 0; i < taskSize; i++) {
				if (i == altiBaseNum || i == oraCleNum) {
					continue;
				}
				
				Callable<?> c = new DBThread(i, sql);
				// 执行任务并获取Future对象
				Future<?> f = pool.submit(c);
				list.add(f);
			}
			// 关闭线程池
			pool.shutdown();

			// 获取所有并发任务的运行结果
			for (Future<?> f : list) {
				// 从Future对象上获取任务的返回值，并输出到控制台
				List<String> re = (List<String>) f.get();
				for (String string : re) {
					dataLists.add(string);
				}
			}

			resultMap.put(RESULT, TRUE);
			resultMap.put(TABLE_DATA, JacksonHelper.writeBean2Json(dataLists.toString()));

		} catch (Exception e) {
			LOGGER.error(SQL_EXCEPTION, e);
			resultMap.put(RESULT, FALSE);
			resultMap.put(MSG, e.getMessage());
		}
		return resultMap;
	}

	@Override
	public Map<String, String> insertSql(String[] sqlArray, Integer partitionID) {
		Map<String, String> resultMap = new HashMap<>();

		// 匹配对应的Pool序号
		int dbPoolId = -1;
		// 归属的数据库IP
		String dbPoolName = null;

		try {
			List<AltiRouteCfg> routeCfgList = routeCfgDao.loadRouteCfgList();

			// 判断该记录属于哪个库
			for (int i = 0; i < routeCfgList.size(); i++) {
				if (partitionID >= routeCfgList.get(i).getBeginPartitionId()
						&& partitionID <= routeCfgList.get(i).getEndPartitionId()) {
					LOGGER.info("记录归属-----partitionID:" + partitionID + ",DB_NAME:" + routeCfgList.get(i).getDbName()
							+ ",BeginPartitionId:" + routeCfgList.get(i).getBeginPartitionId() + ",EndPartitionId:"
							+ routeCfgList.get(i).getEndPartitionId());
					// 匹配成功
					dbPoolId = i;
					dbPoolName = routeCfgList.get(i).getDbName();
					break;
				}
			}

			LOGGER.info("连接池------dbPoolId:" + dbPoolId + ",DBPoolConnection.getDBNameListSize():"
					+ DBPoolConnection.getDBNameListSize());

			if (dbPoolId == -1 || dbPoolId >= DBPoolConnection.getDBNameListSize()
					|| !dbPoolName.equals(DBPoolConnection.getDBNameList(dbPoolId))) {
				LOGGER.info("连接池匹配失败------partitionID:" + partitionID);
				resultMap.put(RESULT, FALSE);
				resultMap.put(MSG, "PartitionID:" + partitionID + "---未查找到匹配DBS路由规则或未配置DB连接");
				return resultMap;
			}

			resultMap.put(RESULT, TRUE);
			resultMap.put(TABLE_DATA, JacksonHelper.writeBean2Json(sqlDMLDao.insertSQL(sqlArray, dbPoolId).toString()));

		} catch (Exception e) {
			LOGGER.error(SQL_EXCEPTION, e);
			resultMap.put(RESULT, FALSE);
			resultMap.put(MSG, e.getMessage());
		}
		return resultMap;
	}

}
