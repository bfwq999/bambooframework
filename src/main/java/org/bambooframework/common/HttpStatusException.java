package org.bambooframework.common;

/**
 * http请求返回异常类
 * @author lei
 * @date 2015年9月15日
 * @Description:
 */
public class HttpStatusException extends RuntimeException {
	private static final long serialVersionUID = 7440711041248641854L;
	
	private int httpStatus; //http状态编码

	public HttpStatusException(int httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
