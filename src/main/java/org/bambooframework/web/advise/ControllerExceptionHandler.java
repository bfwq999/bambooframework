package org.bambooframework.web.advise;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.bambooframework.common.HttpStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {

	/**
	 * 校验不通过异常
	 * @author lei
	 * @param ex
	 * @return
	 * @date 2015年9月15日
	 * @Description:
	 */
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = BindException.class)
	protected BindException processBindException(BindException ex) {
		ex.printStackTrace();
		return ex;
	}

	
	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public UnauthorizedException processUnauthenticatedException(UnauthorizedException ex) {
		ex.printStackTrace();
		return ex;
	}

	/**
	 * 请求不正确,或处理异常
	 * @author lei
	 * @param response
	 * @param ex
	 * @return
	 * @date 2015年9月15日
	 * @Description:
	 */
	@ExceptionHandler(value = HttpStatusException.class)
	public HttpStatusException processHttpStatusException(HttpServletResponse response,HttpStatusException ex){
		ex.printStackTrace();
		response.setStatus(ex.getHttpStatus());
		return ex;
	}
	
	/**
	 * 其它系统异常
	 * @author lei
	 * @param ex
	 * @return
	 * @date 2015年9月15日
	 * @Description:
	 */
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Exception processOthersException(Exception ex) {
		ex.printStackTrace();
		return ex;
	}
	
}
