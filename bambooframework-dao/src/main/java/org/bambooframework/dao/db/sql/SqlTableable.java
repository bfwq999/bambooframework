package org.bambooframework.dao.db.sql;

import java.util.List;


/**
 * 可作为表使用的
 * @author lei
 * @date 2016年11月21日
 * @Description:
 */
public interface SqlTableable extends Sqlable{
	
	public String getAlias();
	
	public String getName();
	
	public List<? extends SqlColumnable> getColumns();
	
	/**
	 * 获取字段
	 * @author lei
	 * @param colName 字段名
	 * @return
	 * @date 2016年11月26日
	 * @Description:
	 */
	public SqlColumnable getColumn(String colName);
	
	/**
	 * 在作用域中查询可用字段
	 * @author lei
	 * @param colName
	 * @param caller 防止出现循环查找
	 * @return
	 * @date 2016年11月27日
	 * @Description:
	 */
	public SqlColumnable findColumn(String colName,Sqlable caller);
	
}
