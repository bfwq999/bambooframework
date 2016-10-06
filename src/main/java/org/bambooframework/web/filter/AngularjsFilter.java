package org.bambooframework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

/**
 * 对angularjs route的请求进行处理
 * @author lei
 * @date 2015年8月22日
 * @Description:
 * 	如果不是angularjs的请求,则自动跳转到index.jsp页面
 */
@Component
public class AngularjsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//response.addHeader( "Cache-Control", "no-cache" );//浏览器和缓存服务器都不应该缓存页面信息
		//response.addHeader( "Cache-Control", "no-store" );//请求和响应的信息都不应该被存储在对方的磁盘系统中；    
		//response.addHeader( "Cache-Control", "must-revalidate" );//于客户机的每次请求，代理服务器必须想服务器验证缓存是否过时；
		//response.setDateHeader("Expires",0);
		
		String path =  new UrlPathHelper().getLookupPathForRequest(request);
		String flag = request.getHeader("Access-From");
		System.out.println("path:"+path+"======Access-From:"+flag);
		if(flag == null || !"angularjs".equalsIgnoreCase(flag)){
			System.out.println("*****************forward");
			request.getRequestDispatcher("/index?url="+path).forward(request, response);
			return;
		}
		filterChain.doFilter(request,response);
	}

}
