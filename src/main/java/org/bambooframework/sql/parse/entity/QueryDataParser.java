package org.bambooframework.sql.parse.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bambooframework.sql.parse.Param;
import org.bambooframework.sql.parse.Parser;
import org.bambooframework.sql.parse.SQLFunction;
import org.bambooframework.sql.parse.SQLFunctionFactory;
import org.bambooframework.sql.parse.SqlExpression;

public class QueryDataParser implements Parser {

	
	@Override
	public Param parser(Map<String, Object> paramData) {
		QueryParam queryParam = new QueryParam();
		queryParam.setDatabase((String) paramData.get("DATABASE"));
		Map<String,Object> data = (Map<String, Object>) paramData.get("DATA");
		queryParam.setQuery(parserView(data));
		return queryParam;
	}
	
	private QueryResultTable parserTable(Map<String,Object> data){
		QueryResultTable table;
		Object name = data.get("TABLE");
		if(name instanceof Map){
			table = parserView(data);
		}else{
			table = new QueryResultTable();
			table.setName((String)name);
			table.setAlias((String) data.get("ALIAS"));
			parserColumns((List<Object>) data.get("COLUMNS"), table);
		}
		return table;
	}
	private QueryResultView parserView(Map<String,Object> data){
		QueryResultView query = new QueryResultView();
		query.setResult(parserTable((Map<String, Object>) data.get("TABLE")));
		parserJoins((List<Map<String, Object>>) data.get("JOINS"), query);
		query.setWhere(parserSqlExpression( data.get("WHERE")));
		query.setOrderBy(parserSqlExpression( data.get("ORDERBY")));
		query.setGroupBy(parserSqlExpression( data.get("GROUPBY")));
		return query;
	}
	private  void parserColumns(List<Object> columns,QueryResultTable resultTable){
		for(int i=0; i<columns.size(); i++){
			Object col = columns.get(i);
			if(col instanceof Map){
				//有三种情况
				//1. 函数 {function:'funcName',args:[],alias:''}
				//2. fk表{colname:'',columns:[]}
				//3. 字段：{colname:'',alias:''}
				//4. 引用表：{table:'',alias:'',columns:[],where,orderby}
				//5. 子查询：{table:{},alias:'',join,where,orderby,groupby}
				Map<String,Object> map = (Map<String, Object>)col;
				String function = (String) map.get("FUNCTION");
				Object table = (String) map.get("TABLE");
				String alias = (String) map.get("ALIAS");
				if(function !=null){
					resultTable.addColumn(alias,parserFunction(map));
				}else if(table!=null){
					resultTable.addColumn(alias,parserTable((Map<String, Object>) table));
				}else{
					resultTable.addColumn(alias,map.get("COLUMN").toString());
				}
			}else{
				resultTable.addColumn(col.toString());	
			}
		}
	}
	
	/**
	 * 解析连接
	 * @author lei
	 * @param joins
	 * @date 2016年7月9日
	 * @Description:
	 */
	private  void parserJoins(List<Map<String,Object>> joins,QueryResultView query){
		for(Map<String,Object> join:joins){
			QueryResultTable table = parserTable(join);
			query.addJoin((String) join.get("TYPE"),table,parserSqlExpression(join.get("ON")));
		}
	}
	
	/**
	 * 解析表达式
	 * @author lei
	 * @param args
	 * @return
	 * @date 2016年7月10日
	 * @Description:
	 */
	private SqlExpression parserSqlExpression(Object expression){
		SqlExpression exp = new SqlExpression();
		if(expression instanceof Map){
			String expressionStr = null;
			Map<String,Object> map = (Map<String,Object>) expression;
			exp.setExpression((String)map.get("EXPRESSION"));
			exp.setArgs(parserExpressionArgs((List<Object>) map.get("ARGS")));
		}else{
			exp.setExpression((String)expression);
		}
		return exp;
	}
	/**
	 * 解析表达式参数
	 * @author lei
	 * @param args
	 * @return
	 * @date 2016年7月10日
	 * @Description:
	 */
	private List<Object> parserExpressionArgs(List<Object> args){
		List<Object> newArgs = new ArrayList<Object>();
		if(args == null || args.size() == 0){
			return newArgs;
		}
		for(int i=0; i<args.size(); i++){
			Object obj = args.get(i);
			if(obj instanceof Map){
				Map<String,Object> map = (Map<String, Object>) obj;
				if( map.get("FUNCTION")!=null){
					
				}
			}
			newArgs.add(obj);
		}
		return newArgs;
	}
	/**
	 * 解析函数
	 * @author lei
	 * @param map
	 * @return
	 * @date 2016年7月10日
	 * @Description:
	 */
	private SQLFunction parserFunction(Map<String,Object> map){
		String func = (String) map.get("FUNCTION");
		List<Object> funcArgs = (List<Object>) map.get("ARGS");
		if(func!=null){
			//函数
			return new SQLFunctionFactory().getFunction(func,null, funcArgs);
		}
		return null;
	}
}
