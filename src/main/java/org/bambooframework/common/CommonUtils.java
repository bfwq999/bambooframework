package org.bambooframework.common;

/**
 * 工具类
 * @author lei
 * @date 2015年9月12日
 * @Description:
 */
public class CommonUtils {
	/**
	 * 是否是管理员账户
	 * @author lei
	 * @param userName
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	public static boolean isAdminAccount(String userName){
		return CodeConstants.ADMIN_ACCOUNT.equals(userName);
	}
	
	/**
	 * 判断字符串是否是空字符串
	 * @author lei
	 * @param str
	 * @return
	 * @date 2015年11月8日
	 * @Description:
	 */
	public static boolean isEmpty(String str){
		if(str == null){
			return true;
		}
		return "".equals(str.trim());
	}
}
