package org.bambooframework.dao.db.sql.parser;

import org.bambooframework.core.exception.BambooException;
import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.db.metadata.Column;
import org.bambooframework.dao.db.metadata.Table;
import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.sql.Query;
import org.bambooframework.dao.db.sql.SqlBasicJavaObject;
import org.bambooframework.dao.db.sql.SqlColumn;
import org.bambooframework.dao.db.sql.SqlColumnable;
import org.bambooframework.dao.db.sql.SqlRefColumn;
import org.bambooframework.dao.db.sql.SqlRelTable;
import org.bambooframework.dao.db.sql.SqlTable;
import org.bambooframework.dao.exception.ParseSqlDataException;

public class SqlParser {
	/**
	 * 
	 * @author lei
	 * @param query
	 * @param columnData
	 * @return
	 * @date 2016年11月23日
	 * @Description:
	 * <pre>
	 *	格式: 1. 简单类型 "12"(整数)、"1.2"(小数)、"'1'"(字符串)
	 *		  2. 表字段"columnname"或{name:'',alias:''}
	 *		  3. 引用表字段  "TABLE.COLUMN" 或 {table:'',name:'',alias:''}
	 * 		  4. 函数 {func:'',args:[字段]}
	 * 		  5. 查询语句
	 * 		  6. 关联表：{TABLENAME:[字段],alias:'',where:[],order:[]}
	 * </pre>
	 */
	public static Object parseSqlTableColumn(SqlTable parent,Table table,Object columnData) {
		if(columnData instanceof String){
			//普通值
			String col =  (String)columnData;
			int type = StringUtils.getType(col);
			switch(type){
			case 1: 
				if(col.equals("NULL") || 
						(col.charAt(0)=='\'' && col.charAt(col.length()-1)=='\'')){
					//字符串
					return new SqlBasicJavaObject(col,parent,parent);
				}
				
				//表
				if(col.indexOf(".")==-1){
					Column column = table.getColumn(col);
					if(column == null){
						throw new ParseSqlDataException("字段{0}.{1}不存在",table.getName(),col);
					}
					return new SqlColumn(parent,column,null);
				}
				return new SqlRefColumn(col, parent, parent);
			case 2: 
				//整数
			case 3:
				//小数
				return new SqlBasicJavaObject(col,parent,parent);
			default:
				throw new BambooException("数据格式有误");
			}
		}else{
			DataMap dataMap = (DataMap)columnData;
			Object name = dataMap.get("name");
			if(name == null){
				//不是字段
				String func = (String) dataMap.get("func");
				if(func != null){
					//函数
					return null;
				}
				
				Object tableName  = dataMap.get("table");
				if(tableName != null){
					//查询语句
					return new Query(dataMap, parent, parent); 
				}
				
				return new SqlRelTable(dataMap, parent);
				
			}else if(name instanceof String){
				//是字段
				String col = (String)name;
				String tableName = (String) dataMap.get("table");
				if(tableName == null && col.indexOf(".")==-1){
					//普通字段
					Column c = table.getColumn(col);
					if(c == null){
						throw new ParseSqlDataException("字段{0}.{1}不存在",table.getName(),col);
					}
					return new SqlColumn(parent,c,(String)dataMap.get("alias"));
				}else{
					//其它表字段
					return new SqlRefColumn(dataMap, parent, parent);
				}
			}
		}
		return null;
	}
	
}
