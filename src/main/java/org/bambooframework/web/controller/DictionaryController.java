package org.bambooframework.web.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.common.CodeConstants;
import org.bambooframework.dictionary.SelModelUtils;
import org.bambooframework.dictionary.TableLoader;
import org.bambooframework.web.vo.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典
 * @author lei
 * @date 2015年9月20日
 * @Description:
 */
@RestController
public class DictionaryController {
	
	@Autowired
	TableLoader tbLoader;
	
	@RequestMapping(value="/consts",method=RequestMethod.GET)
	public Map<String,Object> codeConstants(){
		Map<String,Object> consts = new HashMap<String, Object>();
		try {
			Field[] fields = CodeConstants.class.getDeclaredFields();
			for(Field field:fields){
				consts.put(field.getName(),field.get(null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return consts;
	}
	
	/**
	 * 静态字典
	 * @author lei
	 * @return
	 * @date 2015年10月8日
	 * @Description:
	 */
	@RequestMapping(value="/staticcodes",method=RequestMethod.GET)
	public Map<String, List<Map<String, String>>> staticCodes(){
		return tbLoader.getCache();
	}
	
	/**
	 * 获取字典列表
	 * @author lei
	 * @param codeName
	 * @param queryParam
	 * @return
	 * @date 2015年10月8日
	 * @Description:
	 */
	@RequestMapping(value="/codes/{codeName}")
	public List<Map<String, String>> getCodeList(@PathVariable("codeName") String codeName,
			QueryParam queryParam){
		return SelModelUtils.getCodeList(codeName, queryParam.getStringMap());
	}
}
