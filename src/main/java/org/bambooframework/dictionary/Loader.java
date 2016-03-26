package org.bambooframework.dictionary;

import java.util.List;
import java.util.Map;

public interface Loader {

	/**
	 * 加载数据
	 * @author lei
	 * @param code
	 * @param args
	 * @return
	 * @date 2015年9月19日
	 * @Description:
	 */
	List<Map<String, String>> load(String code,Map<String,String> args);
	
	
	/**
	 * 清理所有缓存
	 * @author lei
	 * @date 2015年9月20日
	 * @Description:
	 */
	void cleanCache();
	
	/**
	 * 清理指定codeName的缓存
	 * @author lei
	 * @param codeName
	 * @date 2015年9月20日
	 * @Description:
	 */
	void cleanCache(String codeName);
}
