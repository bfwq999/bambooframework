package org.bambooframework.common;

import org.bambooframework.dao.IPropertyDao;
import org.bambooframework.entity.Property;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator implements ApplicationContextAware {
	
	private static ApplicationContext ctx;  
	
	private static IPropertyDao propertyDao; 
	
	private static final int DEFAULT_CAPACITY = 50;
	
	private static int currVal = 0; //当前位置
	private static int lastVal = -1; //最大位置
	
	public static synchronized Integer getNextId(){
		if(currVal>lastVal){
			grow();
		}
		return currVal++;
	}
	/**  扩充数据 */
	private static void grow(){
		Property property = propertyDao.get("nextid");
		
		int val = Integer.valueOf(property.getValue());
		int lval = val+DEFAULT_CAPACITY; //保留DEFAULT_CAPACITY个
		
		//数据库中的当前值要比lastVal大1,否则就重了
		property.setValue(String.valueOf(lval)); 
		propertyDao.update(property);
		
		//保证数据库值更新成功后
		currVal = val;
		lastVal = lval-1;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
		propertyDao = ctx.getBean(IPropertyDao.class);
	}
}
