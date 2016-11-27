package org.bambooframework.dao.db.sql;

/** 可作为字段用的
 * 
 * @author lei
 * @date 2016年11月21日
 * @Description: */
public interface SqlColumnable extends Sqlable {

	String getName();
	
	String getAlias();
	
	SqlTableable getTable();
	
	String getId();
}
