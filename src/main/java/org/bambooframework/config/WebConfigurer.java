package org.bambooframework.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.bambooframework.web.filter.AngularjsFilter;
import org.bambooframework.web.method.support.QueryParamArgumentResolver;
import org.bambooframework.web.servlet.resource.HtmlResourceHttpRequestHandler;
import org.h2.server.web.WebServlet;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * web配置
 * 
 * @author lei
 * @date 2015年8月22日
 * @Description:
 */
@Configuration
@ConditionalOnWebApplication
public class WebConfigurer extends WebMvcConfigurerAdapter implements ServletContextAware,ApplicationContextAware {
	
	ServletContext servletContext;
	
	ApplicationContext applicationContext;
	
	@Bean
	public FilterRegistrationBean angularjsFilterRegistrationBean(
			AngularjsFilter angularjsFilter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(angularjsFilter);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/views/*");
		return filterRegistrationBean;
	}
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(
				new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}
	
	@Bean
	public SimpleUrlHandlerMapping htmlHandlerMapping() {
		List<Resource> locations = new ArrayList<Resource>();
		locations.add(new ServletContextResource(servletContext, "/"));
		ResourceHttpRequestHandler requestHandler = new HtmlResourceHttpRequestHandler();
		requestHandler.setLocations(locations);
		requestHandler.setServletContext(servletContext);
		requestHandler.setApplicationContext(applicationContext);
		
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Integer.MIN_VALUE + 1);
		mapping.setUrlMap(Collections.singletonMap("/**/*.html",requestHandler));
		
		return mapping;
	}
	
	

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new QueryParamArgumentResolver());
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		registry.enableContentNegotiation(new MappingJackson2JsonView());
//	}
}
