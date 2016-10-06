package org.bambooframework.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;
import org.bambooframework.common.BaseMybatisInterceptor;
import org.bambooframework.common.Pagination;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor extends BaseMybatisInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = getActualTarget(statementHandler);
		Connection connection = (Connection) invocation.getArgs()[0];

		RowBounds rowBounds = (RowBounds) metaStatementHandler
				.getValue("delegate.rowBounds");

		if (rowBounds.getLimit() == Pagination.NO_ROW_LIMIT
				|| !(rowBounds instanceof Pagination)) {
			// 不需要分页
			return invocation.proceed();
		}

		Pagination pagination = (Pagination) rowBounds;

		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = boundSql.getSql();

		int total = getTotalRecord(statementHandler, metaStatementHandler,
				connection, sql);
		pagination.setTotal(total);

		/** 强制将resultSetHandler中的rowBounds */
		rowBounds = new RowBounds();
		metaStatementHandler.setValue("delegate.resultSetHandler.rowBounds",
				rowBounds);

		String dbType = connection.getMetaData().getDatabaseProductName();
		StringBuffer sb = new StringBuffer();
		if ("h2".equalsIgnoreCase(dbType) || "mysql".equalsIgnoreCase(dbType)) {
			sb.append(sql);
			sb.append(" limit ");
			sb.append(pagination.getOffset());
			sb.append(",");
			sb.append(pagination.getLimit());
		} else if ("oracle".equals(dbType)) {

		}
		metaStatementHandler.setValue("delegate.boundSql.sql", sb.toString());
		return invocation.proceed();
	}

	/**
	 * 获取记录总数
	 * 
	 * @author lei
	 * @param statementHandler
	 * @param metaStatementHandler
	 * @param connection
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @date 2015年8月26日
	 * @Description:
	 */
	private int getTotalRecord(StatementHandler statementHandler,
			MetaObject metaStatementHandler, Connection connection, String sql)
			throws SQLException {
		// 替换掉sql最后面的order by 语句
		Pattern p = Pattern.compile("order\\s+by", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		
		//取最后一个order by 的位置
		int end = -1;
		int start = -1;
		while (m.find()) {
			start = m.start();
			end = m.end();
		}
		
		//判断order by 后面的括号是否对等,如果不对等说明order by 被包含在括号内
		if(end>-1){
			//有order by语句
			Stack<Integer> stack = new Stack<Integer>();
			for (int i = end; i < sql.length(); i++) {
				if (sql.charAt(i) == '(') {
					stack.push(i);
				} else if (sql.charAt(i) == ')') {
					if (stack.size() == 0) {
						//栈已经空了,但括号还没关闭,说明order by 包含在括号内
						stack = null;
						break;
					}
					stack.pop();
				}
			}
			if (stack != null && stack.size() == 0) {
				sql = sql.substring(0, start);
			}
		}
		

		StringBuffer countSql = new StringBuffer("select count(1) from (")
				.append(sql).append(") view_");
		metaStatementHandler.setValue("delegate.boundSql.sql",
				countSql.toString());
		Statement stmt = null;
		ResultSet resultSet = null;
		int total;
		try {
			stmt = statementHandler.prepare(connection);
			statementHandler.parameterize(stmt);
			PreparedStatement ps = (PreparedStatement) stmt;
			ps.execute();
			resultSet = ps.getResultSet();
			resultSet.next();
			total = resultSet.getBigDecimal(1).intValue();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return total;
	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的 次数
		if (target instanceof StatementHandler) {
			MetaObject metaStatementHandler = getActualTarget(target);
			if(!"SELECT".equalsIgnoreCase((String)metaStatementHandler.getValue("delegate.mappedStatement.sqlCommandType.name"))){
				return target;
			}
			RowBounds rowBounds = (RowBounds) metaStatementHandler
					.getValue("delegate.rowBounds");
			if(rowBounds!=null && rowBounds.getLimit() != Pagination.NO_ROW_LIMIT
					&& (rowBounds instanceof Pagination)){
				return Plugin.wrap(target, this);
			}
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
