package org.bambooframework.common;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class BaseService {
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class,IBaseDao> DAO_BEANS_CACHE = new HashMap<Class, IBaseDao>();
	
	@Autowired
	ApplicationContext ctx;
	
	public <T> T getBean(Class<T> requiredType){
		return ctx.getBean(requiredType);
	}
	
	/**
	 * 根据entity获取实际的Dao接口
	 * @author lei
	 * @param clazz entity类
	 * @return 实际的Dao接口,如t 为User.class,则返回IUserDao
	 * @date 2015年9月11日
	 * @Description:
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public  <T> IBaseDao<T> getActualDao(Class<T> clazz){
		IBaseDao<T> dao = DAO_BEANS_CACHE.get(clazz);
		if(dao!=null){
			return dao;
		}
		
		//取得所有继承IBaseDao的类
		Map<String,IBaseDao> daos  = ctx.getBeansOfType(IBaseDao.class);
		for(IBaseDao baseDao : daos.values()){
			//由于获取到的baseDao是代理类,需要获取被代理的接口
			Class interf = (Class)baseDao.getClass().getGenericInterfaces()[0];
			for(Type type:interf.getGenericInterfaces()){
				if(type instanceof ParameterizedType){
					//如果接口有泛型,判断泛型是否和entity对象类型一致
					if(((ParameterizedType)type).getActualTypeArguments()[0] == clazz){
						DAO_BEANS_CACHE.put(clazz, baseDao);
						return baseDao;
					}
				}
			}
		}
		return null;
	}
	
	/** 
	 * 插入
	 * @author lei
	 * @param t 要插入的entity
	 * @date 2015年9月11日
	 * @Description:
	 */
	@SuppressWarnings("unchecked")
	public <T> void insert(T t){
		getActualDao((Class<T>)t.getClass()).insert(t);
	}
	
	/** 
	 * 更新
	 * @author lei
	 * @param t 要更新的entity
	 * @date 2015年9月11日
	 * @Description:
	 */
	@SuppressWarnings("unchecked")
	public <T> void update(T t){
		getActualDao((Class<T>)t.getClass()).update(t);
	}
	
	/** 
	 * 根据主键删除
	 * @author lei
	 * @param clazz 要删除的实体类
	 * @param  pk 要删除的实体的主键值
	 * @date 2015年9月11日
	 * @Description:
	 */
	public <T> void delete(Class<T> clazz,Serializable pk){
		getActualDao(clazz).delete(pk);
	}
	
	/**
	 * 根据实体主键值获取对象
	 * @author lei
	 * @param clazz 要获取的实体类
	 * @param pk 主键
	 * @return
	 * @date 2015年9月11日
	 * @Description:
	 */
	public <T> T get(Class<T> clazz,Serializable pk){
		return getActualDao(clazz).get(pk);
	}
	
	/**
	 * 单表分页查询
	 * @author lei
	 * @param clazz entity对象
	 * @param condition 查询条件
	 * @param pagination 分页
	 * @return
	 * @date 2015年9月11日
	 * @Description:
	 */
	public <T> List<T> find(Class<T> clazz,Map<String,Object> condition,Pagination pagination){
		if(pagination == null){
			return getActualDao(clazz).find(condition);
		}
		return getActualDao(clazz).find(condition, pagination);
	}
	
	/**
	 * 单表不分页查询
	 * @author lei
	 * @param clazz entity对象
	 * @param condition
	 * @return
	 * @date 2015年9月11日
	 * @Description:
	 */
	public <T> List<T> find(Class<T> clazz,Map<String,Object> condition){
		return getActualDao(clazz).find(condition);
	}
}
