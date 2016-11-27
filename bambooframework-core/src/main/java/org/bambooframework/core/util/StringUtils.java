package org.bambooframework.core.util;

public abstract class StringUtils {
	
	/**
	 * 判断是否为空
	 * @author lei
	 * @param str
	 * @return
	 * @date 2016年11月20日
	 * @Description:
	 */
	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str.trim()));
	}

	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	/**
	 * 重复字符串
	 * @author lei
	 * @param str 被重复的
	 * @param count 重复次数
	 * @param separator 字符串间分隔等
	 * @return
	 * @date 2016年11月20日
	 * @Description:
	 */
	public static String repeat(String str,int count,String separator){
		if(str == null || count == 0){
			return str;
		}
		StringBuffer sb = new StringBuffer(str);
		for (int i = 1; i < count; i++) {
			if(separator != null){
				sb.append(separator);
			}
			sb.append(str);
		}
		return sb.toString();
	}
	
	/**
	 * 大写
	 * @author lei
	 * @param str
	 * @return
	 * @date 2016年11月20日
	 * @Description:
	 */
	public static String upperCase(String str){
		if(str != null){
			str = str.toUpperCase();
		}
		return str;
	}
	
	/**
	 * 判断字符串是否相等
	 * @author lei
	 * @param str1
	 * @param str2
	 * @return
	 * @date 2016年11月20日
	 * @Description:
	 */
	public static boolean isEqual(String str1,String str2){
		if(str1 == null && str2 == null){
			return true;
		}
		if(str1 !=null){
			return str1.equals(str2);
		}
		return str2.equals(str1);
	}
	
	/**
	 * 获取字符串类型
	 * @author lei
	 * @param str
	 * @return 0：未知，1：字符串，2：整数，3：小数
	 * @date 2016年11月23日
	 * @Description:
	 * 
	 */
	public static int getType(String str){
		if(isEmpty(str)){
			return 0; 
		}
		boolean isInt = true;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c == '.'){
				if(!isInt){
					//遇到二次'.'
					return 1;
				}
				isInt = false;
			}else if(c>'9'|| c<'0'){
				return 1;
			}
		}
		return isInt?2:3;
	}
	
	/**
	 * 去掉前后空格
	 * @author lei
	 * @param str
	 * @return
	 * @date 2016年11月23日
	 * @Description:
	 */
	public static String trim(String str){
		if(str  == null){
			return null;
		}
		return str.trim();
	}
}
