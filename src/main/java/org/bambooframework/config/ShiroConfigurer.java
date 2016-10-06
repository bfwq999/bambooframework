package org.bambooframework.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.bambooframework.auth.shiro.UserRealm;
import org.bambooframework.auth.shiro.filter.MyPermissionAuthorizationFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfigurer {
	
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
	    ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
	    shiroFilter.setLoginUrl("/login.html");
	    //shiroFilter.setSuccessUrl("/main.html");
	    //shiroFilter.setUnauthorizedUrl("/login.html");
	    
	    Map<String, String> filterChainDefinitionMapping = new LinkedHashMap<String, String>();
	    filterChainDefinitionMapping.put("/resources/**", "anon");
	    filterChainDefinitionMapping.put("/login.html", "anon");
	    filterChainDefinitionMapping.put("/index.html", "anon");
	    filterChainDefinitionMapping.put("/login", "anon");
	    filterChainDefinitionMapping.put("/main.html", "authc");
	    //filterChainDefinitionMapping.put("/views/auth/user.html", "anon");
	    //filterChainDefinitionMapping.put("/views/**", "authc");
	    shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
	    shiroFilter.setSecurityManager(securityManager());
	    Map<String, Filter> filters = new HashMap<String, Filter>();
	    filters.put("perms", new MyPermissionAuthorizationFilter());
//	    filters.put("anon", new AnonymousFilter());
//	    filters.put("authc", formAuthenticationFilter);
//	    filters.put("logout", new LogoutFilter());
//	    filters.put("roles", new RolesAuthorizationFilter());
//	    filters.put("user", new UserFilter());
	    shiroFilter.setFilters(filters);
	    
	    return shiroFilter;
	}

	@Bean
	public SecurityManager securityManager() {
	    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	    securityManager.setRealm(userRealm());
	    securityManager.setCacheManager(cacheManager());
	    return securityManager;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
	    return new LifecycleBeanPostProcessor();
	}
	
	@Bean
	@DependsOn(value="lifecycleBeanPostProcessor")
	public UserRealm userRealm(){
		UserRealm realm =  new UserRealm();
		realm.setCachingEnabled(true);
		realm.setAuthenticationCachingEnabled(true);
		realm.setAuthenticationCacheName("authenticationCache");
		realm.setAuthorizationCachingEnabled(true);
		realm.setAuthorizationCacheName("authorizationCacheName");
		return realm;
	}
	
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager());
		return advisor;
	}
	
	@Bean
	public EhCacheManager cacheManager(){
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		
		return cacheManager;
	}
}
