package org.bambooframework.dao.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.core.util.StringUtils;

public class SqlWhereGroup extends SqlBuilder  implements SqlWhereable {
	List<SqlWhereable> sqlWheres;
	
	List<SqlLogicType> sqlLogicTypes;
	
	public SqlWhereGroup(List<DataMap> source, SqlBuilder parent, SqlTableable scope) {
		super(source, parent, scope);
	}

	/**
	 * <pre>
	 * [{left:col1,sign:"=",right:"1"}
	 *  			,{logic="and",where:[{left:col1,sign:"=",right:"1"}}]]
	 * </pre>
	 */
	@Override
	protected void compile() {
		List<DataMap> source  = (List<DataMap>) this.source;
		if(source == null || source.size()==0){
			return;
		}
		sqlWheres = new ArrayList<SqlWhereable>(source.size());
		sqlLogicTypes = new ArrayList<SqlLogicType>(source.size());
		
		for(DataMap whereData:source){
			String logic = (String) whereData.get("logic");
			if(StringUtils.isEmpty(logic)){
				logic = "AND";
			}else{
				logic = logic.toUpperCase();
			}
			SqlWhereable sqlWhere = newSqlWhere(whereData);
			sqlWheres.add(sqlWhere);
			sqlLogicTypes.add(SqlLogicType.valueOf(logic));
		}
		
	}
	
	protected SqlWhereable newSqlWhere(DataMap whereData){
		List<DataMap> where = (List<DataMap>) whereData.get("where");
		if(where==null){
			return new SqlWhere(whereData, parent, scope);
		}
		return new SqlWhereGroup(where, parent, scope);
	}

	@Override
	protected void link() {
		if(sqlWheres==null){
			return;
		}
		for(SqlWhereable where:sqlWheres){
			((SqlBuilder)where).link();
		}
	}

	@Override
	public String toSql() {
		if(this.sqlWheres.size()>0){
			StringBuffer sql = new StringBuffer();
			sql.append("(")
				.append(sqlWheres.get(0).toSql());
			for(int i=1; i<sqlWheres.size(); i++){
				sql.append(" ")
				.append(sqlLogicTypes.get(i).getValue())
				.append(" ")
				.append(sqlWheres.get(i).toSql());
			}
			sql.append(")");
			return sql.toString();
		}
		return null;
	}

	@Override
	public String getDatabase() {
		return null;
	}

	public List<SqlWhereable> getSqlWheres() {
		return sqlWheres;
	}

	public List<SqlLogicType> getSqlLogicTypes() {
		return sqlLogicTypes;
	}

}
