package org.bambooframework.dao.db.sql;

import java.util.List;

/**
 * 能转换成sql语句的
 * @author lei
 * @date 2016年11月21日
 * @Description:
 */
public interface Sqlable {
	/**
	 * 转换成sql
	 * @author lei
	 * @return
	 * @date 2016年11月21日
	 * @Description:
	 */
	String toSql();
	
	/**
	 * 获取值域
	 * @author lei
	 * @return
	 * @date 2016年11月26日
	 * @Description:
	 */
	SqlTableable getScope();
	
	/**
	 * 最顶层
	 * @author lei
	 * @return
	 * @date 2016年11月27日
	 * @Description:
	 */
	Query top();
	
}
