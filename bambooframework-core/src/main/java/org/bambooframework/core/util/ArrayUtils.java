package org.bambooframework.core.util;

import java.lang.reflect.Array;

public class ArrayUtils {
	
	/**
	 * 将数组拼接成字符串,默认字符串间用逗号分隔
	 * @author lei
	 * @param strArray
	 * @return
	 * @date 2016年11月19日
	 * @Description:
	 */
	public static String toString(String[] strArray){
		return toString(strArray, ",");
	}
	
	/**
	 * 将数组拼接成字符串
	 * @author lei
	 * @param strArray
	 * @param separator 字符串间的分隔符合
	 * @return
	 * @date 2016年11月19日
	 * @Description:
	 */
	public static String toString(String[] strArray,String separator){
		if(strArray == null || strArray.length == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(String str:strArray){
			if(sb.length()>0 && separator != null){
				sb.append(separator);
			}
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static <T> T[] newArray(Class<T> clazz,int length,T defaultVal){
		T[] ts = (T[]) Array.newInstance(clazz, length);
		for (int i = 0; i < ts.length; i++) {
			ts[i] =  defaultVal;
		}
		return ts;
	}
}
