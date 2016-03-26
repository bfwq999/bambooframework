package org.bambooframework.common;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

public class BaseMybatisInterceptor {

	/**
	 * 得到实际被代理的对象
	 * @author lei
	 * @param target
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	public static MetaObject getActualTarget(Object target){
		MetaObject metaStatementHandler = SystemMetaObject.forObject(target); 
		 // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环  
	     // 可以分离出最原始的的目标类)  
	     while (metaStatementHandler.hasGetter("h")) {  
	         Object object = metaStatementHandler.getValue("h");  
	         metaStatementHandler = SystemMetaObject.forObject(object);  
	     }  
	     // 分离最后一个代理对象的目标类  
	     while (metaStatementHandler.hasGetter("target")) {  
	         Object object = metaStatementHandler.getValue("target");  
	         metaStatementHandler = SystemMetaObject.forObject(object);  
	     }  
	     return metaStatementHandler;
	}
}
