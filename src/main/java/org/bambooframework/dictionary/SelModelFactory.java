package org.bambooframework.dictionary;

import java.util.HashMap;
import java.util.Map;

public class SelModelFactory {
	Map<String,Loader> loaders = new HashMap<String, Loader>();
	
	private  String defaultLoader;
	
	/**
	 * 
	 * @author lei
	 * @param pre
	 * @return
	 * @date 2015年9月20日
	 * @Description:
	 */
	public Loader getLoader(String pre){
		return loaders.get(pre);
	}
	
	/**
	 * 获取所有loader
	 * @author lei
	 * @return
	 * @date 2015年9月20日
	 * @Description:
	 */
	public Map<String,Loader> getLoaders(){
		return loaders;
	}
	
	public void registerLoader(String pre,Loader loader){
		loaders.put(pre, loader);
	}

	public String getDefaultLoader() {
		return defaultLoader;
	}

	public void setDefaultLoader(String defaultLoader) {
		this.defaultLoader = defaultLoader;
	}
}
