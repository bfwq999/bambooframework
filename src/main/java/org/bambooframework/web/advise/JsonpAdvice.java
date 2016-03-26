package org.bambooframework.web.advise;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 添加对jsonp的支持
 * @author lei
 * @date 2015年10月7日
 * @Description:
 * 	当请求参数含有callback时,当成jsonp请求对等
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
	public JsonpAdvice() {
		super("callback");
	}
}
