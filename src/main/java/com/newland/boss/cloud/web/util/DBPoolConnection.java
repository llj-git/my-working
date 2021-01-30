package com.newland.boss.cloud.web.util;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.newland.boss.cloud.web.balanceLibrary.dao.AltiJdbcCfgDao;
import com.newland.boss.cloud.web.balanceLibrary.entity.AltiJdbcCfg;
import com.newland.boss.cloud.web.config.AppConfig;
import com.newland.boss.life.NextChange;
import com.newland.boss.life.dao.GetDataDao;

@Component
public class DBPoolConnection {

	private static final Log LOGGER = LogFactory.getLog(DBPoolConnection.class);

	private static DBPoolConnection dbPoolConnection = null;
	private static DruidDataSource druidDataSource = null;
	private static List<DruidDataSource> druidDataSourceList = new ArrayList<>();
	private static List<String> dbNameList = new ArrayList<>();
	public static  List<HashMap>  appDeployEntityList = null;
	public static List<HashMap> appDeployMutualAidRelationList = null;
	public static List<HashMap> checkWaitingDurationList = null;
	public static long sessionTimeOut = 0; //会话执行超时时间,可配置
 

	static {
		InputStream is = DBPoolConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
			druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(prop);

			AltiJdbcCfgDao jdbcCfgDao = new AltiJdbcCfgDao();
			List<AltiJdbcCfg> jdbcCfgList = jdbcCfgDao.loadJdbcCfgList(druidDataSource);
			for (AltiJdbcCfg altiJdbcCfg : jdbcCfgList) {
				prop.setProperty("url", altiJdbcCfg.getJdbcUrl());
				prop.setProperty("username", altiJdbcCfg.getJdbcUser());
				prop.setProperty("password", altiJdbcCfg.getJdbcPwd());
				prop.setProperty("driverClassName", altiJdbcCfg.getDriverClass());
				druidDataSourceList.add((DruidDataSource) DruidDataSourceFactory.createDataSource(prop));
				dbNameList.add(altiJdbcCfg.getDbName());
				LOGGER.info("altiJdbcCfg---" + altiJdbcCfg.toString());
			}
			
			
			appDeployEntityList = new GetDataDao().getAppDeployInfoList();
			appDeployMutualAidRelationList = new GetDataDao().getAppDeployMutualAidRelationList();
			checkWaitingDurationList = new GetDataDao().getCheckWaitingDurationList();
			sessionTimeOut = Long.parseLong(AppConfig.getProperty("timeOut"));
			
			NextChange nextChange=new NextChange();
			nextChange.init();
			
			
		} catch (Exception e) {
			LOGGER.error("Exception.", e);
		}
	}

	/**
	 * 数据库连接池单例
	 * 
	 * @return
	 */
	public static synchronized DBPoolConnection getInstance() {
		if (null == dbPoolConnection) {
			dbPoolConnection = new DBPoolConnection();
		}
		return dbPoolConnection;
	}

	/**
	 * 获取druid数据连接池
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidPooledConnection getConnection() throws SQLException {
		return druidDataSource.getConnection();
	}

	/**
	 * 获取druid数据连接池
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidPooledConnection getConnection(Integer dbPoolId) throws SQLException {
		return druidDataSourceList.get(dbPoolId).getConnection();
	}

	/**
	 * 获取DB集合的信息
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static String getDBNameList(Integer num) {
		return dbNameList.get(num);
	}

	/**
	 * 获取DB集合的大小
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Integer getDBNameListSize() throws SQLException {
		return dbNameList.size();
	}

	/**
	 * 获取DB集合
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getIPList() throws SQLException {
		return dbNameList;
	}

	/**
	 * 获取Bill连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidPooledConnection getBillConnection() throws SQLException {
		int dbPoolId = dbNameList.indexOf(AppConfig.BILL_CONN);
		return druidDataSourceList.get(dbPoolId).getConnection();
	}
	

	/**
	 * 用户资料altibase库
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidPooledConnection getUsrAltibaseConnection() throws SQLException {
		int dbPoolId = dbNameList.indexOf(AppConfig.ALTIBASE_CONN);
		return druidDataSourceList.get(dbPoolId).getConnection();
	}
}
