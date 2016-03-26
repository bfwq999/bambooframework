package org.bambooframework.web.method.support;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.bambooframework.web.vo.QueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 查询参数的数据组装
 * @author lei
 * @date 2015年8月29日
 * @Description:
 * 	
 */
public class QueryParamArgumentResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(QueryParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		String name = ModelFactory.getNameForParameter(parameter);
		QueryParam queryParam = (QueryParam)(mavContainer.containsAttribute(name) ?
				mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, webRequest));
		ServletRequest request = webRequest.getNativeRequest(ServletRequest.class);
		Map<String,String[]> params = request.getParameterMap();
		
		int pos = name.length();
		
		for(Map.Entry<String, String[]> entry:params.entrySet()){
			String key = entry.getKey();
			String val = null;
			if(entry.getValue()!=null &&entry.getValue().length>0){
				val =  entry.getValue()[0];
			}
			
			if(key.indexOf(name)>-1 && key.length()>pos){
				if(key.charAt(pos) == '.'){
					queryParam.put(key.substring(pos+1), val);
				}else if(key.charAt(pos) == '['){
					queryParam.put(key.substring(pos+1,key.length()-1), val);
				}
			}
		}
		return queryParam;
	}

	protected Object createAttribute(String attributeName, MethodParameter methodParam,
			WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

		return BeanUtils.instantiateClass(methodParam.getParameterType());
	}
	
}
