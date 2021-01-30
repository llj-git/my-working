package com.newland.boss.cloud.web.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.newland.boss.cloud.web.balanceLibrary.dao.SQLDMLDao;

public class DBThread implements Callable<List<String>> {

	private static final Log LOGGER = LogFactory.getLog(DBThread.class);

	private Integer threadNumber;

	private String sql;

	public DBThread(Integer threadNumber, String sql) {
		super();
		this.threadNumber = threadNumber;
		this.sql = sql;
	}

	@Override
	public List<String> call() throws Exception {
		LOGGER.info(new Date() + "---ThreadNumber:" + threadNumber + ",sql:" + sql);
		List<String> resultLists = null;
		SQLDMLDao sqlDao = new SQLDMLDao();
		String title = sql.substring(0, 6).toUpperCase();
		switch (title) {
		case "SELECT":
			resultLists = sqlDao.sqlQueryList(sql, threadNumber);
			break;
		case "DELETE":
			resultLists = sqlDao.deleteSQL(sql, threadNumber);
			break;
		case "UPDATE":
			resultLists = sqlDao.updateSQL(sql, threadNumber);
			break;
		default:
			break;
		}
		return resultLists;
	}

}
