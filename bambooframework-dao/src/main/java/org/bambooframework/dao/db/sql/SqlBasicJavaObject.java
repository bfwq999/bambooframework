package org.bambooframework.dao.db.sql;

import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.exception.ParseSqlDataException;

/** 基本数据类型
 * 
 * @author lei
 * @date 2016年11月27日
 * @Description: */
public class SqlBasicJavaObject extends SqlBuilder implements SqlColumnable {

	String val;
	SqlBasicJavaType type;
	String alias;

	public SqlBasicJavaObject(String source, SqlBuilder parent, SqlTableable scope) {
		super(source, parent, scope);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public SqlTableable getTable() {
		return null;
	}
	
	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public String toSql() {
		if (this.type == SqlBasicJavaType.STRING) {
			return "'" + this.val + "'";
		}
		if (this.type == SqlBasicJavaType.NULL) {
			return "NULL";
		}
		return this.val;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void compile() {
		String source = (String) this.source;
		if (source.charAt(0) == '\'' && source.charAt(source.length() - 1) == '\'') {
			// 字符串
			this.type = SqlBasicJavaType.STRING;
			this.val = source.substring(1, source.length() - 1);
		} else {
			this.val = source;
			int type = StringUtils.getType(source);
			switch (type) {
			case 1:
				// 字符串
				if (source.equals("NULL")) {
					this.type = SqlBasicJavaType.NULL;
				} else {
					throw new ParseSqlDataException("数据格式不对:", source);
				}
				break;
			case 2:
				// 整数
				this.type = SqlBasicJavaType.INTEGER;
				break;
			case 3:
				// 小数
				this.type = SqlBasicJavaType.FLOAT;
				break;
			default:
				throw new ParseSqlDataException("数据格式不对:", source);
			}
		}
	}

	@Override
	protected void link() {
	}

}
