package org.bambooframework.common;

import javax.servlet.http.HttpServletRequest;

/**
 * web工具类
 * @author lei
 * @date 2015年9月2日
 * @Description:
 */
public class WebUtils {
	
	/**
	 * 是否是ajax请求
	 * @author lei
	 * @param request
	 * @return
	 * @date 2015年9月2日
	 * @Description:
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String requestType = request.getHeader("Access-From");
		if(requestType == null){
			return false;
		}
		return "angularjs".equals(requestType);
	}
}
