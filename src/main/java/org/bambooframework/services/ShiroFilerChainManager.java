package org.bambooframework.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.bambooframework.dao.IFuncDao;
import org.bambooframework.entity.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 动态url权限控制的url管理
 * @author lei
 * @date 2015年9月5日
 * @Description:
 */
@Service
public class ShiroFilerChainManager{
	
	@Autowired
	ShiroFilterFactoryBean shiroFilterFactoryBean;
	
	@Autowired
	IFuncDao funcDao;
	
	Map<String,String> defaultFilterChains;
	
	@PostConstruct
	public void init() throws Exception{
		//保存默认的url过滤
		defaultFilterChains = new LinkedHashMap<String,String>(shiroFilterFactoryBean.getFilterChainDefinitionMap());
		
		//从数据库中取url初始化
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("existurl", "true");
		List<Func> funcs = funcDao.find(map);
		initFilterChains(funcs);
	}
	
	/**
	 * 将从数据库中取出的权限添加到拦截器中
	 * @author lei
	 * @param funcs
	 * @throws Exception
	 * @date 2015年9月5日
	 * @Description:
	 */
	public void initFilterChains(List<Func> urls)  throws Exception {
		AbstractShiroFilter shiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();
		DefaultFilterChainManager chainManager = (DefaultFilterChainManager)((PathMatchingFilterChainResolver)shiroFilter.getFilterChainResolver()).getFilterChainManager();
		chainManager.getFilterChains().clear();
		
		if (!CollectionUtils.isEmpty(defaultFilterChains)) {
            for (Map.Entry<String, String> entry : defaultFilterChains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                chainManager.createChain(url, chainDefinition);
            }
        }
		for(Func url:urls){
			chainManager.createChain("/"+url.getUrl(), "perms[\""+url.getCode()+"\"]");
		}
	}
}
