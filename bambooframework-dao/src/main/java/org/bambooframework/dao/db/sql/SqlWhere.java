package org.bambooframework.dao.db.sql;

import org.bambooframework.core.exception.BambooException;
import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.exception.ParseSqlDataException;

public class SqlWhere extends SqlBuilder implements SqlWhereable {

	SqlColumnable left;
	SqlColumnable right;
	String sign;

	public SqlWhere(DataMap source, SqlBuilder parent, SqlTableable scope) {
		super(source, parent, scope);
	}

	@Override
	public String toSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(left.toSql())
		.append(" ")
		.append(sign)
		.append(" ")
		.append(right.toSql());
		return sql.toString();
	}

	@Override
	public String getDatabase() {
		return null;
	}

	@Override
	protected void compile() {

	}

	/** {left:col1,sign:"=",right:"1" */
	@Override
	protected void link() {
		DataMap source = (DataMap) this.source;
		Object left = (String) source.get("left");
		Object right = (String) source.get("right");
		String sign = (String) source.get("sign");
		this.left = getSqlColumn(left);
		if(this.left == null){
			throw new ParseSqlDataException("{1}中的字段({0})不存在：", left.toString(),source.toString());
		}
		this.right = getSqlColumn(right);
		if(this.right == null){
			throw new ParseSqlDataException("{1}中的字段({0})不存在", right.toString(),source.toString());
		}
		this.sign = sign;
		if(this.sign == null){
			throw new ParseSqlDataException("运算符({0})不存在", source.toString());
		}
	}

	protected SqlColumnable getSqlColumn(Object columnData) {
		if(columnData instanceof String){
			String col = (String) columnData;
			int type = StringUtils.getType(col);
			switch(type){
			case 1: 
				if(col.equals("NULL") || 
						(col.charAt(0)=='\'' && col.charAt(col.length()-1)=='\'')){
					//字符串
					return new SqlBasicJavaObject(col,this,this.scope);
				}
				
				//表
				return this.scope.findColumn(col,this);
			case 2: 
				//整数
			case 3:
				//小数
				return new SqlBasicJavaObject(col,this,this.scope);
			default:
				throw new BambooException("数据格式有误");
			}
		}else{
			DataMap columnMap = (DataMap) columnData;
			String func = (String) columnMap.get("func");
			if(func != null){
				
			}
		}
		return null;
	}


	public SqlColumnable getLeft() {
		return left;
	}

	public SqlColumnable getRight() {
		return right;
	}

	public String getSign() {
		return sign;
	}
}
