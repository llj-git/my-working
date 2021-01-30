package com.newland.boss.cloud.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName StringUtil
 * @Author: Lcc
 * @Date: 2019/4/2 10:28
 */
public class StringUtil {

	public static Object getMethod(String str, Object object)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = object.getClass().getMethod(str, null);
		return method.invoke(object, null);
	}

	/**
	 * other_number=>OtherNumber
	 *
	 * @param field
	 * @return
	 */
	public static String conversionField(String field) {
		StringBuilder builder = new StringBuilder();
		builder.append(field.substring(0, 1).toUpperCase());
		if (field.contains("_")) {
			int f = field.indexOf("_".charAt(0));
			builder.append(field.substring(1, f));
			builder.append(field.substring(f + 1, f + 2).toUpperCase());
			builder.append(field.substring(f + 2));
		} else {
			builder.append(field.substring(1, field.length()));
		}
		field = builder.toString();
		return field.trim();
	}

	public static boolean allFieldNotNull(Object object) throws IllegalAccessException {
		boolean flag = true;
		Class cla = object.getClass();
		Field[] fs = cla.getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			Object val = field.get(object);
			if (val == null) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	
}
