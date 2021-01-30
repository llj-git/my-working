package com.newland.boss.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.texen.util.PropertiesUtil;

import com.newland.boss.cloud.web.util.DBPoolConnection;

public class TheardTest {

	public static void main(String[] args) {
		/*System.out.println( DBPoolConnection.class.getClassLoader());
		InputStream is = new PropertiesUtil().getClass().getResourceAsStream("dbsrouterule.properties");
		

		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] jdbcURL = prop.getProperty("IP").split(",");
		String[] jdbcUserName = prop.getProperty("partitionIDMin").split(",");
		String[] jdbcUserPwd = prop.getProperty("partitionIDMax").split(",");
		System.out.println(jdbcURL.length);
		System.out.println(jdbcUserName[0]);
		System.out.println(jdbcUserPwd[0]);
	
		System.out.println(593478%200);
		Integer key = 593078;
		String k1 = null;
		for (int i = 0 ;i<jdbcURL.length;i++) {
			if(key >= Integer.parseInt(jdbcUserName[i]) && key <= Integer.parseInt(jdbcUserPwd[i])){
				System.out.println(jdbcURL[i]+"--"+jdbcUserName[i]+"--"+jdbcUserPwd[i]+"--");
				k1 = jdbcURL[i];
				break;
			}
		}
		
		for (int i = 0 ;i<jdbcURL.length;i++) {
			if(k1.equals(jdbcURL[i])){
				System.out.println(i+"--"+jdbcURL[i]);
				break;
			}
		}*/
		
		/*System.out.println(jdbcURL[0].indexOf("@"));
		System.out.println(jdbcURL[0].indexOf(":"));
		String a = jdbcURL[0].substring(jdbcURL[0].indexOf("@") + 1, jdbcURL[0].length());
		System.out.println(a.substring(0, a.indexOf(":")));*/

	/*	String sql0 = "    aect * from metrics_detail                ";
		String sql = "      select * from metrics_detail  where              ";
		String sql1 = "      delete * from metrics_detail                ";
		String sql2 = "      update * from metrics_detail                ";
		String sql3 = "      insert * from metrics_detail                ";
		String formatSQL = sql.toUpperCase().trim();
		String title = formatSQL.substring(0, 6);
		System.out.println(title);
		System.out.println(formatSQL.startsWith("SELECT"));
		System.out.println(formatSQL.contains("WHERE"));

		switch (title) {
		case "SELECT":
			System.out.println("1" + title);
			break;
		case "DELETE":
			System.out.println(title);
			break;
		case "INSERT":
			System.out.println("2" + title);
			break;
		case "UPDATE":
			System.out.println(title);
			break;
		default:
			System.out.println("不符合");
			break;
		}*/
		
		
		
		String a = "asdasdad;asdasdad;";
		int k = a.length()-a.replace(";", "").length();
		
		long s = 1111111111;
		System.out.println(calcPartitionID(599,s));
		if(a.endsWith(";")){
			System.out.println(a.substring(0, a.length()-1));
		}
	}

	
	/**
	 * 根据地市和用户标识计算分区标识
	 * @param homeCity
	 * @param userId
	 * @return
	 */
	public static Integer calcPartitionID(Integer homeCity, Long userId) {
		if (null != homeCity && null != userId) {
			return homeCity.intValue() * 1000 + ((int) (userId.longValue() % 200));
		} else {
			return null;
		}
	}
}
