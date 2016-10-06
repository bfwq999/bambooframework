package org.bambooframework.dictionary;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SelModelUtils implements ApplicationContextAware  {
	
	private static ApplicationContext ctx;
	private static SelModelFactory selModelFactory;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
		selModelFactory = ctx.getBean(SelModelFactory.class);
	}
	
	/**
	 * 获取字典数据
	 * @author lei
	 * @param codeName 字典名格式为 pre:name,其中pre为前缀,name为字典表中的名称,程序通过 pre寻找指定的loader
	 * @param args
	 * @return
	 * @date 2015年9月20日
	 * @Description:
	 */
	public static List<Map<String, String>> getCodeList(String codeName,Map<String,String> args){
		String[] s = parseCodeName(codeName);
		Loader loader = selModelFactory.getLoader(s[0]);
		Assert.notNull(loader, "没有找到相应"+s[0]+"前缀的loader");
		return loader.load(s[1], args);
	}
	
	/**
	 * 值转文字
	 * @author lei
	 * @param codeName 字典名格式为 pre:name,其中pre为前缀,name为字典表中的名称,程序通过 pre寻找指定的loader
	 * @param val 要转换的值
	 * @return
	 * @date 2015年9月20日
	 * @Description:
	 */
	public static String val2Text(String codeName,String val){
		List<Map<String,String>> list = getCodeList(codeName, null);
		if(val==null){
			return "";
		}
		for(Map<String,String> code:list){
			if(code.get("value").equals(val)){
				return code.get("text");
			}
		}
		return val;
	}
	
	/**
	 * 解析字典名
	 * @author lei
	 * @param codeName
	 * @return
	 * @date 2015年9月20日
	 * @Description:
	 * 	字典名格式为 pre:name,其中pre为前缀,name为字典表中的名称
	 *  程序通过 pre寻找指定的loader
	 */
	private static String[] parseCodeName(String codeName){
		String[] ss = codeName.split(":");
		if(ss.length>1){
			return ss;
		}
		return new String[]{selModelFactory.getDefaultLoader(),ss[0]};
	}
}
