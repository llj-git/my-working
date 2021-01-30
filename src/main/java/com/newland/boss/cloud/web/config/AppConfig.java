package com.newland.boss.cloud.web.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class AppConfig {
	
	private static Log logger = LogFactory.getLog(AppConfig.class);
 	
	public static final String VAR_APP_NAME = "{appName}";

	// 执行结果,成功
	public static final String RESULT_SUCCESS = "0";
	// 执行结果,失败
	public static final String RESULT_FAILED = "1";
	// 应用状态,正常,没在处理
	public static final String APP_STATE_NORMAL = "normal";
	// 应用状态,运行,正在处理
	public static final String APP_STATE_RUNNING = "running";

	private static Properties props;
	
	public static final String BILL_CONN = "usrOracle";
	public static final String ALTIBASE_CONN = "usrAltibase";

	static {
		try {
			Resource resource = new ClassPathResource("bdsweb.properties");
			logger.info( "config path:"+resource.getURI().getPath());
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	private AppConfig() {
		
	}

	public static String getProperty(String key) {
		return props == null ? null : props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return props == null ? null : props.getProperty(key, defaultValue);
	}

	public static Properties getProperties() {
		return props;
	}
}
